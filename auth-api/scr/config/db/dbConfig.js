import Sequelize from "sequelize";

const sequelize = new Sequelize("auth-db", "postgres", "12345678", {
    host: "localhost",
    port: 5433,
    dialect: "postgres",
    quoteIdentifiers: false,
    define: {
        syncOnAssociation: true,
        timestamps: false,
        underscored: true,
        underscoredAll: true,
        freezeTableName: true,
    }
});

sequelize.authenticate()
.then(() => {
    console.info("Connection has been stabilished!");
})
.catch((err) => {
    console.error("Unable connect to the database");
    console.error(err.message);
})

export default sequelize;