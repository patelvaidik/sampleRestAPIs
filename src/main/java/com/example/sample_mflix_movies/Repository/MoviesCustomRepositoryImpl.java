package com.example.sample_mflix_movies.Repository;

import com.example.sample_mflix_movies.model.Movies;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

@Repository
public class MoviesCustomRepositoryImpl implements MoviesCustomRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public void addAll(List<Movies> moviesList)
    {
        mongoTemplate.insertAll(moviesList);
    }

    public List<Movies> getAll() {
        Query query = new Query().with(PageRequest.of(0, 5));
        return mongoTemplate.find(query, Movies.class);
    }

    public Movies getById(String id) {
        return mongoTemplate.findById(id, Movies.class);
    }

    public Document explainQuery(Query query) {

        Document explainDocument = new Document();
        explainDocument.put("find", "movies");
        explainDocument.put("filter", query.getQueryObject());

        Document command = new Document();
        command.put("explain", explainDocument);

        return mongoTemplate.getDb().runCommand(command);
    }

    public List<Movies> getQuery() {
        List<String> CountryList = new ArrayList<>();
        CountryList.add("India");

        Criteria criteria = Criteria.where("countries").in(CountryList);

        List<Sort.Order> lst = new ArrayList<>();
        lst.add(desc("awards.wins"));
        lst.add(desc("awards.nominations"));
        lst.add(asc("year"));

        Query query = new Query(criteria).with(Sort.by(lst));

        query.fields().include("title", "awards.wins", "awards.nominations", "year");

        Pageable pageable = PageRequest.of(0, 5);
        query.with(pageable);
        //System.out.println(explainQuery(query).toJson(JsonWriterSettings.builder().indent(true).build()));
        return mongoTemplate.find(query, Movies.class);
    }

    public List<Movies> getFullTextQuery() {
        TextCriteria textCriteria = new TextCriteria().matchingAny("Drama").caseSensitive(true);
        TextQuery textQuery = new TextQuery(textCriteria).sortByScore();
        textQuery.with(Sort.by(desc("score")));
        textQuery.fields().include("title", "score", "genres", "fullplot").exclude("id");

        Pageable pageable = PageRequest.of(0, 5);
        textQuery.with(pageable);
        //System.out.println(explainQuery(textQuery).toJson(JsonWriterSettings.builder().indent(true).build()));
        return mongoTemplate.find(textQuery,Movies.class);

    }
    public List<Movies> getMoviesByLang(String type, String[] lang) {

        Criteria criteria;
        switch (type) {
            case "in": {
                criteria = Criteria.where("languages").in(lang); //any of match
                break;
            }
            case "all": {
                criteria = Criteria.where("languages").all(lang); //atleast match
                break;
            }
            case "is": {
                criteria = Criteria.where("languages").is(lang);   //exactly match
                break;
            }
            default: {
                System.out.println("value of type must be in/all/is.");
                return null;
            }
        }

        Query query = new Query(criteria);
        query.fields().include("title", "languages").exclude("id");
        return mongoTemplate.find(query, Movies.class);
    }

    public List<String> getDistinctValuesString(String field) {
        //List<String> listdoc = mongoTemplate.query(Movies.class).distinct(field).as(String.class).all();
        //System.out.println("Total distinct values of " + field + " field  is : " + listdoc.size());
        return mongoTemplate.findDistinct(field, Movies.class, String.class);
    }

    public List<Integer> getDistinctValuesInt(String field) {

        //List<Integer> listdoc = mongoTemplate.query(Movies.class).distinct(field).as(Integer.class).all();
        //System.out.println("Total distinct values of " + field + " field  is : " + listdoc.size());
        return mongoTemplate.findDistinct(field, Movies.class, Integer.class);
    }

    public List<IndexInfo> getListOfIndexes() {
        //Method-1
        return mongoTemplate.indexOps(Movies.class).getIndexInfo();
        //Method-2
        //return mongoTemplate.indexOps("movies").getIndexInfo();
    }

    public void setIndex(String keyField, String sortOrder) {

        IndexDefinition indexDefinition;

        if (sortOrder.equals("ASC")) {
            indexDefinition = new Index().on(keyField, Sort.Direction.ASC);
        } else {
            indexDefinition = new Index().on(keyField, Sort.Direction.DESC);
        }
        mongoTemplate.indexOps(Movies.class).ensureIndex(indexDefinition);
    }

    public void setCompoundIndex(Document listOfKeys) {
        CompoundIndexDefinition compoundIndexDefinition = new CompoundIndexDefinition(listOfKeys);
        mongoTemplate.indexOps(Movies.class).ensureIndex(compoundIndexDefinition);
    }

    public void removeIndexByIndexName(String nameOfIndex) {
        mongoTemplate.indexOps(Movies.class).dropIndex(nameOfIndex);
    }

    public List<Document> getAllCommentOfMovie(String movieTitle){

        Criteria criteria = new Criteria("awards.wins").gte(50);
        MatchOperation matchStage = Aggregation.match(criteria);
        ProjectionOperation projectStage = Aggregation.project("title","countries", "ListOfComments");
        UnwindOperation unwindStage = Aggregation.unwind("countries");
        GroupOperation groupStage = Aggregation.group("countries").addToSet("title").as("list").addToSet("ListOfComments").as("ListComments");
        LookupOperation lookupStage = Aggregation.lookup("comments","_id","movie_id", "ListOfComments");
        Aggregation agg = Aggregation.newAggregation(matchStage, lookupStage,projectStage, unwindStage, groupStage );
        AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(agg, Movies.class, Document.class);

        return aggregationResults.getMappedResults();
    }

    public List<Movies> getListOfYearDocs(Integer syear, Integer eyear)
    {
        Criteria criteria = Criteria.where("year").gte(syear).lte(eyear);
        Query query = new Query(criteria);

        return mongoTemplate.find(query,Movies.class);
    }

    //just for testing purpose
    public void tempDelete(Integer year)
    {
        System.out.println("Temporary delete document containing" + year + "value.");
    }

}

