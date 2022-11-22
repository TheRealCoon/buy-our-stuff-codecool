package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.model.Cart;


public interface CartDao {
    void add(Cart cart);
    Cart find(int id);
    void remove(int id);
    void clear();
}
