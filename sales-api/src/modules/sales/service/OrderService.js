import OrderRepository from "../repository/OrderRepository.js";
import { sendMessageToProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import { PENDING, ACCEPTED, REJECTED } from "../status/OrderStatus.js";
import OrderException from "../exception/OrderException.js"
import * as httpStatus from "../../../config/constants/httpStatus.js"
import ProductClient from "../../product/client/ProductClient.js";

class OrderService {
    async createOrder(req) {
        try {
            
            let products = req.body;
            this.validateOrderData(products);

            const { authUser } = req;
            const { authorization } = req.headers;

            let order = this.createInitialOrderData(products, authUser);
            await this.validateProductStock(order, authorization);

            let createdOrder = await OrderRepository.save(order);

            this.sendMessage(createdOrder);

            return {
                status: httpStatus.SUCCESS,
                createdOrder
            };
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.BAD_REQUEST,
                message: err.message
            }
        }
    }

    async updateOrder(message) {
        try {
            const orderMessage = JSON.parse(message);

            if (!orderMessage.salesId || !orderMessage.status) {
                console.warn("The order message was not complete");
                return;
            }

            let existingOrder = await OrderRepository.findById(orderMessage.salesId);

            if (order && order.status !== existingOrder.status) {
                existingOrder.status = order.status;
                existingOrder.updatedAt = new Date();
                
                await OrderRepository.save(existingOrder);
            }
        } catch (err) {
            console.error("Could not parse order message from queue.");
            console.error(err.message)
        }
    }

    createInitialOrderData(products, authUser) {
        return {
            user: authUser,
            status: PENDING,
            createdAt: new Date(),
            updatedAt: new Date(),
            products: orderData
        }
    }

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new OrderException("The products must be informed!");
        }
    }

    async validateProductStock(order, token) {
        let stockIsOut = await ProductClient.checkProductStock(order.products, token);

        if (stockIsOut) {
            throw new OrderException(httpStatus.BAD_REQUEST, "The stock is out for the products.");
        }
    }

    sendMessage(createdOrder) {
        const message = {
            salesId: createdOrder.id,
            products: createdOrder.products
        };

        sendMessageToProductStockUpdateQueue(message);
    }
}

export default new OrderService();