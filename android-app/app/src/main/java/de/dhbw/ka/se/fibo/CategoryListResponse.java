package de.dhbw.ka.se.fibo;

import com.google.gson.annotations.SerializedName;

public class CategoryListResponse {

    private int id;
    private String name;
    @SerializedName("account")
    private int accountID;


    @Override
    public String toString() {
        return "CategoryListResponse{" +
                "id=" + id +
                ", name=" + name +
                ", accountID=" + accountID +
                '}';
    }
}
