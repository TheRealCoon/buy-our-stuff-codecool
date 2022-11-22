import java.sql.*;

public class CreateDB {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "USER";
    static final String PASSWORD = "PASSWORD";
    private Connection connection;


    public CreateDB(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Creating database...");
            CreateDB createDB = new CreateDB(con);
            createDB.createDataBase("BOS_webshop");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void createDataBase(String name) throws SQLException {
        String SqlQuery = "CREATE DATABASE ?;";
        PreparedStatement ps = connection.prepareStatement(SqlQuery);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
    }
}
