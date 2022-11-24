package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.ProductCategoryDao;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ProductCategoryDaoFile implements ProductCategoryDao {

    private final File PRODUCT_CATEGORY_FILE = new File("data/product_category.csv");
    private List<ProductCategory> productCategories = new ArrayList<>();
    private static final String DATA_SEPARATOR = ";";
    private int highestID;


    @Override
    public void add(ProductCategory category) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_CATEGORY_FILE, true))) {
            int id = category.getId();
            if (id == 0) {
                id = ++highestID;
                category.setId(id);
            } else if (id > highestID) {
                highestID = id;
            }
            String line = id + DATA_SEPARATOR + category.getDescription() + DATA_SEPARATOR + category.getDepartment();
            writer.append(line).append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductCategory find(int id) {

        ProductCategory productCategory = null;
        String line;
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_CATEGORY_FILE))) {
            while ((line = reader.readLine()) != null && productCategory == null) {
                lineCounter++;
                String[] values = line.split(DATA_SEPARATOR);
                int resultId = Integer.parseInt(values[0]);
                String name = values[1];
                String description = values[2];
                String department = values[3];
                if (resultId == id) {
                    productCategory = new ProductCategory(name, description, department);
                    productCategory.setId(resultId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID in file at line: " + lineCounter);
        }
        if (productCategory == null) {
            throw new DataNotFoundException("Cart with id = " + id + " not found!");
        }
        return productCategory;
    }


    @Override
    public void remove(int id) {
        List<ProductCategory> productCategories = getAll();
        productCategories.removeIf(p -> p.getId() == id);
        overWriteListOfCartsInFile(productCategories);
    }

    @Override
    public void clear() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_CATEGORY_FILE, false))) {
                writer.append("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    @Override
    public List<ProductCategory> getAll() {
        return new ArrayList<>(productCategories);
    }


    private void overWriteListOfCartsInFile(List<ProductCategory> productCategories) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_CATEGORY_FILE, false))) {
            for (ProductCategory productCategory : productCategories) {
                String line = productCategory.getId() + DATA_SEPARATOR + productCategory.getName()
                        + DATA_SEPARATOR + productCategory.getDescription() + DATA_SEPARATOR + productCategory.getDepartment();
                writer.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}