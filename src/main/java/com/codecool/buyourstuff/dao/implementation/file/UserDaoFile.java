package com.codecool.buyourstuff.dao.implementation.file;

import com.codecool.buyourstuff.dao.CartDao;
import com.codecool.buyourstuff.dao.DataManager;
import com.codecool.buyourstuff.dao.UserDao;
import com.codecool.buyourstuff.model.Cart;
import com.codecool.buyourstuff.model.User;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserDaoFile implements UserDao {

    private final File USER_FILE = new File("data/users.csv");
    private int nextUserId;

    public UserDaoFile() {
        if (USER_FILE.exists()) {
            try (Scanner scanner = new Scanner(USER_FILE)) {
                scanner.useDelimiter(",");
                while (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
                scanner.findInLine("id=");
                int lastUserId = scanner.nextInt();
                nextUserId = lastUserId + 1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else nextUserId = 1;
    }

    @Override
    public void add(User user) {
        if (isNameAvailable(user.getName())) {
            CartDao cartDao = DataManager.getCartDao();
            Cart cart = new Cart();
            cartDao.add(cart);

            user.setCartId(cart.getId());
            user.setId(nextUserId++);

            try (FileWriter fileWriter = new FileWriter(USER_FILE, true)) {
                fileWriter
                        .append(String.valueOf(user.getId()))
                        .append(',')
                        .append(user.getName())
                        .append(',')
                        .append(user.getPassword())
                        .append(',')
                        .append(String.valueOf(user.getCartId()))
                        .append('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User find(String name, String password) {
        try (Scanner scanner = new Scanner(USER_FILE)) {
            while (scanner.hasNextLine()) {
                User user = readUserFromLine(scanner.nextLine());
                if (name.equals(user.getName()) && BCrypt.checkpw(password, user.getPassword()))
                    return user;
            }
            throw new DataNotFoundException("No such user. Name or password may be incorrect.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clear() {
        try {
            new FileWriter(USER_FILE, false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        nextUserId = 1;
    }

    @Override
    public boolean isNameAvailable(String name) {
        try (Scanner scanner = new Scanner(USER_FILE)) {
            scanner.useDelimiter(",");
            while (scanner.hasNextLine()) {
                scanner.findInLine("name=");
                if (name.equals(scanner.next())) return false;
                scanner.nextLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
