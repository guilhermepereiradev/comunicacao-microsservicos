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

app.use(express.json());

function startApplication() {
    if (env.NODE_ENV === CONTAINER_ENV) {
        console.info("Waiting for RabbitMQ and MongoDB containers to start...")
        setInterval(() => {            
            connectMongoDb();
            connectRabbitMq();
        }, THREE_MINUTES)
    } else {
        connectMongoDb();
        connectRabbitMq();
        createInitialData();
    }
}

startApplication()

app.get("/api/initial-data", (req, res) => {
    createInitialData();
    return res.status(200).json({ message: "Data created." })
})


app.use(tracing);
app.use(checkToken);
app.use(orderRoutes);

app.get("/api/status", async (req, res)=> {
    return res.status(200).json({
        service: "Sales-API",
        status: "up",
        httpStatus: 200,
    })
})


app.listen(PORT, () => {
    console.info(`Server started successfully at port ${PORT}`);
})