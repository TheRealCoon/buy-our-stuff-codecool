package com.codecool.buyourstuff.dao;

import com.codecool.buyourstuff.model.User;
import com.codecool.buyourstuff.model.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    //ATTENTION: this dao doesn't have remove, so users will stay in the database/file, unless they are removed manually
    private static final UserDao USER_DAO = DataManager.getUserDao();

    @Test
    void testAdd() {
        User user = new User("test", "test");
        USER_DAO.add(user);
        assertNotEquals(0, user.getId());
    }

    @Test
    void testFind_validId() {
        User user = new User("test2", "test");
        USER_DAO.add(user);

        User result = USER_DAO.find(user.getName(), user.getPassword());
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void testFind_invalidId() {
        assertThrows(DataNotFoundException.class, () -> USER_DAO.find("test3", "test"));
    }

}
