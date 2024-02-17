const env = process.env;

export const API_SECRET = env.API_SECRET 
? env.API_SECRET 
: "Y3J1emVpcm8sIGNydXplaXJvIHF1ZXJpZG8sIHTDoG8gY29tYmF0aWRvLCBqYW1haXMgdmVuY2lkbw==";

export const DB_HOST = env.DB_HOST ? env.DB_HOST : "localhost";

export const DB_PORT = env.DB_PORT ? env.DB_PORT : 5433;

export const DB_NAME = env.DB_NAME ? env.DB_NAME : "auth-db";

export const DB_USER = env.DB_USER ? env.DB_USER : "postgres";

export const DB_PASSWORD = env.DB_PASSWORD ? env.DB_PASSWORD : "12345678";