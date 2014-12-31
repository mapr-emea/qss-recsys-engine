package com.mapr.demo.recsys.model;

import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;

public class FilmItem extends Item {
    @Field
    String year;

    @Field
    String url;

    @Field
    List<String> genre;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }
}
