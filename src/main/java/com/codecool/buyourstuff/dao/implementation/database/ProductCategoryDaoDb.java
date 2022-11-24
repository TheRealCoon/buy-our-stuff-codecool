package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.dao.ProductCategoryDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.sql.*;
import java.util.List;

import static com.codecool.buyourstuff.dao.implementation.database.DbLogin.*;

public class ProductCategoryDaoDb implements ProductCategoryDao {
    @Override
    public void add(ProductCategory category) {
        String SqlQuery = "INSERT INTO product_categories(\"name\", description, department) VALUES(?, ?, ?);";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setString(3, category.getDepartment());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductCategory find(int id) {
        String SqlQuery = "SELECT \"name\", description, department FROM product_categories WHERE id = ?;";
        ProductCategory pc = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            String name = rs.getString(1);
            String description = rs.getString(2);
            String department = rs.getString(3);
            if (name != null) {
                pc = new ProductCategory(name, description, department);
                pc.setId(id);
            } else throw new DataNotFoundException("No such product category");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pc;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void clear() {

    }

    @Override
    public List<ProductCategory> getAll() {
        return null;
    }
}
