package com.sigmacr.ui.beans;

import com.sigmacr.commons.data.ProductItem;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean
@SessionScoped
public class SearchBean {

    private static final int NO_TRENDING_PRODUCTS = 3;

    private String query;
    private List<ProductItem> searchResults;
    private List<ProductItem> trends;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String search(){
        //TODO Implement
        searchResults = new ArrayList<>();
        return "search_results";
    }

    public List<ProductItem> getSearchResults() {
        if(searchResults==null){
            searchResults = new ArrayList<>();
        }
        return searchResults;
    }

    public void setSearchResults(List<ProductItem> searchResults) {
        this.searchResults = searchResults;
    }

    public List<ProductItem> getTrends() {
        if(trends==null){
            trends = new ArrayList<>();
        }

        if(trends.size()> NO_TRENDING_PRODUCTS){
            return trends.subList(0, NO_TRENDING_PRODUCTS);
        }else {
            return trends;
        }

    }

    public void setTrends(List<ProductItem> trends) {
        this.trends = trends;
    }
}
