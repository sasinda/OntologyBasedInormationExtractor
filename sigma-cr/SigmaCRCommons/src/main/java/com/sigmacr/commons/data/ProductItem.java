package com.sigmacr.commons.data;

public class ProductItem {
    private long productId;
    private String productName;
    private String productImageURL;
    private String overallRating;

    public ProductItem() {
    }

    public ProductItem(long productId, String productName, String productImageURL, String overallRating) {
        this.productId = productId;
        this.productName = productName;
        this.productImageURL = productImageURL;
        this.overallRating = overallRating;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }
}
