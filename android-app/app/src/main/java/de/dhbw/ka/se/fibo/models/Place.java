package de.dhbw.ka.se.fibo.models;

public class Place {

    private int placeId;
    private String name;
    private String address;


    public Place(String name, int placeId, String address) {
        this.name = name;
        this.address = address;
        this.placeId = placeId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
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
                "name='" + name + '\'' +
                '}';
    }
}
