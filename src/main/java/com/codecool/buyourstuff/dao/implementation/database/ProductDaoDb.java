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
import java.util.ArrayList;
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
        String SqlQuery = "SELECT p.\"name\", p.price, p.currency, p.description, " +
                "pc.product_category_id, pc.\"name\", pc.description, pc.department, " +
                "s.\"name\", s.description " +
                "FROM products as p " +
                "JOIN product_categories as pc ON pc.product_category_id = p.product_category_id " +
                "JOIN suppliers as s ON s.supplier_id = p.supplier_id " +
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
        List<Product> allProducts = new ArrayList<>();
        String SqlQuery = "SELECT p.product_id, p.\"name\", p.price, p.currency, p.description, " +
                "pc.product_category_id, pc.\"name\", pc.description, pc.department, " +
                "s.supplier_id, s.\"name\", s.description " +
                "FROM products as p " +
                "JOIN product_categories as pc ON pc.product_category_id = p.product_category_id " +
                "JOIN suppliers as s ON s.supplier_id = p.supplier_id;";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString(2);
                BigDecimal price = rs.getBigDecimal(3);
                String currency = rs.getString(4);
                String description = rs.getString(5);
                ProductCategory pc = new ProductCategory(rs.getString(7), rs.getString(8), rs.getString(9));
                pc.setId(rs.getInt(6));
                Supplier supplier = new Supplier(rs.getString(11), rs.getString(12));
                supplier.setId(rs.getInt(10));
                Product product = new Product(name, price, currency, description, pc, supplier);
                product.setId(rs.getInt(1));
                allProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allProducts;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        List<Product> products = new ArrayList<>();
        String SqlQuery = "SELECT p.product_id, p.\"name\", p.price, p.currency, p.description, " +
                "pc.product_category_id, pc.\"name\", pc.description, pc.department, " +
                "s.supplier_id, s.\"name\", s.description " +
                "FROM products as p " +
                "JOIN product_categories as pc ON pc.product_category_id = p.product_category_id " +
                "JOIN suppliers as s ON s.supplier_id = p.supplier_id " +
                "WHERE p.supplier_id = ? OR (s.\"name\" = ? AND s.description = ?);";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setInt(1, supplier.getId());
            ps.setString(2, supplier.getName());
            ps.setString(3, supplier.getDescription());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString(2);
                BigDecimal price = rs.getBigDecimal(3);
                String currency = rs.getString(4);
                String description = rs.getString(5);
                ProductCategory pc = new ProductCategory(rs.getString(7), rs.getString(8), rs.getString(9));
                pc.setId(rs.getInt(6));
                supplier.setId(10);
                Product product = new Product(name, price, currency, description, pc, supplier);
                product.setId(rs.getInt(1));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        List<Product> products = new ArrayList<>();
        String SqlQuery = "SELECT p.product_id, p.\"name\", p.price, p.currency, p.description, " +
                "pc.product_category_id, pc.\"name\", pc.description, pc.department, " +
                "s.supplier_id, s.\"name\", s.description " +
                "FROM products as p " +
                "JOIN product_categories as pc ON pc.product_category_id = p.product_category_id " +
                "JOIN suppliers as s ON s.supplier_id = p.supplier_id " +
                "WHERE pc.product_category_id = ? OR (pc.\"name\" = ? AND pc.description = ? AND pc.department = ?);";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery);
            ps.setInt(1, productCategory.getId());
            ps.setString(2, productCategory.getName());
            ps.setString(3, productCategory.getDescription());
            ps.setString(4, productCategory.getDepartment());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString(2);
                BigDecimal price = rs.getBigDecimal(3);
                String currency = rs.getString(4);
                String description = rs.getString(5);
                productCategory.setId(rs.getInt(6));
                Supplier supplier = new Supplier(rs.getString(11), rs.getString(12));
                supplier.setId(10);
                Product product = new Product(name, price, currency, description, productCategory, supplier);
                product.setId(rs.getInt(1));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
