import axios from "axios";
import { PRODUCT_API_URL } from "../../../config/constants/secrets.js";

class ProductClient {

    async checkProductStock(order, token) {
        try {
            const headers = {
                Authorization: token
            };

            console.info(`Sending request to Product API with data ${JSON.stringify(order.products)}`);
            let response = false;

            await axios.post(
                        `${PRODUCT_API_URL}/check-stock`,
                        { products: order.products },
                        { headers }
                    )
                .then(res => {
                    response = true;
                })
                .catch(err => {
                    console.error(err.response.data.message);
                    response = false;
                });
            return response;
        } catch (err) {
            return false;
        }
    }
}

export default new ProductClient();