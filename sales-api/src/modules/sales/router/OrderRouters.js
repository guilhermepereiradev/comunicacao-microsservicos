import { Router } from "express";
import OrderController from "../controller/OrderController.js";


const router = new Router();


router.get("/api/orders/:id", OrderController.findById);
router.post("/api/orders", OrderController.createOrder);


export default router;