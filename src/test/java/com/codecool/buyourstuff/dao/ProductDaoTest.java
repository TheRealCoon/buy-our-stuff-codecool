package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.model.Product;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.Supplier;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoTest {
    private static final ProductDao PRODUCT_DAO = DataManager.getProductDao();
    private static Supplier testSupplier = new Supplier("test", "test");
    private static ProductCategory testProductCategory = new ProductCategory("test", "test", "test");


    @Test
    void testAdd() {
        Product product = new Product("Test", new BigDecimal(12),
                "USD", "test", testProductCategory, testSupplier);
        PRODUCT_DAO.add(product);
        assertNotEquals(0, product.getId());
        PRODUCT_DAO.remove(product.getId());
    }

    @Test
    void testFind_validId() {
        Product product = new Product("Test", new BigDecimal(12),
                "USD", "test", testProductCategory, testSupplier);
        PRODUCT_DAO.add(product);

        Product result = PRODUCT_DAO.find(product.getId());
        assertEquals(product.getId(), result.getId());
        PRODUCT_DAO.remove(product.getId());
    }

    @Test
    void testFind_invalidId() {
        assertThrows(DataNotFoundException.class, () -> PRODUCT_DAO.find(-1));
    }

    @Test
    void testRemove() {
        Product product = new Product("Test", new BigDecimal(12),
                "USD", "test", testProductCategory, testSupplier);
        PRODUCT_DAO.add(product);
        assertNotNull(PRODUCT_DAO.find(product.getId()));

        PRODUCT_DAO.remove(product.getId());
        assertThrows(DataNotFoundException.class, () -> PRODUCT_DAO.find(product.getId()));
    }
}
