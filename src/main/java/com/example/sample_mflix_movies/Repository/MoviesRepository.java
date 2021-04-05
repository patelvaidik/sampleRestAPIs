package com.example.sample_mflix_movies.Repository;

import com.example.sample_mflix_movies.model.Movies;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesRepository extends MongoRepository<Movies,String>,MoviesCustomRepository{

    @Query(sort = "{year:1}",fields = "{'id':0 , 'title':1 ,'year':1 }")
    List<Movies> findByYearBetween(int from, int to, Pageable pageable); //    from < year < to
}
