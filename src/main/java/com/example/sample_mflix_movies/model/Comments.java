package com.example.sample_mflix_movies.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "comments")
@Getter
@Setter
public class Comments {
    @Id
    private String id;
    private Date date;
    private String email;
    private String movie_id;
    private String name;
    private String text;



}
