package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.dao.implementation.file.CartDaoFile;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CartDaoTest {
//    private static final CartDao CART_DAO = DataManager.getCartDao();
    private static final CartDao CART_DAO = new CartDaoFile();

    @Test
    void testAdd() {
        Cart cart = new Cart("USD");
        CART_DAO.add(cart);
        assertNotEquals(0, cart.getId());
        CART_DAO.remove(cart.getId());
    }

    @Test
    void testFind_validId() {
        Cart cart = new Cart("USD");
        CART_DAO.add(cart);

        Cart result = CART_DAO.find(cart.getId());
        assertEquals(cart.getId(), result.getId());
        CART_DAO.remove(cart.getId());
    }

    @Test
    void testFind_invalidId() {
        assertThrows(DataNotFoundException.class, () -> CART_DAO.find(-1));
    }

    @Test
    void testRemove() {
        Cart cart = new Cart("USD");
        CART_DAO.add(cart);
        assertNotNull(CART_DAO.find(cart.getId()));

        CART_DAO.remove(cart.getId());
        assertThrows(DataNotFoundException.class, () -> CART_DAO.find(cart.getId()));
    }
}
