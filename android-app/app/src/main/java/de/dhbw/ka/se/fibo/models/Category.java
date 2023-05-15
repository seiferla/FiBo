package de.dhbw.ka.se.fibo.models;


import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.dhbw.ka.se.fibo.R;


public class Category implements Comparable<Category> {
    private static final List<Integer> COLOR_LIST = Arrays.asList(R.color.purple_500, R.color.teal_200, R.color.teal_700, R.color.black, R.color.white, R.color.orange, R.color.purple, R.color.green, R.color.blue, R.color.pink, R.color.yellow, R.color.red, R.color.green1, R.color.light_purple, R.color.delete_red);
    private int id;
    private String name;
    private int accountID;


    public Category(int id, String name, int accountID) {
        this.id = id;
        this.name = name;
        this.accountID = accountID;
    }

    public int getColor() {
        String categoryName = getName();
        int index = Math.abs(categoryName.hashCode()) % Category.COLOR_LIST.size();

        return Category.COLOR_LIST.get(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountID=" + accountID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id && accountID == category.accountID && name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accountID);
    }

    @Override
    public int compareTo(Category o) {
        int result = Integer.compare(getId(), o.getId());

        if (0 == result) {
            result = getName().compareTo(o.getName());
        }

        return result;
    }
}
