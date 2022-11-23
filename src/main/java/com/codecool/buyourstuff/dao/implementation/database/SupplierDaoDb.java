package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.SupplierDao;
import com.codecool.buyourstuff.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoDb implements SupplierDao {

    @Override
    public void add(Supplier supplier) {
        final String SQL = "INSERT INTO suppliers (name, description) VALUES(?, ?);";

        try(Connection con = DriverManager.getConnection(DbLogin.URL, DbLogin.USER, DbLogin.PASSWORD)) {
            PreparedStatement st = con.prepareStatement(SQL);

            st.setString(1, supplier.getName());
            st.setString(2, supplier.getDescription());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Supplier find(int id) {
        final String SQL = "SELECT name, description FROM suppliers WHERE supplier_id = ?;";

        try(Connection con = DriverManager.getConnection(DbLogin.URL, DbLogin.USER, DbLogin.PASSWORD)) {
            PreparedStatement st = con.prepareStatement(SQL);

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String name = rs.getString(1);
                String description = rs.getString(2);

                Supplier supplier = new Supplier(name, description);
                supplier.setId(id);
                return supplier;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(int id) {
        final String SQL = "DELETE FROM suppliers WHERE supplier_id = ?;";

        try(Connection con = DriverManager.getConnection(DbLogin.URL, DbLogin.USER, DbLogin.PASSWORD)) {
            PreparedStatement st = con.prepareStatement(SQL);

            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        final String SQL = "TRUNCATE suppliers;";
        try(Connection con = DriverManager.getConnection(DbLogin.URL, DbLogin.USER, DbLogin.PASSWORD)) {
            PreparedStatement st = con.prepareStatement(SQL);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Supplier> getAll() {
        final String SQL = "SELECT supplier_id, name, description FROM suppliers;";
        try(Connection con = DriverManager.getConnection(DbLogin.URL, DbLogin.USER, DbLogin.PASSWORD)) {
            PreparedStatement st = con.prepareStatement(SQL);
            ResultSet rs = st.executeQuery();

            List<Supplier> suppliers = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);

                Supplier supplier = new Supplier(name, description);
                supplier.setId(id);
                suppliers.add(supplier);
            }

            return suppliers;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
