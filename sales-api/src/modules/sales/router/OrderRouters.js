import { Router } from "express";
import OrderController from "../controller/OrderController.js";

const router = new Router();

router.get("/api/orders/:id", OrderController.findById);
router.post("/api/orders", OrderController.createOrder);
router.get("/api/orders", OrderController.findAll);
router.get("/api/orders/products/:productId", OrderController.findByProductId);

export default router;