package com.wenshi_egypt.wenshi;

public class Vehicle {
    private String type, model;

    Vehicle(String type, String model)   {
        this.type = type;
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}