package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.LineItemDao;
import com.codecool.buyourstuff.model.*;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.math.BigDecimal;
import java.sql.*;

import com.codecool.buyourstuff.dao.ProductDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.LineItem;
import com.codecool.buyourstuff.model.Product;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.codecool.buyourstuff.dao.implementation.database.DbLogin.*;

public class LineItemDaoDb implements LineItemDao {
    @Override
    public void add(LineItem lineItem) {

        String sql = "INSERT INTO line_items(product_id, cart_id, quantity) VALUES(?, ?, ?);";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
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

    @Override
    public void remove(LineItem lineItem) {
        String sql = "DELETE FROM line_items WHERE line_items_id = ?;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, lineItem.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM line_items;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(LineItem lineItem, int quantity) {
        String sql = "UPDATE line_items SET quantity = ? WHERE line_items_id = ?;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(quantity, lineItem.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LineItem find(int id) {
        String SqlQuery = "SELECT l.product_id, l.cart_id, l.quantity, " +
                "p.\"name\", p.price, p.currency, p.description, " +
                "pc.product_category_id, pc.\"name\", pc.description, pc.department, " +
                "s.supplier_id, s.\"name\", s.description " +
                "FROM line_items as l " +
                "JOIN products as p ON p.product_id = l.product_id " +
                "JOIN product_categories as pc ON pc.product_category_id = p.product_category_id " +
                "JOIN suppliers as s ON s.supplier_id = p.supplier_id " +
                "WHERE line_items_id = ?;";
        LineItem lineItem = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int productId = rs.getInt(1);
                int cartId = rs.getInt(2);
                int quantity = rs.getInt(3);
                String productName = rs.getString(4);
                BigDecimal price = rs.getBigDecimal(5);
                String currency = rs.getString(6);
                String productDescription = rs.getString(7);
                ProductCategory pc = new ProductCategory(rs.getString(9), rs.getString(10), rs.getString(11));
                pc.setId(rs.getInt(8));
                Supplier supplier = new Supplier(rs.getString(13), rs.getString(14));
                supplier.setId(rs.getInt(12));
                Product product = new Product(productName, price, currency, productDescription, pc, supplier);
                product.setId(productId);
                lineItem = new LineItem(product, cartId, quantity);
                lineItem.setId(id);
            } else throw new DataNotFoundException("No such product");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lineItem;
    }

    @Override
    public List<LineItem> getBy(Cart cart) {
        String sql = "SELECT l.line_items_id, l.product_id, l.cart_id, l.quantity, " +
                "p.\"name\", p.price, p.currency, p.description, " +
                "pc.product_category_id, pc.\"name\", pc.description, pc.department, " +
                "s.supplier_id, s.\"name\", s.description " +
                "FROM line_items as l " +
                "JOIN products as p ON p.product_id = l.product_id " +
                "JOIN product_categories as pc ON pc.product_category_id = p.product_category_id " +
                "JOIN suppliers as s ON s.supplier_id = p.supplier_id " +
                "WHERE cart_id = ?;";
        List<LineItem> result = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, cart.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int lineItemId = rs.getInt(1);
                int productId = rs.getInt(2);
                int cartId = rs.getInt(3);
                int quantity = rs.getInt(4);
                String productName = rs.getString(5);
                BigDecimal price = rs.getBigDecimal(6);
                String currency = rs.getString(7);
                String productDescription = rs.getString(8);
                ProductCategory pc = new ProductCategory(rs.getString(10), rs.getString(11), rs.getString(12));
                pc.setId(rs.getInt(9));
                Supplier supplier = new Supplier(rs.getString(14), rs.getString(15));
                supplier.setId(rs.getInt(13));
                Product product = new Product(productName, price, currency, productDescription, pc, supplier);
                product.setId(productId);
                LineItem lineItem = new LineItem(product, cartId, quantity);
                lineItem.setId(lineItemId);
                result.add(lineItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
