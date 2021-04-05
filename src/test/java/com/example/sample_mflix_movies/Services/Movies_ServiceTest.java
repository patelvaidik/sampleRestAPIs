package com.example.sample_mflix_movies.Services;

import com.example.sample_mflix_movies.Repository.MoviesRepository;
import com.example.sample_mflix_movies.model.Movies;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
class Movies_ServiceTest {

    @Mock
    MoviesRepository moviesRepository;

    @InjectMocks
    MoviesService moviesService;

    static List<Movies> moviesList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        System.out.println("Setup for Testing!!!");
        log.info("start!!!");
    }

    @AfterEach
    public void afterAll() {
        System.out.println("Termination of Testing!!!");
        log.info("end!!!");
    }

    @BeforeAll
    static void beforeAll() {

        moviesList.add(new Movies(2011));
        moviesList.add(new Movies(2012));
        moviesList.add(new Movies(2013));
        moviesList.add(new Movies(2014));
        moviesList.add(new Movies(2015));
        moviesList.add(new Movies(2016));
        moviesList.add(new Movies(2017));
        moviesList.add(new Movies(2018));
        moviesList.add(new Movies(2019));
        moviesList.add(new Movies(2020));
        moviesList.add(new Movies(2021));
        moviesList.add(new Movies(2022));
        moviesList.add(new Movies(2023));
        moviesList.add(new Movies(2024));
        moviesList.add(new Movies(-2024)); //check for this function handle invalid value or not
        moviesList.add(new Movies()); //check for this function handle NullPointerException or not
    }

    @Test
    void checkLeapYearTest() {

        //Assert.assertEquals(false,moviesService.checkLeapYear(2021));
        //Assert.assertEquals(true,moviesService.checkLeapYear(2020));
        assertEquals(false, moviesService.checkLeapYear(-2024));
    }

    @Test
    void countTotalLeapYearTest() {

        assertEquals((Integer) 4, (Integer) moviesService.countTotalLeapYear(moviesList));

    }

    @Test
    void getYearRangeIncludeTest() {

        when(moviesRepository.getListOfYearDocs(anyInt(), anyInt())).thenReturn(moviesList);

        assertEquals((Integer) 4, (Integer) moviesService.getYearRangeInclude(2011, 2024));
        assertEquals((Integer) 4, (Integer) moviesService.getYearRangeInclude(2000, 2024)); //not matter on argument value

    }

    @Test
    void removeDocContainLeapYearTest() {

        moviesService.removeDocContainLeapYear(moviesList);
        verify(moviesRepository, times(4)).tempDelete(anyInt()); //make fack delete function to verify logic of given function


//        way-1 ----------------------------------
/**
 *      @Autowired
 *      MoviesRepository moviesRepository;
 *      @Autowired
 *      MoviesService moviesService;
 * **/
//        List<Movies>moviesList = new ArrayList<>();
//        moviesList.add(new Movies(2011));
//        moviesList.add(new Movies(2012));
//        moviesList.add(new Movies(2013));
//        moviesList.add(new Movies(2014));
//        moviesList.add(new Movies(2015));
//        moviesList.add(new Movies(2016));
//        moviesList.add(new Movies(2017));
//        moviesList.add(new Movies(2018));
//        moviesList.add(new Movies(2019));
//        moviesList.add(new Movies(2020));
//        moviesList.add(new Movies(2021));
//        moviesList.add(new Movies(2022));
//        moviesList.add(new Movies(2023));
//        moviesList.add(new Movies(2024));
//        moviesList.add(new Movies(-2024)); //check for this function handle invalid value or not
//        moviesList.add(new Movies()); //check for this function handle NullPointerException or not
//
//        moviesRepository.addAll(moviesList);
//
//        List<Movies>moviesListFromDatabase = moviesRepository.findAll();
//        System.out.println("before check: "+ moviesListFromDatabase.size());
//        for(Movies m:moviesListFromDatabase)
//        {
//            System.out.println("Year: "+ m.getYear() + "id: " + m.getId());
//        }
//
//        assertEquals((Integer) 4,(Integer) moviesService.countTotalLeapYear(moviesListFromDatabase)); //check this function without actual deletion of documents contain Leap Year
//        moviesService.removeDocContainLeapYear(moviesListFromDatabase);
//
//        System.out.println("after check: "+moviesRepository.count()); // before check - after check = no of deleted docs ==> Number of Documents containing Leap Year
//        moviesRepository.deleteAll();
//---------------------------------------------

        //verify(moviesRepository,times(4)).deleteById(anyString());

    }
}