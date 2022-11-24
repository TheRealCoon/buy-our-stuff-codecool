package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.ProductDao;
import com.codecool.buyourstuff.model.Product;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.Supplier;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import static com.codecool.buyourstuff.dao.implementation.database.DbLogin.*;

public class ProductDaoDb implements ProductDao {
    @Override
    public void add(Product product) {
        String SqlQuery = "INSERT INTO products(\"name\", price, currency, description, product_category_id, supplier_id) VALUES(?, ?, ?, ?, ?, ?);";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setBigDecimal(2, product.getDefaultPrice());
            ps.setString(3, product.getDefaultCurrency().toString());
            ps.setString(4, product.getProductCategory().getDescription());;
            ps.setInt(5, product.getProductCategory().getId());
            ps.setInt(6, product.getSupplier().getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            product.setId(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void clear() {

    }

    @Override
    public List<Product> getAll() {
        return null;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return null;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return null;
    }
}
