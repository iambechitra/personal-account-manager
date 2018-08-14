package com.example.bechitra.walleto.table;

public class Wallet {
    String ID, activation, name;

    public Wallet(String ID, String name, String activation) {
        this.ID = ID;
        this.activation = activation;
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public String getActivation() {
        return activation;
    }

    public String getName() {
        return name;
    }
}
