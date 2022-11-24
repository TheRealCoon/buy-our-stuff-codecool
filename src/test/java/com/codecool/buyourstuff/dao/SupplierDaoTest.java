package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.model.Supplier;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierDaoTest {
    private static final SupplierDao SUPPLIER_DAO = DataManager.getSupplierDao();

    @Test
    void testAdd() {
        Supplier supplier = new Supplier("test", "test");
        SUPPLIER_DAO.add(supplier);
        assertNotEquals(0, supplier.getId());
        SUPPLIER_DAO.remove(supplier.getId());
    }

    @Test
    void testFind_validId() {
        Supplier supplier = new Supplier("test", "test");
        SUPPLIER_DAO.add(supplier);

        Supplier result = SUPPLIER_DAO.find(supplier.getId());
        assertEquals(supplier.getId(), result.getId());
        SUPPLIER_DAO.remove(supplier.getId());
    }

    @Test
    void testFind_invalidId() {
        assertThrows(DataNotFoundException.class, () -> SUPPLIER_DAO.find(-1));
    }

    @Test
    void testRemove() {
        Supplier supplier = new Supplier("test", "test");
        SUPPLIER_DAO.add(supplier);
        assertNotNull(SUPPLIER_DAO.find(supplier.getId()));

        SUPPLIER_DAO.remove(supplier.getId());
        assertThrows(DataNotFoundException.class, () -> SUPPLIER_DAO.find(supplier.getId()));
    }
}
