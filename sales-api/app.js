import express from 'express'
import { connectMongoDb } from './src/config/db/mongodbConfig.js';
import { createInitialData } from './src/config/db/initialData.js';
import checkToken from "./src/config/auth/checkToken.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js";
import orderRoutes from "./src/modules/sales/router/OrderRouters.js";
import tracing from './src/config/tracing.js';

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;
const CONTAINER_ENV = "container";
const THREE_MINUTES = 180000;

function startApplication() {
    if (env.NODE_ENV === CONTAINER_ENV) {
        console.info("Waiting for RabbitMQ and MongoDB containers to start...")
        setTimeout(() => {            
            connectMongoDb();
            connectRabbitMq();
        }, THREE_MINUTES)
    } else {
        connectMongoDb();
        connectRabbitMq();
        createInitialData();
    }
}

startApplication();
app.use(express.json());

app.get("/api/initial-data", (req, res) => {
    createInitialData();
    return res.status(200).json({ message: "Data created." });
})


app.get("/", (req, res) => {
    return res.status(200).json(getSuccessReponse());
})

app.use(tracing);
app.use(checkToken);
app.use(orderRoutes);

app.get("/api/status", (req, res) => {
    return res.status(200).json(getSuccessReponse());
})

function getSuccessReponse() {
    return {
        service: "Sales-API",
        status: "up",
        httpStatus: 200,
    };
}



app.listen(PORT, () => {
    console.info(`Server started successfully at port ${PORT}`);
});