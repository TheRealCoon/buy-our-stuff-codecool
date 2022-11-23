package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.model.Cart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CartDaoDb implements CartDao {

    @Override
    public void add(Cart cart) {
        String SqlQuery = "INSERT INTO cart(currency) VALUES(?);";
        try (Connection connection = DriverManager.getConnection(LOCALHOST_5432 + DEFAULT_DBNAME, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setString(1, cart.getCurrency().toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cart find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void clear() {

    }

    @Override
    public List<Cart> getAll() {
        return null;
    }
}
