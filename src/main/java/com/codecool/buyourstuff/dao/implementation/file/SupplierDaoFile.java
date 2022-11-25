package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.SupplierDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.Supplier;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoFile implements SupplierDao {
    private static final String SUPPLIER_FILE = "data/supplier.csv";
    private static final String DATA_SEPARATOR = ";";
    private static int highestID;

    @Override
    public void add(Supplier supplier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SUPPLIER_FILE, true))) {
            int id = supplier.getId();
            if (id == 0) {
                id = ++highestID;
                supplier.setId(id);
            } else if (id > highestID) {
                highestID = id;
            }
            String line = id + DATA_SEPARATOR + supplier.getName() + DATA_SEPARATOR + supplier.getDescription();
            writer.append(line).append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Supplier find(int id) {
        Supplier supplier = null;
        String line;
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            while ((line = reader.readLine()) != null && supplier == null) {
                lineCounter++;
                String[] values = line.split(DATA_SEPARATOR);
                int resultId = Integer.parseInt(values[0]);
                String name = values[1];
                String description = values[2];
                if (resultId == id) {
                    supplier = new Supplier(name, description);
                    supplier.setId(resultId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID in file at line: " + lineCounter);
        }
        if (supplier == null) {
            throw new DataNotFoundException("Line item with id = " + id + " not found!");
        }
        return supplier;
    }

    @Override
    public void remove(int id) {
        List<Supplier> suppliers = getAll();
        suppliers.removeIf(c -> c.getId() == id);
        overWriteListOfSuppliersInFile(suppliers);
    }

    @Override
    public void clear() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SUPPLIER_FILE, false))) {
            writer.append("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Supplier> getAll() {
        List<Supplier> suppliers = new ArrayList<>();
        String line;
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            while ((line = reader.readLine()) != null) {
                lineCounter++;
                String[] values = line.split(DATA_SEPARATOR);
                int resultId = Integer.parseInt(values[0]);
                String name = values[1];
                String description = values[2];
                Supplier supplier = new Supplier(name, description);
                supplier.setId(resultId);
                suppliers.add(supplier);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (
                NumberFormatException e) {
            System.out.println("Invalid ID in file at line: " + lineCounter);
        }
        return suppliers;
    }

    private void overWriteListOfSuppliersInFile(List<Supplier> suppliers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SUPPLIER_FILE, false))) {
            for (Supplier supplier : suppliers) {
                String line = supplier.getId() + DATA_SEPARATOR + supplier.getName() + DATA_SEPARATOR + supplier.getDescription();
                writer.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
