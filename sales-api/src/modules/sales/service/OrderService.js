import OrderRepository from "../repository/OrderRepository.js";
import { sendMessageToProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import { PENDING, ACCEPTED, REJECTED } from "../status/OrderStatus.js";
import OrderException from "../exception/OrderException.js"
import * as httpStatus from "../../../config/constants/httpStatus.js"
import ProductClient from "../../product/client/ProductClient.js";
import { response } from "express";

class OrderService {
    async createOrder(req) {
        try {
            let orderData = req.body;
            
            const { transactionid, serviceid } = req.headers;
            console.info(`Request to POST order with data ${JSON.stringify(orderData)} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`);

            this.validateOrderData(orderData);

            const { authUser } = req;
            const { authorization } = req.headers;

            let order = this.createInitialOrderData(orderData, authUser, transactionid, serviceid);
            await this.validateProductStock(order, authorization, transactionid);

            let createdOrder = await OrderRepository.save(order);

            this.sendMessage(createdOrder, transactionid);

            let response = {
                status: httpStatus.SUCCESS,
                createdOrder
            };

            console.info(`Response to POST order: ${JSON.stringify(response)} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`);
            return response;
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.BAD_REQUEST,
                message: err.message
            }
        }
    }

    async findById(req) {
        try {
            const { id } = req.params;
            
            const { transactionid, serviceid } = req.headers;
            console.info(`Request to GET order by id: ${id} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`);
            
            this.validateInformedId(id);

            let order = await OrderRepository.findById(id);

            if (!order) {
                throw new OrderException(httpStatus.NOT_FOUND, `Order not found for ID: ${id}`);
            }

            let response = {
                status: httpStatus.SUCCESS,
                order
            };

            console.info(`Response to GET order by id: ${JSON.stringify(response)} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`);
            return response;
        } catch(err) {
            return {
                status: err.status ? err.status : httpStatus.BAD_REQUEST,
                message: err.message
            }
        }
    }

    async findAll(req) {
        try {
            const { transactionid, serviceid } = req.headers;
            console.info(`Request to GET all orders | [transactionid: ${transactionid} | serviceid: ${serviceid}]`);

            let orders = await OrderRepository.findAll();

            let response = {
                status: httpStatus.SUCCESS,
                orders
            };

            console.info(`Response to GET all orders: ${JSON.stringify(response)} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`);
            return response;
        } catch(err) {
            return {
                status: err.status ? err.status : httpStatus.BAD_REQUEST,
                message: err.message
            }
        }
    }

    async findByProductId(req) {
        try {
            const { productId } = req.params;

            const { transactionid, serviceid } = req.headers;
            console.info(`Request to GET orders by productId: ${productId} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`);

            this.validateInformedProductId(productId);

            let orders = await OrderRepository.findByProductId(productId);

            if (!orders) {
                throw new OrderException(httpStatus.NOT_FOUND, `No orders were found for product ID: ${id}`);
            }

            let response = {
                status: httpStatus.SUCCESS,
                salesIds: orders.map(order => {
                    return order.id
                })
            };
            
            console.info(`Response to GET orders by productId: ${JSON.stringify(response)} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`);
            return response;
        } catch(err) {
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
                console.warn(`The order message was not complete | [transactionid: ${message.transactionid}]`);
                return;
            }

            let existingOrder = await OrderRepository.findById(orderMessage.salesId);

            if (existingOrder && existingOrder.status !== orderMessage.status) {
                existingOrder.status = orderMessage.status;
                existingOrder.updatedAt = new Date();
                
                await OrderRepository.save(existingOrder);
            }
        } catch (err) {
            console.error("Could not parse order message from queue.");
            console.error(err.message)
        }
    }

    createInitialOrderData(orderData, authUser, transactionid, serviceid) {
        return {
            user: authUser,
            status: PENDING,
            createdAt: new Date(),
            updatedAt: new Date(),
            transactionid,
            serviceid,
            products: orderData.products
        }
    }

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new OrderException(httpStatus.BAD_REQUEST, "The products must be informed!");
        }
    }

    async validateProductStock(order, token, transactionid) {
        try {
            await ProductClient.checkProductStock(order, token, transactionid);
        } catch (err) {
            let errorMessage = "Cannot validade product stock.";
            let errorStatusCode = httpStatus.BAD_REQUEST;

            if (err.response && err.response.data) {
                errorMessage = err.response.data.message;
                errorStatusCode = err.response.data.status;
            }

            throw new OrderException(errorStatusCode, errorMessage)
        }
    }

    sendMessage(createdOrder, transactionid) {

        const message = {
            salesId: createdOrder.id,
            products: createdOrder.products,
            transactionid
        };

        sendMessageToProductStockUpdateQueue(message);
    }

    validateInformedId(id) {
        if (!id) {
            throw new OrderException(httpStatus.BAD_REQUEST, "The order ID must be informed.");
        }
    }

    validateInformedProductId(productId) {
        if (!productId) {
            throw new OrderException(httpStatus.BAD_REQUEST, "The order's productId must be informed.");
        }
    }
}

export default new OrderService();