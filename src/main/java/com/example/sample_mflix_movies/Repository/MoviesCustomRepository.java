package com.example.sample_mflix_movies.Repository;

import com.example.sample_mflix_movies.model.Movies;
import org.bson.Document;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface MoviesCustomRepository {

    List<Movies> getAll();
    Movies getById(String id);
    Document explainQuery(Query query);
    List<Movies> getQuery();
    List<Movies> getFullTextQuery();
    List<Movies> getMoviesByLang(String type, String[] lang);
    List<String> getDistinctValuesString(String field);
    List<Integer> getDistinctValuesInt(String field);
    List<IndexInfo> getListOfIndexes();
    void setIndex(String keyField, String sortOrder);
    void setCompoundIndex(Document listOfKeys);
    void removeIndexByIndexName(String nameOfIndex);
    List<Document> getAllCommentOfMovie(String movieTitle);
    List<Movies> getListOfYearDocs(Integer startYear,Integer endYear);
    void addAll(List<Movies> moviesList);
    void tempDelete(Integer year);
}
