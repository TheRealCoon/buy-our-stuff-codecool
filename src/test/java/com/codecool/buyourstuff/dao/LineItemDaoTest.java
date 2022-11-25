package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.dao.implementation.database.*;
import com.codecool.buyourstuff.dao.implementation.file.LineItemDaoFile;
import com.codecool.buyourstuff.dao.implementation.file.ProductCategoryDaoFile;
import com.codecool.buyourstuff.dao.implementation.file.ProductDaoFile;
import com.codecool.buyourstuff.dao.implementation.file.SupplierDaoFile;
import com.codecool.buyourstuff.model.*;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class LineItemDaoTest {
    //    private static final LineItemDao LINE_ITEM_DAO = DataManager.getLineItemDao();
//    private static final SupplierDao SUPPLIER_DAO = DataManager.getSupplierDao();
//    private static final ProductCategoryDao PRODUCT_CATEGORY_DAO = DataManager.getProductCategoryDao();
//    private static final ProductDao PRODUCT_DAO = DataManager.getProductDao();
//
//    private static final LineItemDao LINE_ITEM_DAO = new LineItemDaoFile();
//    private static final SupplierDao SUPPLIER_DAO = new SupplierDaoFile();
//    private static final ProductCategoryDao PRODUCT_CATEGORY_DAO = new ProductCategoryDaoFile();
//    private static final ProductDao PRODUCT_DAO = new ProductDaoFile();
    private static final SupplierDao SUPPLIER_DAO = new SupplierDaoDb();
    private static final ProductCategoryDao PRODUCT_CATEGORY_DAO = new ProductCategoryDaoDb();
    private static final ProductDao PRODUCT_DAO = new ProductDaoDb();
    private static final LineItemDao LINE_ITEM_DAO = new LineItemDaoDb();
    private static final CartDao CART_DAO = new CartDaoDb();

    private static Supplier testSupplier = new Supplier("test", "test");
    private static ProductCategory testProductCategory = new ProductCategory("test", "test", "test");
    private static Product testProduct = new Product("Test", new BigDecimal(12),
            "USD", "test", testProductCategory, testSupplier);
    private static Cart testCart = new Cart();

    @BeforeEach
    void setup() {
        SUPPLIER_DAO.add(testSupplier);
        PRODUCT_CATEGORY_DAO.add(testProductCategory);
        PRODUCT_DAO.add(testProduct);
        CART_DAO.add(testCart);

    }

    @AfterEach
    void breakdown() {
        SUPPLIER_DAO.remove(testSupplier.getId());
        PRODUCT_CATEGORY_DAO.remove(testProductCategory.getId());
        PRODUCT_DAO.remove(testProduct.getId());
        CART_DAO.remove(testCart.getId());
    }

    @Test
    void testAdd() {
        LineItem lineItem = new LineItem(testProduct, testCart.getId(), 1);
        LINE_ITEM_DAO.add(lineItem);
        assertNotEquals(0, lineItem.getId());
        LINE_ITEM_DAO.remove(lineItem);
    }

    @Test
    void testFind_validId() {
        LineItem lineItem = new LineItem(testProduct, testCart.getId(), 1);
        LINE_ITEM_DAO.add(lineItem);

        LineItem result = LINE_ITEM_DAO.find(lineItem.getId());
        assertEquals(lineItem.getId(), result.getId());
        LINE_ITEM_DAO.remove(lineItem);
    }

    @Test
    void testFind_invalidId() {
        assertThrows(DataNotFoundException.class, () -> LINE_ITEM_DAO.find(-1));
    }

    @Test
    void testRemove() {
        LineItem lineItem = new LineItem(testProduct, testCart.getId(), 1);
        LINE_ITEM_DAO.add(lineItem);
        assertNotNull(LINE_ITEM_DAO.find(lineItem.getId()));

        LINE_ITEM_DAO.remove(lineItem);
        assertThrows(DataNotFoundException.class, () -> LINE_ITEM_DAO.find(lineItem.getId()));
    }
}
