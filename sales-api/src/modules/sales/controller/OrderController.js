import OrderService from "../service/OrderService.js";

class OrderController {
    async createOrder(req, res) {
        let order = await OrderService.createOrder(req);

        return res.status(order.status).json(order);
    }

    async findById(req, res) {
        let order = await OrderService.findById(req);

        return res.status(order.status).json(order);
    }

    async findAll(req, res) {
        let orders = await OrderService.findAll();

        return res.status(orders.status).json(orders);
    }

    async findByProductId(req, res) {
        let salesId = await OrderService.findByProductId(req);

        return res.status(salesId.status).json(salesId);
    }
}

export default new OrderController();