package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.codecool.buyourstuff.dao.implementation.database.DbLogin.*;

public class CartDaoDb implements CartDao {

    @Override
    public void add(Cart cart) {
        String SqlQuery = "INSERT INTO carts(currency) VALUES(?);";
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
        String SqlQuery = "SELECT currency FROM carts WHERE id = ?;";
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
        String SqlQuery = "DELETE FROM carts WHERE id = ?;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setInt(1, id);
            if (!ps.execute()) throw new DataNotFoundException("No such cart");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        String SqlQuery = "DELETE FROM carts;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cart> getAll() {
        List<Cart> allCarts = new ArrayList<>();
        String SqlQuery = "SELECT * FROM carts;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Cart cart = new Cart(rs.getString(2));
                cart.setId(rs.getInt(1));
                allCarts.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCarts;
    }
}
