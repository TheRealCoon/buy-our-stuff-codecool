package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.ProductCategoryDao;
import com.codecool.buyourstuff.model.ProductCategory;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ProductCategoryDaoFile implements ProductCategoryDao {

    private final File PRODUCT_CATEGORY_FILE = new File("data/productCategory.csv");
    private static final String DATA_SEPARATOR = ";";
    private int highestId;


    @Override
    public void add(ProductCategory category) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_CATEGORY_FILE, true))) {
            int id = category.getId();
            if (id <= highestId) {
                id = ++highestId;
                category.setId(id);
            } else {
                highestId = id;
            }
            String line = id + DATA_SEPARATOR +
                    category.getName() + DATA_SEPARATOR +
                    category.getDescription() + DATA_SEPARATOR +
                    category.getDepartment();
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
                if (resultId == id) {
                    String name = values[1];
                    String description = values[2];
                    String department = values[3];
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
        overWriteListOfProductCategoriesInFile(productCategories);
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

    }


    private void overWriteListOfProductCategoriesInFile(List<ProductCategory> productCategories) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_CATEGORY_FILE, false))) {
            for (ProductCategory productCategory : productCategories) {
                String line = productCategory.getId() + DATA_SEPARATOR +
                        productCategory.getName() + DATA_SEPARATOR +
                        productCategory.getDescription() + DATA_SEPARATOR +
                        productCategory.getDepartment();
                writer.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}