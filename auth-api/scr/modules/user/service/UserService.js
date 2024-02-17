import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

import UserRepository from "../repository/UserRepository.js";
import UserException from "../exception/UserException.js";
import * as httpStatus from "../../../config/constants/httpStatus.js";
import * as secrets from "../../../config/constants/secrets.js"

class UserService {

    async findByEmail(req) {
        try {
            const { email } = req.params;
            const { authUser } = req;
            this.validateRequestDate(email);

            let user = await UserRepository.findByEmail(email);
            
            this.validateUserNotFound(user);
            this.validateAuthUser(authUser, user);

            return {
                status: httpStatus.SUCCESS,
                user: {
                    id: user.name,
                    name: user.name,
                    email: user.email
                }
            };
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message
            }
        }
    }

    validateRequestDate(email) {
        if (!email) {
            throw new UserException(httpStatus.BAD_REQUEST, "User email was not informed.");
        }
    }

    validateUserNotFound(user) {
        if (!user) {
            throw new UserException(httpStatus.NOT_FOUND, "User was not found.");
        }
    }

    validateAuthUser(authUser, user) {
        if (!authUser || user.id != authUser.id) {
            throw new UserException(httpStatus.FORBIDDEN, "You cannot see this user data.");
        }
    }

    async getAccesToken(req) {
        try {
            const { transactionid, serviceid } = req.headers;
            console.info(`Request to POST login with data ${JSON.stringify(req.body)} |  transactionid: ${transactionid} | serviceid: ${serviceid}]`);

            const { email, password } = req.body;
            this.validateAccessTokenData(email, password);

            let user = await UserRepository.findByEmail(email);
            this.validateUserNotFound(user);

            await this.validatePassword(password, user.password);

            const authUser = {
                id: user.id, 
                name: user.name,
                email: user.email
            };

            const accessToken = jwt.sign(
                { authUser }, 
                secrets.API_SECRET,
                {expiresIn: "1d"}
            );

            let response = {
                status: httpStatus.SUCCESS,
                accessToken,
            }
            
            console.info(`Response to POST login with data ${JSON.stringify(response)} |  transactionid: ${transactionid} | serviceid: ${serviceid}]`);
            return response;
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message
            }
        }
    }

    validateAccessTokenData(email, password) {
        if (!email || !password) {
            throw new UserException(httpStatus.UNAUTHORIZED, "Email and password must be informed.")
        }
    }

    async validatePassword(password, hashPassword) {
        if (!await bcrypt.compare(password, hashPassword)) {
            throw new UserException(httpStatus.UNAUTHORIZED, "Password doesn't match.")
        }
    }
}

export default new UserService();