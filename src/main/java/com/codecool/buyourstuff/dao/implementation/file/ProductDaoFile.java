package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.ProductDao;
import com.codecool.buyourstuff.dao.implementation.database.SupplierDaoDb;
import com.codecool.buyourstuff.model.*;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;


import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ProductDaoFile implements ProductDao {

    private final File PRODUCTS_FILE = new File("data/products.csv");

    private List<Product> product = new ArrayList<>();
    private static final String DATA_SEPARATOR = ", ";

    private int highestId;

    @Override
    public void add(Product product) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE, true))) {
            int id = product.getId();
            if (id == 0) {
                id = ++highestId;
                product.setId(id);
            } else if (id > highestId) {
                highestId = id;
            }
            String data =
                    product.getId() + DATA_SEPARATOR +
                            product.getName() + DATA_SEPARATOR +
                            product.getDefaultPrice() + DATA_SEPARATOR +
                            product.getDefaultCurrency().toString() + DATA_SEPARATOR +
                            product.getDescription() + DATA_SEPARATOR +
                            product.getProductCategory().getId() + DATA_SEPARATOR +
                            product.getSupplier().getId();
            writer.append(data).append(System.lineSeparator());
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
                String[] values = data.split(DATA_SEPARATOR);
                int resultId = Integer.parseInt(values[0]);
                if (resultId == id) {
                    String name = values[1];
                    BigDecimal defaultPrice = BigDecimal.valueOf(Double.parseDouble(values[2]));
                    String currencyString = values[3];
                    String description = values[4];
                    ProductCategory productCategory = new ProductCategoryDaoFile().find(Integer.parseInt(values[5]));
                    Supplier supplier = new SupplierDaoFile().find(values[6]);
                    product = new Product(name, defaultPrice, currencyString, description, productCategory, supplier);
                    product.setId(resultId);
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
        overWriteListOfProductsInFile(product);
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
                BigDecimal defaultPrice = BigDecimal.valueOf(Double.parseDouble(values[2]));
                String currencyString = values[3];
                String description = values[4];
                ProductCategory productCategory = new ProductCategoryDaoFile().find(Integer.parseInt(values[5]));
                Supplier supplier = new SupplierDaoFile().find(Integer.parseInt(values[6]));
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
        String line;
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            while ((line = reader.readLine()) != null) {
                lineCounter++;
                String[] values = line.split(DATA_SEPARATOR);
                int resultId = Integer.parseInt(values[0]);
                String name = values[1];
                BigDecimal defaultPrice = BigDecimal.valueOf(Double.parseDouble(values[2]));
                String currencyString = values[3];
                String description = values[4];
                ProductCategory productCategory = new ProductCategoryDaoFile().find(Integer.parseInt(values[5]));
                Supplier foundSupplier = new SupplierDaoFile().find(Integer.parseInt(values[6]));
                if (supplier.getId() == foundSupplier.getId() ||
                        ((supplier.getName().equals(foundSupplier.getName()) &&
                                supplier.getDescription().equals(foundSupplier.getDescription())))) {
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
                if (productCategory == product.getProductCategory())){
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

    private void overWriteListOfProductsInFile(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE, false))) {
            for (Product product : products) {
                String line = product.getId() + DATA_SEPARATOR +
                        product.getName() + DATA_SEPARATOR +
                        product.getDefaultPrice().toString() + DATA_SEPARATOR +
                        product.getDefaultCurrency().toString() + DATA_SEPARATOR +
                        product.getDescription() + DATA_SEPARATOR +
                        product.getProductCategory().getId() + DATA_SEPARATOR +
                        product.getSupplier().getId();
                writer.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}