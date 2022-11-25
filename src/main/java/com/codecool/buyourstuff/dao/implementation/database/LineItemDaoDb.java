package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.LineItemDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.LineItem;

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
    public LineItem find(int id) {
        return null;
    }

    @Override
    public List<LineItem> getBy(Cart cart) {
        return null;
    }
}
