import bcrypt from "bcrypt";
import User from "../../modules/user/model/User.js";

export async function createInitialData() {
    try {
        await User.sync({ force: true });

        let password = await bcrypt.hash("12345678", 10);

        await User.create({
            name: "User Test",
            email: "userTest@email.com",
            password: password
        })


        await User.create({
            name: "User Test 2",
            email: "userTest2@email.com",
            password: password
        })
    } catch (err) {
        console.log(err);
    }
}