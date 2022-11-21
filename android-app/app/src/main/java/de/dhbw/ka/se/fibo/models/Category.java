package de.dhbw.ka.se.fibo.models;


import android.graphics.Color;

import java.util.Random;


public enum Category {


    RESTAURANT(Color.rgb(216, 49, 91), "Restaurant"), GESUNDHEIT(Color.rgb(89, 178, 34), "Gesundheit"), WOHNEN(Color.rgb(231,74,22), "Wohnen"), HAUSHALT(Color.rgb(33,11,99), "Haushalt"), KULTUR(Color.rgb(53,23,200), "Kultur"), BILDUNG(Color.rgb(234,33,11), "Bildung"), SOZIALLEBEN(Color.rgb(223,22,44), "Sozialleben"), MOBILITÄT(Color.rgb(23,99,22), "Mobilit\u00e4t"), BEKLEIDUNG(Color.rgb(234,200,18), "Bekleidung"), GESCHENK(Color.rgb(66,83,244), "Geschenk"), SONSTIGE(Color.rgb(33,1,9), "Sonstige"), VERSICHERUNG(Color.rgb(23,42,44), "Versicherung");


    private int color;
    private String name;

    Category(int color, String name) {

        this.name = name;
        this.color = color;
    }

    Category(String name) {

        this.name = name;

        Random random = new Random();

        int r = random.nextInt(255) + 1;
        int g = random.nextInt(255) + 1;
        int b = random.nextInt(255) + 1;

        this.color = Color.rgb(r,g,b);


    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
