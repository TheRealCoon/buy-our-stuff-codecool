package com.codecool.buyourstuff.model;

public class UserDTO {

    private final int id;
    private final String name;
    private final String password;
    private final int cartId;

    public UserDTO(int id, String name, String password, int cartId) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.cartId = cartId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getCartId() {
        return cartId;
    }
}
