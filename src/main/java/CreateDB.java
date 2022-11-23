import com.codecool.buyourstuff.model.Product;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.Supplier;
import com.codecool.buyourstuff.model.User;
import com.codecool.buyourstuff.util.BaseData;

import java.sql.*;
import java.util.List;

public class CreateDB {
    //TODO you need to set 'USER' and 'PASSWORD' in environment variables

    private static final String DEFAULT_DBNAME = "postgres";
    private static final String LOCALHOST_5432 = "jdbc:postgresql://localhost:5432/";
    private static final String USER = System.getenv("USER");
    private static final String PASSWORD = System.getenv("PASSWORD");
    private static final String DB_NAME = "bos_webshop";
    private Connection connection;

    public CreateDB(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(LOCALHOST_5432 + DEFAULT_DBNAME, USER, PASSWORD)) {
            System.out.println("Creating database...");
            CreateDB createDB = new CreateDB(con);
            createDB.createDataBase();
            System.out.println("Database created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (Connection con = DriverManager.getConnection(LOCALHOST_5432 + DB_NAME, USER, PASSWORD)) {
            CreateDB createDB = new CreateDB(con);
            //ATTENTION: order is extremely important here because of Foreign keys
            createDB.createSuppliersTable();
            createDB.createProductCategoriesTable();
            createDB.createCartsTable();
            createDB.createProductsTable();
            createDB.createUsersTable();
            createDB.createLineItemsTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDataBase() throws SQLException {
        String SqlQuery = "CREATE DATABASE " + DB_NAME + ";";
        Statement statement = connection.createStatement();
        statement.execute(SqlQuery);
    }

    private void createSuppliersTable() throws SQLException {
        String SqlQuery = "CREATE TABLE IF NOT EXISTS suppliers (" +
                "supplier_id serial PRIMARY KEY," +
                "\"name\" varchar(10) NOT NULL," +
                "description text);";
        Statement statement = connection.createStatement();
        statement.execute(SqlQuery);
    }

    private void createProductCategoriesTable() throws SQLException {
        String SqlQuery = "CREATE TABLE IF NOT EXISTS product_categories (" +
                "product_category_id serial PRIMARY KEY," +
                "\"name\" varchar(20) NOT NULL," +
                "description text," +
                "department varchar(20));";
        Statement statement = connection.createStatement();
        statement.execute(SqlQuery);
    }

    private void createCartsTable() throws SQLException {
        String SqlQuery = "CREATE TABLE IF NOT EXISTS carts (" +
                "cart_id serial PRIMARY KEY," +
                "currency varchar(4) DEFAULT 'USD' NOT NULL);";
        Statement statement = connection.createStatement();
        statement.execute(SqlQuery);
    }

    private void createProductsTable() throws SQLException {
        String SqlQuery = "CREATE TABLE IF NOT EXISTS products (" +
                "product_id serial PRIMARY KEY, " +
                "\"name\" varchar(40) NOT NULL, " +
                "price decimal(5,2) NOT NULL, " +
                "currency varchar(4) NOT NULL, " +
                "description text, " +
                "product_category_id int, " +
                "supplier_id int, " +
                "CONSTRAINT fk_product_categories " +
                "FOREIGN KEY (product_category_id) REFERENCES product_categories(product_category_id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE SET NULL, " +
                "CONSTRAINT fk_suppliers " +
                "FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE SET NULL);";
        Statement statement = connection.createStatement();
        statement.execute(SqlQuery);
    }

    private void createUsersTable() throws SQLException {
        String SqlQuery = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id serial PRIMARY KEY, " +
                "\"name\" varchar(20) NOT NULL, " +
                "\"password\" varchar(100) NOT NULL," +
                "cart_id int, " +
                "CONSTRAINT fk_cart " +
                "FOREIGN KEY (cart_id) REFERENCES carts(cart_id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE SET NULL);";
        Statement statement = connection.createStatement();
        statement.execute(SqlQuery);
    }

    private void createLineItemsTable() throws SQLException {
        String SqlQuery = "CREATE TABLE IF NOT EXISTS line_items (" +
                "line_items_id serial PRIMARY KEY, " +
                "product_id int, " +
                "cart_id int, " +
                "quantity int NOT NULL DEFAULT 1, " +
                "CONSTRAINT fk_product " +
                "FOREIGN KEY (product_id) REFERENCES products(product_id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE CASCADE, " +
                "CONSTRAINT fk_cart " +
                "FOREIGN KEY (cart_id) REFERENCES carts(cart_id) " +
                "ON UPDATE CASCADE " +
                "ON DELETE CASCADE);";
        Statement statement = connection.createStatement();
        statement.execute(SqlQuery);
    }


}
