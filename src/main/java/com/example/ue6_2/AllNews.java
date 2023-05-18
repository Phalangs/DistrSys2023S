package com.example.ue6_2;


import java.util.List;

public class AllNews {

    public List<SingleArticle> results;

    public String status;
    public int totalResults;
    public String nextPage;

    public AllNews(){}

    public List<SingleArticle> getResults() {
        return results;
    }

    public void setResults(List<SingleArticle> results) {
        this.results = results;
    }
}
