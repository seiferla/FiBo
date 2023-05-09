package de.dhbw.ka.se.fibo.utils.backend;

import com.google.gson.annotations.SerializedName;

import de.dhbw.ka.se.fibo.models.Category;

public class CategoryListResponse {

    public int id;
    public String name;
    @SerializedName("account")
    public int accountID;


    public Category toCategory() {
        return new Category(
                id,
                name,
                accountID
        );
    }

    @Override
    public String toString() {
        return "CategoryListResponse{" +
                "id=" + id +
                ", name=" + name +
                ", accountID=" + accountID +
                '}';
    }
}
