import UserService from "../service/UserService.js";

class UserController {

    async findByEmail(req, res) {
        let user = await UserService.findByEmail(req);
        
        return res.status(user.status).json(user);
    }

    async getAccessToken(req, res) {
        let accessToken = await UserService.getAccesToken(req, res);

        return res.status(accessToken.status).json(accessToken);
    }
}

export default new UserController();