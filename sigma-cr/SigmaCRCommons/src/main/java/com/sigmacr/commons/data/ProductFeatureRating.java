package com.sigmacr.commons.data;

public class ProductFeatureRating {
    private String featureName;
    private double featureRating;
    private int positiveReviews;
    private int negativeReviews;
    private int neutralReviews;
    private int totalReviews;

    public ProductFeatureRating() {
    }

    public ProductFeatureRating(String featureName, double featureRating, int positiveReviews, int negativeReviews,
                                int totalReviews) {
        this.featureName = featureName;
        this.featureRating = featureRating;
        this.positiveReviews = positiveReviews;
        this.negativeReviews = negativeReviews;
        this.totalReviews = totalReviews;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public double getFeatureRating() {
        return featureRating;
    }

    public void setFeatureRating(double featureRating) {
        this.featureRating = featureRating;
    }

    public int getPositiveReviews() {
        return positiveReviews;
    }

    public void setPositiveReviews(int positiveReviews) {
        this.positiveReviews = positiveReviews;
    }

    public int getNegativeReviews() {
        return negativeReviews;
    }

    public void setNegativeReviews(int negativeReviews) {
        this.negativeReviews = negativeReviews;
    }

    public int getNeutralReviews() {
        return neutralReviews;
    }

    public void setNeutralReviews(int neutralReviews) {
        this.neutralReviews = neutralReviews;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }
}
