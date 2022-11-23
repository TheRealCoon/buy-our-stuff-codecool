package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.dao.DataManager;
import com.codecool.buyourstuff.dao.UserDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.User;
import com.codecool.buyourstuff.model.UserDTO;

import java.sql.*;

public class UserDaoDb implements UserDao {

    @Override
    public void add(User user) {
        CartDao cartDao = DataManager.getCartDao();
        Cart cart = new Cart();
        cartDao.add(cart);

        user.setCartId(cart.getId());

        final String SQL = "INSERT INTO users (name, password, cart_id) VALUES(?, ?, ?);";

        try(Connection con = DriverManager.getConnection(DbLogin.URL, DbLogin.USER, DbLogin.PASSWORD)) {
            PreparedStatement st = con.prepareStatement(SQL);

            st.setString(1, user.getName());
            st.setString(2, user.getPassword());
            st.setInt(3, user.getCartId());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User find(String userName, String password) {
        final String SQL = "SELECT user_id, name, password, cart_id FROM users WHERE name = ? AND password = ?;";

        try(Connection con = DriverManager.getConnection(DbLogin.URL, DbLogin.USER, DbLogin.PASSWORD)) {
            PreparedStatement st = con.prepareStatement(SQL);

            st.setString(1, userName);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String pw = rs.getString(3);
                int cart_id = rs.getInt(4);
                UserDTO userDTO = new UserDTO(id, name, pw, cart_id);
                return new User(userDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clear() {
        final String SQL = "TRUNCATE users;";
        try(Connection con = DriverManager.getConnection(DbLogin.URL, DbLogin.USER, DbLogin.PASSWORD)) {
            PreparedStatement st = con.prepareStatement(SQL);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isNameAvailable(String username) {
        final String SQL = "SELECT name FROM users WHERE name = ?;";
        try(Connection con = DriverManager.getConnection(DbLogin.URL, DbLogin.USER, DbLogin.PASSWORD)) {
            PreparedStatement st = con.prepareStatement(SQL);
            ResultSet rs = st.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
