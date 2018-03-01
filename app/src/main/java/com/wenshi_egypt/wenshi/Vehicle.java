package com.wenshi_egypt.wenshi;

public class Vehicle {
    private String type, type2, model;

    Vehicle(String type, String type2, String model)   {
        this.type = type;
        this.type2 = type2;
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}