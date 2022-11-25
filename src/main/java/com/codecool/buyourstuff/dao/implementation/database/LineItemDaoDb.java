package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.LineItemDao;
import com.codecool.buyourstuff.model.*;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import static com.codecool.buyourstuff.dao.implementation.database.DbLogin.*;

public class LineItemDaoDb implements LineItemDao {
    @Override
    public void add(LineItem lineItem) {
        String SqlQuery = "INSERT INTO line_items(product_id, cart_id, quantity) VALUES(?, ?, ?);";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, lineItem.getProduct().getId());
            ps.setInt(2, lineItem.getCartId());
            ps.setInt(3, lineItem.getQuantity());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            lineItem.setId(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public LineItem find(int id) {
//        String SqlQuery = "SELECT l.product_id, l.cart_id, l.quantity " +
//                "p.\"name\", p.price, p.currency, p.description, " +
//                "pc.product_category_id, pc.\"name\", pc.description, pc.department, " +
//                "s.supplier_id, s.\"name\", s.description " +
//                "FROM line_items as l " +
//                "JOIN products as p ON p.product_id = l.product_id " +
//                "JOIN product_categories as pc ON pc.product_category_id = p.product_category_id " +
//                "JOIN suppliers as s ON s.supplier_id = p.supplier_id " +
//                "WHERE line_items_id = ?;";
//       LineItem lineItem = null;
//        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
//            PreparedStatement ps = connection.prepareStatement(SqlQuery);
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                int productId = rs.getInt(1);
//                int cartId = rs.getInt(2);
//                int quantity = rs.getInt(3);
//                String productName = rs.getString(4);
//                BigDecimal price = rs.getBigDecimal(5);
//                String currency = rs.getString(6);
//                String productDescription = rs.getString(7);
//                ProductCategory pc = new ProductCategory(rs.getString(6), rs.getString(7), rs.getString(8));
//                pc.setId(rs.getInt(5));
//                Supplier supplier = new Supplier(rs.getString(10), rs.getString(11));
//                supplier.setId(rs.getInt(9));
//                Product product = new Product(name, price, currency, description, pc, supplier);
//                product.setId(id);
//            } else throw new DataNotFoundException("No such product");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return product;
//    }

    @Override
    public void remove(LineItem lineItem) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void update(LineItem lineItem, int quantity) {

    }

    @Override
    public List<LineItem> getBy(Cart cart) {
        return null;
    }
}
