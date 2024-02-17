import axios from "axios";
import { PRODUCT_API_URL } from "../../../config/constants/secrets.js";

class ProductClient {

    async checkProductStock(order, token) {
        const headers = {
            Authorization: token
        };

        console.info(`Sending request to Product API with data ${JSON.stringify(order.products)}`);

        await axios.post(
            `${PRODUCT_API_URL}/check-stock`,
            { products: order.products },
            { headers }
        )
        .then(res => { })
        .catch(err => { throw err });
    }
}

export default new ProductClient();