/**
 * <p>
 * 1) remove all string type value of year field from database
 * =>   Criteria c= Criteria.where("year").type(JsonSchemaObject.Type.stringType());
 * Query query=new Query(c);
 * List<Movies> ans= mongoTemplate.find(query,Movies.class);
 * <p>
 * System.out.println("total size of docs:"+ ans.size());
 * for(Movies x:ans)
 * {
 * //System.out.println(x.getId()+"        value : "+ x.getYear());
 * mongoTemplate.remove(mongoTemplate.findById(x.getId(),Movies.class));
 * }
 * //System.out.println("after..................");
 * //        count = mongoTemplate.find(c1,Movies.class).size();
 * //        System.out.println("count: "+count);
 * //
 * //        System.out.println("total size of docs:"+ ans.size());
 * return ans;
 * <p>
 * 2) count total distinct value of year field
 * =>  Method-1
 * <p>
 * List<Integer> list = new ArrayList<>();
 * DistinctIterable distinctIterable = mongoTemplate.getCollection("movies").distinct("year", Integer.class);
 * MongoCursor cursor=distinctIterable.iterator();
 * while (cursor.hasNext())
 * {
 * list.add((Integer) cursor.next());
 * }
 * <p>
 * <p>
 * Method-2
 * <p>
 * List<Integer> list = mongoTemplate.query(Movies.class).distinct("year").as(Integer.class).all();
 * <p>
 * Method-3
 * List<Integer>listdoc = mongoTemplate.findDistinct(field,Movies.class,Integer.class);
 * <p>
 * <p>
 * 3)use batchsize() to fetch all documents
 * <p>
 * 4)Make list of movie title by country name
 * notes:"here all list of titles are unique name due to use of $addToSet" so not consider
 * the case of two movies contain same names but different values in other fields.
 * <p>
 * Aggregation agg=Aggregation.newAggregation(
 * Aggregation.project("title","countries"),
 * Aggregation.unwind("countries"),
 * Aggregation.group("countries").addToSet("title").as("list")//take care of duplication
 * //Aggregation.group("countries").push("title").as("list") //not take care of duplication
 * );
 * AggregationResults<Document> ans = mongoTemplate.aggregate(agg,Movies.class,Document.class);
 * List<Document> returnDoc = ans.getMappedResults();
 * return returnDoc;
 * <p>
 * 5)search movies by country and use projection to return the title and _id field.
 * List<String> CountryList = new ArrayList<>();
 * CountryList.add("India");
 * Criteria criteria = Criteria.where("countries").in(CountryList);
 * Query query = new Query(criteria);
 * query.fields().include("title");
 * List<Movies> movieList = mongoTemplate.find(query, Movies.class);
 * <p>
 * 6)
 **/