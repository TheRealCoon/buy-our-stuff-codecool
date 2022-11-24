package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.ProductDao;
import com.codecool.buyourstuff.model.*;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;


import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ProductDaoFile implements ProductDao {

    private final File PRODUCTS_FILE = new File("data/products.csv");
    private int nextId;

    private List<Product> product = new ArrayList<>();

    private static final String DATA_SEPARATOR = ";";


    @Override
    public void add(Product product) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE, true))) {
            String data = product.getId() + ", " + product.getDefaultPrice() + ", " + product.getDefaultCurrency()
                    + ", " + product.getDescription() + ", " + product.getProductCategory() + ", " + product.getSupplier();
            writer.append(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product find(int id) {
        Product product = null;
        String data;
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            while ((data = reader.readLine()) != null && product == null) {
                lineCounter++;
                String[] values = data.split(", ");
                int resultId = Integer.parseInt(values[0]);
                String name = values[1];
                BigDecimal defaultPrice = BigDecimal.valueOf(Long.parseLong(values[2]));
                String currencyString = values[3];
                String description = values[4];
                String productCategory = values[5];
                String supplier = values[6];

                if (resultId == id) {
                    product = new Product(name, defaultPrice, currencyString, description, productCategory, supplier);
                    product.setId(resultId);
                    product = new Product(name, defaultPrice, currencyString, description, productCategory, supplier);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID in file at line: " + lineCounter);
        }
        if (product == null) {
            throw new DataNotFoundException("Product with id = " + id + "not found!");
        }
        return product;
    }


    @Override
    public void remove(int id) {
        List<Product> product = getAll();
        product.removeIf(p -> p.getId() == id);
        overWriteListOfCartsInFile(product);
    }

    @Override
    public void clear() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE, false))) {
            writer.append("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAll() {
            List<Product> products = new ArrayList<>();
            String line;
            int lineCounter = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
                while ((line = reader.readLine()) != null) {
                    lineCounter++;
                    String[] values = line.split(DATA_SEPARATOR);
                    int resultId = Integer.parseInt(values[0]);
                    String name = values[1];
                    BigDecimal defaultPrice = BigDecimal.valueOf(Long.parseLong(values[2]));;
                    String currencyString = values[3];
                    String description = values[4];
                    String productCategory = values[5];
                    String supplier = values[6];
                    Product product = new Product(name, defaultPrice, currencyString, description, productCategory, supplier);
                    product.setId(resultId);
                    products.add(product);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (
                    NumberFormatException e) {
                System.out.println("Invalid ID in file at line: " + lineCounter);
            }
            return products;
        }

    @Override
    public List<Product> getBy(Supplier supplier) {
        List<Product> products = new ArrayList<>();
        Product product = new Product(supplier);
        String line;
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            while ((line = reader.readLine()) != null) {
                    lineCounter++;
                    String[] values = line.split(DATA_SEPARATOR);
                    int resultId = Integer.parseInt(values[0]);
                    String name = values[1];
                    BigDecimal defaultPrice = BigDecimal.valueOf(Long.parseLong(values[2]));
                    String currencyString = values[3];
                    String description = values[4];
                    String productCategory = values[5];
                    String supplier = values[6];
                if (supplier == String.valueOf(product.getSupplier())) {
                    Product product = new Product(name, defaultPrice, currencyString, description, productCategory, supplier);
                    product.setId(resultId);
                    products.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (
                NumberFormatException e) {
            System.out.println("Invalid supplier in file at line: " + lineCounter);
        }
        return products;
    }


    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        List<Product> products = new ArrayList<>();
        Product product = new Product(productCategory);
        String line;
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            while ((line = reader.readLine()) != null) {
                    lineCounter++;
                    String[] values = line.split(DATA_SEPARATOR);
                    int resultId = Integer.parseInt(values[0]);
                    String name = values[1];
                    BigDecimal defaultPrice = new BigDecimal(values[2]);
                    String currencyString = values[3];
                    String description = values[4];
                    ProductCategory productCategory = values[5];
                    String supplier = values[6];
                    if (productCategory == product.getProductCategory())) {
                    Product product = new Product(name, defaultPrice, currencyString, description, productCategory, supplier);
                    product.setId(resultId);
                    products.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (
                NumberFormatException e) {
            System.out.println("Invalid product category in file at line: " + lineCounter);
        }
        return products;
    }
    private void overWriteListOfCartsInFile(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE, false))) {
            for (Product product : products) {
                String line = product.getId() + DATA_SEPARATOR + product.getName()
                        + DATA_SEPARATOR + product.getDefaultPrice() + DATA_SEPARATOR + product.getDefaultCurrency()
                        + DATA_SEPARATOR + product.getDescription() + DATA_SEPARATOR + product.getProductCategory()
                        + DATA_SEPARATOR + product.getSupplier();
                writer.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}