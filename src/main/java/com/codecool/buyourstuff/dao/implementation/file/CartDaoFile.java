package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartDaoFile implements CartDao {
    private static final String CART_FILE = "data/cart.csv";
    private static final String DATA_SEPARATOR = ";";
    private static int highestID;

    @Override
    public void add(Cart cart) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE, true))) {
            int id = cart.getId();
            if (id == 0) {
                id = ++highestID;
                cart.setId(id);
            } else if (id > highestID) {
                highestID = id;
            }
            String line = id + DATA_SEPARATOR + cart.getCurrency();
            writer.append(line).append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cart find(int id) {
        Cart cart = null;
        String line;
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
            while ((line = reader.readLine()) != null && cart == null) {
                lineCounter++;
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
            System.out.println("Invalid ID in file at line: " + lineCounter);
        }
        if (cart == null) {
            throw new DataNotFoundException("Cart with id = " + id + " not found!");
        }
        return cart;
    }

    @Override
    public void remove(int id) {
        List<Cart> carts = getAll();
        carts.removeIf(c -> c.getId() == id);
        overWriteListOfCartsInFile(carts);
    }

    @Override
    public void clear() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE, false))) {
            writer.append("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cart> getAll() {
        List<Cart> carts = new ArrayList<>();
        String line;
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
            while ((line = reader.readLine()) != null) {
                lineCounter++;
                String[] values = line.split(DATA_SEPARATOR);
                int resultId = Integer.parseInt(values[0]);
                String resultCurrency = values[1];
                Cart cart = new Cart(resultCurrency);
                cart.setId(resultId);
                carts.add(cart);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (
                NumberFormatException e) {
            System.out.println("Invalid ID in file at line: " + lineCounter);
        }
        return carts;
    }

    private void overWriteListOfCartsInFile(List<Cart> carts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE, false))) {
            for (Cart cart : carts) {
                String line = cart.getId() + DATA_SEPARATOR + cart.getCurrency();
                writer.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
