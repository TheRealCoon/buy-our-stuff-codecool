package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.LineItemDao;
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
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, lineItem.getProduct().getId());
            ps.setInt(2, lineItem.getCartId());
            ps.setInt(3, lineItem.getQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(LineItem lineItem) {
        String sql = "DELETE FROM line_items WHERE id = ?;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, lineItem.getId());
            if (!preparedStatement.execute()) throw new DataNotFoundException("No line item found!");
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
        String sql = "UPDATE line_items SET quantity ";

    }

    @Override
    public LineItem find(int id) {
        String SqlQuery = "SELECT product_id, cart_id, quantity FROM line_items WHERE id = ?;";
        LineItem lineItem = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            int product = rs.getInt(1);
            int cartId = rs.getInt(2);
            int quantity = rs.getInt(3);
            if (product != null) {
                lineItem = new LineItem(productId, cartId, quantity);
                lineItem.setId(id);
            } else throw new DataNotFoundException("No such line item");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lineItem;
    }

    @Override
    public List<LineItem> getBy(Cart cart) {
        return null;
    }
}
