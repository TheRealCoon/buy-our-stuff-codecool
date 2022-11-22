import com.codecool.buyourstuff.util.BaseData;

import java.sql.*;

public class CreateDB {
    //Todo you need to set 'USER' and 'PASSWORD' in environment variables
    private static final String DEFAULT_DBNAME = "postgres";
    private static final String LOCALHOST_5432 = "jdbc:postgresql://localhost:5432/";
    private static final String USER = "USER";
    private static final String PASSWORD = "PASSWORD";
    private static final String DB_NAME = "BOS_webshop";
    private Connection connection;

    public CreateDB(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(LOCALHOST_5432 + DEFAULT_DBNAME, USER, PASSWORD)) {
            System.out.println("Creating database...");
            CreateDB createDB = new CreateDB(con);
            createDB.createDataBase(DB_NAME);
            System.out.println("Database created");
        } catch (SQLException e){
            e.printStackTrace();
        }
        try (Connection con = DriverManager.getConnection(LOCALHOST_5432 + DB_NAME, USER, PASSWORD)) {
//            CreateDB.createTable(BaseData.defaultProducts());
//            CreateDB.createTable(BaseData.defaultSuppliers());
//            CreateDB.createTable(BaseData.defaultUsers());
//            CreateDB.createTable(BaseData.defaultProductCategories());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDataBase(String name) throws SQLException {
        String SqlQuery = "CREATE DATABASE ?;";
        PreparedStatement ps = connection.prepareStatement(SqlQuery);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
