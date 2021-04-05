package com.example.sample_mflix_movies.Services;

import com.example.sample_mflix_movies.Repository.MoviesRepository;
import com.example.sample_mflix_movies.model.Movies;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MoviesService {

    @Autowired
    MoviesRepository moviesRepository;

    public void addAll(List<Movies> moviesList)
    {
        moviesRepository.addAll(moviesList);
    }

    public List<Movies> getAll() {

        return moviesRepository.getAll();
    }

    public Movies getById(String id) {

        return moviesRepository.getById(id);
    }

    public List<Movies> getQuery() {

        return moviesRepository.getQuery();
    }

    public List<Movies> getFullTextQuery() {

        return moviesRepository.getFullTextQuery();
    }

    public List<Movies> getMoviesByLang(String type, String[] lang) {

        return moviesRepository.getMoviesByLang(type,lang);
    }

    public List<String> getDistinctValuesString(String field) {

        return moviesRepository.getDistinctValuesString(field);
    }

    public List<Integer> getDistinctValuesInt(String field) {

        return moviesRepository.getDistinctValuesInt(field);
    }

    public List<IndexInfo> getListOfIndexes() {

        return moviesRepository.getListOfIndexes();
    }

    public void setIndex(String keyField, String sortOrder) {

        moviesRepository.setIndex(keyField,sortOrder);
    }

    public void setCompoundIndex(Document listOfKeys) {

        moviesRepository.setCompoundIndex(listOfKeys);
    }

    public void removeIndexByIndexName(String nameOfIndex) {

        moviesRepository.removeIndexByIndexName(nameOfIndex);
    }

    public void add(Movies movie) {

        moviesRepository.save(movie);
    }

    public void update(Movies movie) {

        moviesRepository.save(movie);
    }

    public void delete(String id) {

        moviesRepository.deleteById(id);
    }

    public List<Movies> getByYearRange(Integer startYear, Integer endYear, Pageable pageable) {

        return moviesRepository.findByYearBetween(startYear, endYear,pageable);
    }
    public List<Document> getAllCommentOfMovie(String movieTitle){

        return moviesRepository.getAllCommentOfMovie(movieTitle);
    }
    boolean checkLeapYear(Integer year)
    {
        if(year<=0)
            return false;

        if(year%400 == 0)
            return true;

        if(year%100 == 0)
            return false;

        if(year%4 == 0)
            return true;

        return false;
    }
    public Integer countTotalLeapYear(List<Movies> movies)
    {
        int count=0,currYear;
        for(Movies m:movies)
        {
//            if(m.getYear() == null || m.getYear()<=0) //handle NullPointerException & invalid Input value
//               continue;

            try {
                currYear = m.getYear();
            }
            catch (NullPointerException e)
            {
                log.error("NullPointerException occur!!!");
                continue;
            }

            if(checkLeapYear(currYear))
                count++;
        }
        return count;
    }
    public Integer getYearRangeInclude(Integer startYear, Integer endYear) {

        List<Movies> docs = moviesRepository.getListOfYearDocs(startYear, endYear);

        return countTotalLeapYear(docs);
    }

    public void removeDocContainLeapYear(List<Movies> moviesList)
    {
        int currYear;
        for(Movies m:moviesList)
        {
            if(m.getYear()==null || m.getYear()<=0)
                continue;

            if(checkLeapYear(m.getYear()))
            {
                //delete(m.getId());
                moviesRepository.tempDelete(m.getYear());
            }
        }
    }

    public void deleteAll()
    {
        moviesRepository.deleteAll();
    }
}

