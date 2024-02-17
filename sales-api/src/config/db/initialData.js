import Order from "../../modules/sales/model/Order.js";
import { v4 as uuidv4 } from "uuid";

export async function createInitialData() {
    await Order.collection.drop();

    await Order.create({
        products: [
            {
                productId: 1000,
                quantity: 3
            },
            {
                productId: 1001,
                quantity: 2
            },
            {
                productId: 1002,
                quantity: 1
            }
        ],
        user: {
            id: "1cebfe2b-0442-4137-8ee9-bd1f4d082329",
            name: "User Test",
            email: "user@email.com"
        },
        status: "APPROVED",
        createdAt: new Date(),
        updatedAt: new Date(),
        transactionid: uuidv4(),
        serviceid: uuidv4(),
    });

    await Order.create({
        products: [
            {
                productId: 1001,
                quantity: 4
            },
            {
                productId: 1002,
                quantity: 2
            }
        ],
        user: {
            id: "df2ed115-31d9-4bfe-aa62-0d4a5aa19008",
            name: "User Test 2",
            email: "user2@email.com"
        },
        status: "REJECTED",
        createdAt: new Date(),
        updatedAt: new Date(),
        transactionid: uuidv4(),
        serviceid: uuidv4(),
    });

    let initialData = await Order.find();
    console.log(`Initial data was created ${JSON.stringify(initialData, undefined, 4)}`);
}