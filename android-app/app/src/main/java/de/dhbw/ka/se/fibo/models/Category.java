package de.dhbw.ka.se.fibo.models;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import de.dhbw.ka.se.fibo.R;


public class Category {

    private List<Integer> colorList = Arrays.asList(R.color.purple_500, R.color.teal_200, R.color.teal_700, R.color.black, R.color.white, R.color.orange, R.color.purple, R.color.green, R.color.blue, R.color.pink, R.color.yellow, R.color.red, R.color.green1, R.color.light_purple, R.color.delete_red);
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public int getColor() {
        String categoryName = getName();
        int index = Math.abs(categoryName.hashCode())%colorList.size();
        return colorList.get(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @NotNull
    public String toString() {
        return "Category{" +
                "name=" + name +
                '}';
    }
}
