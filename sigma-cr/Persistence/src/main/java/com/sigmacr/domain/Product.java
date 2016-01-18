package com.sigmacr.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/21/13
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Product {
    String category;
    String modelName;
    String brand;
    String imageUrl;
    String iconUrl;

    public Product(String category, String modelName, String brand) {
        this.category = category;
        this.modelName = modelName;
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
