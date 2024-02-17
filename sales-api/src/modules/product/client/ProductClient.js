import axios from "axios";
import { PRODUCT_API_URL } from "../../../config/constants/secrets.js";

class ProductClient {

    async checkProductStock(order, token, transactionid) {
        const headers = {
            Authorization: token,
            transactionid
        };

        console.info(`Sending request to Product API with data ${JSON.stringify(order.products)} | [transactionid ${transactionid}]`);

        await axios.post(
            `${PRODUCT_API_URL}/check-stock`,
            { products: order.products },
            { headers }
        )
        .then(res => { 
            console.info(`Success response from Prodcut-API | [transactionid ${transactionid}]`);
        })
        .catch(err => { 
            console.error(`Error response from Prodcut-API | [transactionid ${transactionid}]`);
            throw err 
        });
    }
}

export default new ProductClient();