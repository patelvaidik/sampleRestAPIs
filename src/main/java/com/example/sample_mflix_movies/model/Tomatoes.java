package com.example.sample_mflix_movies.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Tomatoes {
    private String boxOffice;
    private String consensus;
    private Critic critic;
    private Critic viewer;
    private Date dvd;
    private Integer fresh;
    private Date lastUpdated;
    private String production;
    private Integer rotten;
    private String website;
}
