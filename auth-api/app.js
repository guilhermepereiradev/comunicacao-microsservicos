import express from "express";
import * as db from "./scr/config/db/initialData.js"
import userRoutes from "./scr/modules/user/routes/UserRoutes.js"
import tracing from "./scr/config/tracing.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

db.createInitialData();

app.use(express.json());

app.use(tracing);

app.get("/api/status", (req, res) => {
    return res.status(200).json({
        service: "Auth-API",
        status: "up",
        httpStatus: 200,
    })
})

app.use(userRoutes);

app.listen(PORT, () => {    
    console.info(`Server started succesfully at port ${PORT}`);
})