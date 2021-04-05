package com.example.sample_mflix_movies.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.util.Date;
import java.util.List;

@Document(collection = "movies")
@Getter
@Setter
@NoArgsConstructor
public class Movies {
    @Id
    private String id;
    private Awards awards;
    private List<String> cast;
    private List<String> countries;
    private List<String> directors;

    @TextIndexed
    private String fullplot;
    @TextScore
    private Float score;
    @TextIndexed
    private List<String> genres;
    private Imdb imdb;
    private List<String> languages;
    private String lastupdated;
    private Integer metacritic;
    private Integer num_mflix_comments;
    private String plot;
    private String poster;
    private String rated;
    private Date released;
    private Integer runtime;
    private String title;
    private Tomatoes tomatoes;
    private String type;
    private List<String>writers;
    private Integer year;


    public Movies(String title, Integer year) {
        this.title = title;
        this.year = year;
    }

    public Movies(Integer year) {
        this.year = year;
    }

}
