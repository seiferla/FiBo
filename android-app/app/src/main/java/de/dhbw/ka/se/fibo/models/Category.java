package de.dhbw.ka.se.fibo.models;


import org.jetbrains.annotations.NotNull;

import de.dhbw.ka.se.fibo.R;


public enum Category {


    RESTAURANT(R.color.orange, R.string.RESTAURANT), HEALTH(R.color.purple,
            R.string.HEALTH), LIVING(R.color.green, R.string.LIVING), HOUSEHOLD(R.color.blue,
            R.string.HOUSEHOLD), CULTURE(R.color.pink, R.string.CULTURE), EDUCATION(R.color.yellow,
            R.string.EDUCATION), SOCIAL_LIFE(R.color.red, R.string.SOCIALLIFE), MOBILITY(R.color.green1,
            R.string.MOBILITY), CLOTHES(R.color.expense, R.string.CLOTHES), GIFT(R.color.purple_500,
            R.string.GIFT), OTHER(R.color.black, R.string.OTHER), INSURANCE(R.color.light_purple,
            R.string.INSURANCE);

    private int color;
    private int name;

    Category(int color, int name) {
        this.name = name;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
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
