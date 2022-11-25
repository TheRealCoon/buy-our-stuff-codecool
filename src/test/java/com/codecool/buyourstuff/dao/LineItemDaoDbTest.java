package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.dao.implementation.database.*;
import com.codecool.buyourstuff.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineItemDaoDbTest {
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
    void getBy_validCart_returnsValid(){
        LineItem lineItem = new LineItem(testProduct, testCart.getId(), 1);
        LINE_ITEM_DAO.add(lineItem);

        List<LineItem> result = LINE_ITEM_DAO.getBy(testCart);
        assertEquals(lineItem.getId(), result.get(0).getId());
        LINE_ITEM_DAO.remove(lineItem);
    }
}
