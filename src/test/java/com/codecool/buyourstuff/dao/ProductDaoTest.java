package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.dao.implementation.database.ProductCategoryDaoDb;
import com.codecool.buyourstuff.dao.implementation.database.ProductDaoDb;
import com.codecool.buyourstuff.dao.implementation.database.SupplierDaoDb;
import com.codecool.buyourstuff.dao.implementation.file.ProductCategoryDaoFile;
import com.codecool.buyourstuff.dao.implementation.file.ProductDaoFile;
import com.codecool.buyourstuff.dao.implementation.mem.SupplierDaoMem;
import com.codecool.buyourstuff.model.Product;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.Supplier;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoTest {
//    private static final ProductDao PRODUCT_DAO = DataManager.getProductDao();
//    private static final ProductCategoryDao PRODUCT_CATEGORY_DAO = DataManager.getProductCategoryDao();
//    private static final SupplierDao SUPPLIER_DAO = DataManager.getSupplierDao();

//    private static final ProductDao PRODUCT_DAO = new ProductDaoDb();
//    private static final ProductCategoryDao PRODUCT_CATEGORY_DAO = new ProductCategoryDaoDb();
//    private static final SupplierDao SUPPLIER_DAO = new SupplierDaoDb();

    private static final ProductDao PRODUCT_DAO = new ProductDaoFile();
    private static final ProductCategoryDao PRODUCT_CATEGORY_DAO = new ProductCategoryDaoFile();
    private static final SupplierDao SUPPLIER_DAO = new SupplierDaoMem();

    private static Supplier testSupplier = new Supplier("test", "test");
    private static ProductCategory testProductCategory = new ProductCategory("test", "test", "test");

    @BeforeEach
    void setup() {
        SUPPLIER_DAO.add(testSupplier);
        PRODUCT_CATEGORY_DAO.add(testProductCategory);
    }

    @AfterEach
    void breakdown() {
        SUPPLIER_DAO.remove(testSupplier.getId());
        PRODUCT_CATEGORY_DAO.remove(testProductCategory.getId());
    }


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
