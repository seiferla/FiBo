package de.dhbw.ka.se.fibo.utils.backend;

import de.dhbw.ka.se.fibo.models.Place;

public class PlacesListResponse {

    public int id;
    public String address;
    public String name;

    @Override
    public String toString() {
        return "PlacesListResponse{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Place toPlace() {
        return new Place(id, name, address);
    }
}
