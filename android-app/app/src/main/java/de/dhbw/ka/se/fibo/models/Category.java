package de.dhbw.ka.se.fibo.models;


import android.graphics.Color;

import java.util.Random;


public enum Category {


    RESTAURANT(new Color(), "Restaurant"), GESUNDHEIT(new Color(), "Gesundheit"), WOHNEN(new Color(), "Wohnen"), HAUSHALT(new Color(), "Haushalt"), KULTUR(new Color(), "Kultur"), BILDUNG(new Color(), "Bildung"), SOZIALLEBEN(new Color(), "Sozialleben"), MOBILITÄT(new Color(), "Mobilit\u00e4t"), BEKLEIDUNG(new Color(), "Bekleidung"), GESCHENK(new Color(), "Geschenk"), SONSTIGE(new Color(), "Sonstige"), VERSICHERUNG(new Color(), "");


    private Color color;
    private String name;

    Category(Color color, String name) {

        this.name = name;
        this.color = color;
    }

    Category(String name) {

        this.name = name;

        Random random = new Random();

        int a = random.nextInt(255) + 1;
        int r = random.nextInt(255) + 1;
        int g = random.nextInt(255) + 1;
        int b = random.nextInt(255) + 1;

        // Hängt mit der API version zusammen
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            this.color = Color.valueOf(Color.argb(a, r, g, b));

        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
