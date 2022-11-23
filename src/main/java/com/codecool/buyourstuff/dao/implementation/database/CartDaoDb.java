package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.codecool.buyourstuff.dao.implementation.database.DbLogin.*;

public class CartDaoDb implements CartDao {

    @Override
    public void add(Cart cart) {
        String SqlQuery = "INSERT INTO cart(currency) VALUES(?);";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setString(1, cart.getCurrency().toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cart find(int id) {
        String SqlQuery = "SELECT currency FROM cart WHERE id = ?;";
        Cart cart = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setInt(1, id);
            String currency = ps.executeQuery().getString(1);
            if (currency != null && !currency.isEmpty()) {
                cart = new Cart(currency);
                cart.setId(id);
            } else throw new DataNotFoundException("No such cart");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
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
