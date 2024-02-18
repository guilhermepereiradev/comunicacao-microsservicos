const env = process.env;

export const MONGO_DB_URL = env.MONGO_DB_URL
    ? env.MONGO_DB_URL
    : "mongodb://admin:12345678@localhost:27017/";

export const API_SECRET = env.API_SECRET
    ? env.API_SECRET
    : "Y3J1emVpcm8sIGNydXplaXJvIHF1ZXJpZG8sIHTDoG8gY29tYmF0aWRvLCBqYW1haXMgdmVuY2lkbw==";

export const RABBIT_MQ_URL = env.RABBIT_MQ_URL
    ? env.RABBIT_MQ_URL
    : "amqp://guest:guest@localhost:5672/";

export const PRODUCT_API_URL = env.PRODUCT_API_URL
    ? env.PRODUCT_API_URL
    : "http://localhost:8081/api/products";