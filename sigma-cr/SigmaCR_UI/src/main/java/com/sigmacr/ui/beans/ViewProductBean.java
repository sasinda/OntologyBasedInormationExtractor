package com.sigmacr.ui.beans;

import com.sigmacr.commons.data.ProductFeatureRating;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@SessionScoped
public class ViewProductBean {
    private long id;
    private String productName;
    private String imageURL;

    private List<ProductFeatureRating> featureRatings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<ProductFeatureRating> getFeatureRatings() {
        return featureRatings;
    }

    public void setFeatureRatings(List<ProductFeatureRating> featureRatings) {
        this.featureRatings = featureRatings;
    }

    public String getProductDetails(long id){
        this.id = id;
        //TODO: quick view option for static data, need to implement
        return "product_summary";
    }

    public String getGraphData(){
        StringBuilder builder = new StringBuilder();
        builder.append("{" +
                "chart:{" +
                "type:'bar'" +
                "}," +
                "title:{" +
                "text: 'Feature scores summary for "+productName+"'"+
                "}," +
                "xAxis:{" +
                "categories:["
        );
        if(featureRatings!=null && featureRatings.size()>0){
            for (ProductFeatureRating featureRating : featureRatings) {
                builder.append("'").append(featureRating.getFeatureName()).append("',");
            }
            builder.deleteCharAt(builder.length()-1);   //remove the last ',' character
        }
        builder.append("]," +
                "title:{" +
                "text:null" +
                "}" +
                "}," +
                "yAxis:{" +
                "min: 0," +
                "max: 10," +
                "title: {" +
                "text: 'Score'," +
                "align: 'high'" +
                "}," +
                "lables:{" +
                "overflow: 'justify'" +
                "}" +
                "}," +
                "tooltip: {" +
                "valueSuffix:''" +
                "}," +
                "plotOptions: {" +
                "bar: {" +
                "dataLabels: {" +
                "enabled: true" +
                "}" +
                "}" +
                "}," +
                "legend: {" +
                "layout: 'vertical'," +
                "align: 'right'," +
                "verticalAlign: 'top'," +
                "x: 0," +
                "y: 0," +
                "floating: true," +
                "boarderWidth:1," +
                "backgroundColor: 'transparent'," +
                "shadow: true" +
                "}," +
                "credits: {" +
                "enabled: false" +
                "}," +
                "series:[{" +
                "showInLegend:false," +
                "name:''," +
                "data:[");

        if(featureRatings!=null && featureRatings.size()>0){
            for (ProductFeatureRating featureRating : featureRatings) {
                builder.append("{y:").append(String.format("%.2f",featureRating.getFeatureRating())).append
                        (",color:'#");

                //append the color based on the rating
                if(featureRating.getFeatureRating()>7){
                    builder.append("52D017");
                } else if(featureRating.getFeatureRating()<3.5){
                    builder.append("F75D59");
                } else{
                    builder.append("008080");
                }
                builder.append("'},");


            }
            builder.deleteCharAt(builder.length()-1);   //remove the last ',' character
        }
        builder.append("]," +
                "pointWidth: 20" +
                "}]" +
                "}");
        return builder.toString();
    }
}
