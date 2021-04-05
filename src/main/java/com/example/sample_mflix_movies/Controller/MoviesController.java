package com.example.sample_mflix_movies.Controller;

import com.example.sample_mflix_movies.Services.MoviesService;
import com.example.sample_mflix_movies.model.Movies;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    MoviesService moviesService;

    @GetMapping("/showAll")
    public List<Movies> getAll() {
        return moviesService.getAll();
    }

    @GetMapping("/show/{id}")
    public Movies getById(@PathVariable String id) {
        return moviesService.getById(id);
    }

    @GetMapping("/distinctValueString/{field}")
    public List<String> getDistinctValuesString(@PathVariable String field) {

        return moviesService.getDistinctValuesString(field);
    }

    @GetMapping("/distinctValueInt/{field}")
    public List<Integer> getDistinctValuesInt(@PathVariable String field) {
        return moviesService.getDistinctValuesInt(field);
    }

    @GetMapping("/findByYearRange/{startYear}/{endYear}")
    public List<Movies> getByYearRange(@PathVariable Integer startYear, @PathVariable Integer endYear, @RequestParam(defaultValue = "0",required = false) int pageNo, @RequestParam (defaultValue = "10",required = false) int size)
    {
        Pageable pageable = PageRequest.of(pageNo,size);
        return moviesService.getByYearRange(startYear, endYear,pageable);
    }

    @GetMapping("/yearRange/{startYear}/{endYear}")
    public Integer getYearRangeInclude(@PathVariable Integer startYear, @PathVariable Integer endYear)
    {
        return moviesService.getYearRangeInclude(startYear,endYear);
    }

    @GetMapping("/query")
    public List<Movies> getQuery() {
        return moviesService.getQuery();
    }

    @GetMapping("/fullTextQuery")
    public List<Movies> getFullTextQuery()
    {
        return moviesService.getFullTextQuery();
    }

    @GetMapping("/lang/{type}")
    public List<Movies> getMoviesByLang(@PathVariable String type, @RequestBody String... lang) {
        return moviesService.getMoviesByLang(type, lang);
    }

    @GetMapping("/ListOfIndexes")
    public List<IndexInfo> getListOfIndexes()
    {
        return moviesService.getListOfIndexes();
    }

    @GetMapping("/setIndex/{fieldKey}/{sortOrder}")
    public void  setIndex(@PathVariable String fieldKey, @PathVariable String sortOrder)
    {
        moviesService.setIndex(fieldKey, sortOrder);
    }

    @GetMapping("/setCompoundIndex")
    public void setCompoundIndex(@RequestBody  Document listOfKeys)
    {
        moviesService.setCompoundIndex(listOfKeys);
    }

    @GetMapping("/removeIndex/{nameOfIndex}")
    public void removeIndexByIndexName(@PathVariable String nameOfIndex)
    {
        moviesService.removeIndexByIndexName(nameOfIndex);
    }

    @PostMapping()
    public void add(@RequestBody Movies movie) {
        moviesService.add(movie);
    }

    @PutMapping()
    public void update(@RequestBody Movies movie) {
        moviesService.update(movie);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        moviesService.delete(id);
    }
    @GetMapping("/getComments/{movieTitle}")
    public List<Document> getAllCommentOfMovie(@PathVariable String movieTitle){
        return moviesService.getAllCommentOfMovie(movieTitle);
    }

}
