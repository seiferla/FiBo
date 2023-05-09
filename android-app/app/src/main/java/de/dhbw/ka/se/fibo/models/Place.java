package de.dhbw.ka.se.fibo.models;

import androidx.annotation.NonNull;

public class Place implements Comparable<Place> {

    private int id;
    private String name;
    private String address;


    public Place(int id, String name, String address) {
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
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
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public int compareTo(Place o) {
        int idCompareResult = Integer.compare(id, o.id);

        if (0 != idCompareResult) {
            return idCompareResult;
        }

        int nameCompareResult = name.compareTo(o.name);

        if (0 != nameCompareResult) {
            return nameCompareResult;
        }

        return address.compareTo(o.address);
    }
}
