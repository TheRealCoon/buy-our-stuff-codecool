package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.model.LineItem;
import com.codecool.buyourstuff.model.Product;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.Supplier;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class LineItemDaoTest {
    private static final LineItemDao LINE_ITEM_DAO = DataManager.getLineItemDao();
    private static Supplier testSupplier = new Supplier("test", "test");
    private static ProductCategory testProductCategory = new ProductCategory("test", "test", "test");
    private static Product testProduct = new Product("Test", new BigDecimal(12),
            "USD", "test", testProductCategory, testSupplier);

    @Test
    void testAdd() {
        LineItem lineItem = new LineItem(testProduct, 1, 1);
        LINE_ITEM_DAO.add(lineItem);
        assertNotEquals(0, lineItem.getId());
        LINE_ITEM_DAO.remove(lineItem);
    }

    @Test
    void testFind_validId() {
        LineItem lineItem = new LineItem(testProduct, 1, 1);
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
        LineItem lineItem = new LineItem(testProduct, 1, 1);
        LINE_ITEM_DAO.add(lineItem);
        assertNotNull(LINE_ITEM_DAO.find(lineItem.getId()));

        LINE_ITEM_DAO.remove(lineItem);
        assertThrows(DataNotFoundException.class, () -> LINE_ITEM_DAO.find(lineItem.getId()));
    }
}
