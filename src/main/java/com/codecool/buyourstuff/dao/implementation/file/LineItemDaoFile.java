package com.codecool.buyourstuff.dao.implementation.file;

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
    private static final File LINE_ITEM_FILE = new File("data/lineitems.csv");
    private static final String DATA_SEPARATOR = ";";
    private static int highestId;

    public LineItemDaoFile() {
        highestId = findHighestId();
    }

    private static int findHighestId() {
        int result = 0;
        try (Scanner scanner = new Scanner(LINE_ITEM_FILE)) {
            scanner.useDelimiter(DATA_SEPARATOR);
            while (scanner.hasNextLine()) {
                result = scanner.nextInt();
                scanner.nextLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void add(LineItem lineItem) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LINE_ITEM_FILE, true))) {
            int id = lineItem.getId();
            if (id <=highestId) {
                id = ++highestId;
                lineItem.setId(id);
            } else {
                highestId = id;
            }
            String line = id + DATA_SEPARATOR +
                    lineItem.getProduct().getId() + DATA_SEPARATOR +
                    lineItem.getCartId() + DATA_SEPARATOR +
                    lineItem.getQuantity();
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
            new FileWriter(LINE_ITEM_FILE, false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(LineItem lineItem, int quantity) {

    }

    @Override
    public LineItem find(int id) {
        try (Scanner scanner = new Scanner(LINE_ITEM_FILE)) {
            scanner.useDelimiter(DATA_SEPARATOR);
            while (scanner.hasNextLine()) {
                LineItem lineItem = new LineItem(
                        new ProductDaoFile().find(scanner.nextInt()),
                        scanner.nextInt(),
                        scanner.nextInt());
                if (id == lineItem.getId())
                    return lineItem;
            }
            throw new DataNotFoundException("No such line item.");
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
