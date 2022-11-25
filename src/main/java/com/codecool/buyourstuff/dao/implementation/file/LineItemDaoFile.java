package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.dao.DataManager;
import com.codecool.buyourstuff.dao.LineItemDao;
import com.codecool.buyourstuff.model.*;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LineItemDaoFile implements LineItemDao {
    private final File LINEITEM_FILE = new File("data/lineitems.csv");
    private int nextLineItemId;
    private static final String DATA_SEPARATOR = ";";
    private static int highestID;

    public LineItemDaoFile() {
        if (LINEITEM_FILE.exists()) {
            try (Scanner scanner = new Scanner(LINEITEM_FILE)) {
                scanner.useDelimiter(",");
                while (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
                scanner.findInLine("id=");
                int lastUserId = scanner.nextInt();
                nextLineItemId = lastUserId + 1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else nextLineItemId = 1;
    }

    @Override
    public void add(LineItem lineItem) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LINEITEM_FILE, true))) {
            int id = lineItem.getId();
            if (id == 0) {
                id = ++highestID;
                lineItem.setId(id);
            } else if (id > highestID) {
                highestID = id;
            }
            String line = id + DATA_SEPARATOR + lineItem.getProduct() + DATA_SEPARATOR + lineItem.getCartId() + DATA_SEPARATOR + lineItem.getQuantity();
            writer.append(line).append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(LineItem lineItem) {
    }

    @Override
    public void clear() {
        try {
            new FileWriter(LINEITEM_FILE, false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        nextLineItemId = 1;
    }

    @Override
    public void update(LineItem lineItem, int quantity) {

    }

    @Override
    public LineItem find(int id) {
        try (Scanner scanner = new Scanner(LINEITEM_FILE)) {
            while (scanner.hasNextLine()) {
                LineItem lineItem = new LineItem(
                        String.valueOf(scanner.nextInt()),
                        scanner.nextInt(),
                        scanner.nextInt());
                if (id == lineItem.getId())
                    return lineItem;
            }
            throw new DataNotFoundException("No such user. Name or password may be incorrect.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LineItem> getBy(Cart cart) {
        return null;
    }
}
