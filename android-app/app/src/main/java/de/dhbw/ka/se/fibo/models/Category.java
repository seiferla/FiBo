package de.dhbw.ka.se.fibo.models;


import de.dhbw.ka.se.fibo.R;


public enum Category {


    RESTAURANT(R.color.orange, R.string.INSURANCE), HEALTH(R.color.purple,
            R.string.CLOTHES), LIVING(R.color.green, R.string.LIVING), HOUSEHOLD(R.color.blue,
            R.string.HOUSEHOLD), CULTURE(R.color.pink, R.string.CULTURE), EDUCATION(R.color.yellow,
            R.string.EDUCATION), SOCIALLIFE(R.color.red, R.string.SOCIALLIFE), MOBILITY(R.color.green1,
            R.string.MOBILITY), CLOTHES(R.color.expense, R.string.CLOTHES), GIFT(R.color.purple_500,
            R.string.GIFT), OTHER(R.color.black, R.string.OTHER), INSURANCE(R.color.lightpurple,
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
}
