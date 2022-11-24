package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.dao.implementation.database.CartDaoDb;
import com.codecool.buyourstuff.dao.implementation.database.ProductCategoryDaoDb;
import com.codecool.buyourstuff.dao.implementation.database.SupplierDaoDb;
import com.codecool.buyourstuff.dao.implementation.database.UserDaoDb;
import com.codecool.buyourstuff.dao.implementation.file.CartDaoFile;
import com.codecool.buyourstuff.dao.implementation.file.UserDaoFile;
import com.codecool.buyourstuff.dao.implementation.mem.*;

public class DaoImplementationSupplier {

    private final ProductDao productDao;
    private final ProductCategoryDao productCategoryDao;
    private final SupplierDao supplierDao;
    private final CartDao cartDao;
    private final LineItemDao lineItemDao;
    private final UserDao userDao;

    private DaoImplementationSupplier(ProductDao productDao,
                                      ProductCategoryDao productCategoryDao,
                                      SupplierDao supplierDao,
                                      CartDao cartDao,
                                      LineItemDao lineItemDao,
                                      UserDao userDao) {
        this.productDao = productDao;
        this.productCategoryDao = productCategoryDao;
        this.supplierDao = supplierDao;
        this.cartDao = cartDao;
        this.lineItemDao = lineItemDao;
        this.userDao = userDao;
    }

    public static DaoImplementationSupplier getInstance(DaoType daoType) {
        printImplementation(daoType);
        switch (daoType) {
            case FILE:
                return new DaoImplementationSupplier(
                        new ProductDaoFile(),
                        new ProductCategoryDaoFile(),
                        new SupplierDaoFile(),
                        new CartDaoFile(),
                        new LineItemDaoFile(),
                        new UserDaoFile()
                );
            case DATABASE:
                return new DaoImplementationSupplier(
                        new ProductDaoDb(),
                        new ProductCategoryDaoDb(),
                        new SupplierDaoDb(),
                        new CartDaoDb(),
                        new LineItemDaoDb(),
                        new UserDaoDb()
                );

            case MEMORY:
                return new DaoImplementationSupplier(
                    new ProductDaoMem(),
                    new ProductCategoryDaoMem(),
                    new SupplierDaoMem(),
                    new CartDaoMem(),
                    new LineItemDaoMem(),
                    new UserDaoMem()
                );
        }
    }

    private static void printImplementation(DaoType daoType) {
        System.out.printf("--- Using %s DAO implementations ---\n", daoType);
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public ProductCategoryDao getProductCategoryDao() {
        return productCategoryDao;
    }

    public SupplierDao getSupplierDao() {
        return supplierDao;
    }

    public CartDao getCartDao() {
        return cartDao;
    }

    public LineItemDao getLineItemDao() {
        return lineItemDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
