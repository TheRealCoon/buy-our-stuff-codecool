package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.model.Cart;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class CartDaoFile implements CartDao {
    private static final String CART_FILE = "data/cart.csv";
    private static final char DATA_SEPARATOR = ';';

    @Override
    public void add(Cart cart) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE, true))) {
            String line = String.valueOf(cart.getId()) + DATA_SEPARATOR + cart.getCurrency();
            writer.append(line);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Cart find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void clear() {

    }

    @Override
    public List<Cart> getAll() {
        return null;
    }
}
