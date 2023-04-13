package de.dhbw.ka.se.fibo.models;

import org.jetbrains.annotations.NotNull;

public class Place {

    private String name;
    private String address;


    public Place(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    @NotNull
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                '}';
    }
}
