package com.codecool.buyourstuff.dao.implementation.database;

import com.codecool.buyourstuff.dao.DaoImplementationSupplier;
import com.codecool.buyourstuff.dao.ProductDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.Product;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.Supplier;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Currency;
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
            ps.setString(4, product.getProductCategory().getDescription());
            ;
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
        String SqlQuery = "SELECT \"name\", price, currency, description, " +
                "pc.product_category_id, pc.\"name\", pc.description, pc.department, " +
                "s.\"name\", s.description " +
                "FROM carts as c " +
                "JOIN product_categories as pc ON pc.product_category_id = c.product_category_id " +
                "JOIN suppliers as s ON s.supplier_id = c.supplier_id " +
                "WHERE product_id = ?;";
        Product product = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString(1);
                BigDecimal price = rs.getBigDecimal(2);
                String currency = rs.getString(3);
                String description = rs.getString(4);
                ProductCategory pc = new ProductCategory(rs.getString(6), rs.getString(7), rs.getString(8));
                pc.setId(rs.getInt(5));
                Supplier supplier = new Supplier(rs.getString(10), rs.getString(11));
                supplier.setId(rs.getInt(9));
                product = new Product(name, price, currency, description, pc, supplier);
                product.setId(id);
            } else throw new DataNotFoundException("No such product");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public void remove(int id) {
        String SqlQuery = "DELETE FROM products WHERE product_id = ?;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        String SqlQuery = "DELETE FROM products;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
