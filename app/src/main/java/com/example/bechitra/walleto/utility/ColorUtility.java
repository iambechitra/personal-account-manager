package com.example.bechitra.walleto.utility;

import android.graphics.Color;

import android.graphics.drawable.GradientDrawable;
import com.example.bechitra.walleto.R;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
        <item>Gift</item>
        <item>Savings</item>

        <item>Business</item>
        <item>Gifts</item>
        <item>Loan</item>
        <item>Extra Income</item>
        <item>Salary</item>
 */

public class ColorUtility {
    private Random random;

    public ColorUtility() {
        this.random = new Random();
    }

    public int getResource(String category) {
        switch (category) {
            case "Home":
                return R.drawable.ic_home;

            case "Food":
                return R.drawable.ic_food;

            case "Sports":
                return R.drawable.ic_sports;

            case "Health & Fitness":
                return R.drawable.ic_health;

            case "Education":
                return R.drawable.ic_education;

            case "Clothing" :
                return R.drawable.ic_clothing;

            case "Drinks" :
                return R.drawable.ic_drinks;

            case "Family & Personal":
                return R.drawable.ic_family;

            case "Travel":
                return R.drawable.ic_travel;

            case "Bills":
                return R.drawable.ic_bills;

            case "Car":
                return R.drawable.ic_car;

            case "Entertainment":
                return R.drawable.ic_entertainment;

            case "Shopping":
                return R.drawable.ic_shopping;

            case "Accommodation":
                return R.drawable.ic_accomodation;

            case "Utility":
                return R.drawable.ic_utility;

            case "Other":
                return R.drawable.ic_other;

            case "Transport":
                return R.drawable.ic_transport;

            case "Groceries":
                return R.drawable.ic_groceries;

            case "Hobbies":
                return R.drawable.ic_hobbies;

            case "Pets":
                return R.drawable.ic_pet;

            case "Cinema":
                return R.drawable.ic_cinema;

            case "Love":
                return R.drawable.ic_love;

            case "Kids":
                return R.drawable.ic_kids;

            case "Rent":
                return R.drawable.ic_rent;

            case "Gift":
                return R.drawable.ic_gift;

            case "Savings":
                return R.drawable.ic_saving;

            case "Gifts":
                return R.drawable.ic_gift;

            case "Loan":
                return R.drawable.ic_loan;

            case "Business":
                return R.drawable.ic_business;

            case "Extra Income":
                return R.drawable.ic_extra_income;

            case "Salary":
                return R.drawable.ic_salary;

        }

        return R.drawable.ic_other;
    }


    public int getColors(String category) {
        switch (category) {
            case "Home":
                return Color.parseColor("#6A1B9A");

            case "Food":
                return Color.parseColor("#00C853");

            case "Sports":
                return Color.parseColor("#64DD17");

            case "Health & Fitness":
                return Color.RED;

            case "Education" :
                return Color.parseColor("#C51162");

            case "Clothing" :
                return Color.parseColor("#880E4F");

            case "Drinks":
                return Color.parseColor("#DD2C00");

            case "Family & Personal":
                return Color.parseColor("#F50057");

            case "Travel":
                return Color.parseColor("#6200EA");

            case "Bills":
                return Color.parseColor("#311B92");

            case "Car":
                return Color.parseColor("#673AB7");

            case "Entertainment":
                return Color.parseColor("#004D40");

            case "Shopping":
                return Color.parseColor("#00BFA5");

            case "Accommodation":
                return Color.parseColor("#26A69A");

            case "Utility":
                return Color.parseColor("#827717");

            case "Other":
                return Color.parseColor("#4A148C");

            case "Transport":
                return Color.parseColor("#BF360C");

            case "Groceries":
                return Color.parseColor("#DD2C00");

            case "Hobbies":
                return Color.parseColor("#006064");

            case "Pets":
                return Color.parseColor("#00BCD4");

            case "Cinema":
                return Color.parseColor("#F50057");

            case "Love":
                return Color.parseColor("#C2185B");

            case "Kids":
                return Color.parseColor("#1976D2");

            case "Rent":
                return Color.parseColor("#33691E");

            case "Gift":
                return Color.parseColor("#FF1744");

            case "Savings":
                return Color.parseColor("#2E7D32");

            case "Gifts":
                return Color.parseColor("#DD2C00");

            case "Loan":
                return Color.parseColor("#B00020");

            case "Business":
                return Color.parseColor("#9E9D24");

            case "Extra Income":
                return Color.parseColor("#00B8D4");

            case "Salary" :
                return Color.parseColor("#00E676");
        }

        return Color.parseColor("#004D40");
    }

    public int getLighterColor(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    public int getRandomColor() {
        String[] color = {"#827717", "#00695C", "#006064", "#0277BD", "#0D47A1", "#3949AB",
                                "#311B92", "#880E4F", "#8E24AA"};

        int rand = random.nextInt(color.length-1);
        return Color.parseColor(color[rand]);
    }

    public GradientDrawable generateOvalShape(int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setShape(GradientDrawable.OVAL);

        return gd;
    }
}
