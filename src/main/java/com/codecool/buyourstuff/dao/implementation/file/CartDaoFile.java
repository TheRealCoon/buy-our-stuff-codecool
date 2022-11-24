package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CartDaoFile implements CartDao {
    private static final String CART_FILE = "data/cart.csv";
    private static final String DATA_SEPARATOR = ";";

    @Override
    public void add(Cart cart) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE, true))) {
            String line = cart.getId() + DATA_SEPARATOR + cart.getCurrency();
            writer.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cart find(int id) {
        Cart cart = null;
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
            while ((line = reader.readLine()) != null && cart == null) {
                String[] values = line.split(DATA_SEPARATOR);
                int resultId = Integer.parseInt(values[0]);
                String resultCurrency = values[1];
                if (resultId == id) {
                    cart = new Cart(resultCurrency);
                    cart.setId(resultId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID in file!");
        }
        if (cart == null) {
            throw new DataNotFoundException("Cart with id = " + id + "not found!");
        }
        return cart;
    }

    @Override
    public void remove(int id) {
        Cart cart = find(id);

    }

    @Override
    public void clear() {

    }

    @Override
    public List<Cart> getAll() {
        return null;
    }
}
