import com.codecool.buyourstuff.util.BaseData;

import java.sql.*;

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
        } catch (SQLException e){
            e.printStackTrace();
        }
        try (Connection con = DriverManager.getConnection(LOCALHOST_5432 + DB_NAME, USER, PASSWORD)) {
//            CreateDB.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDataBase() throws SQLException {
        String SqlQuery = "CREATE DATABASE " + DB_NAME;
        Statement statement = connection.createStatement();
        statement.execute(SqlQuery);
    }
}
