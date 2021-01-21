package com.kp.jsonserver.jsonserver.service;


import com.kp.jsonserver.jsonserver.models.Author;
import com.kp.jsonserver.jsonserver.models.Blog;
import com.kp.jsonserver.jsonserver.models.Database;
import com.kp.jsonserver.jsonserver.models.MetaData;
import com.kp.jsonserver.jsonserver.provider.DatabaseManager;
import com.kp.jsonserver.jsonserver.request.AuthorRequestBody;
import com.kp.jsonserver.jsonserver.response.GetAllAuthorsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class AuthorService {

    @Autowired
    private DatabaseManager databaseManager;

    private Boolean isAgeInRange(Integer age, List<Integer> ageRange){
        if(age >= ageRange.get(0) && age <= ageRange.get(1))
            return true;
        return false;
    }

    // Filtering the Data set basis of firstName, lastName, email and ageRange
    private List<Author> filter(List<Author> authors, String firstName, String lastName, String email, List<Integer> ageRange){
        List<Author> result = new ArrayList<>();

        if(!StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName) && StringUtils.isEmpty(email)){
            authors.forEach(author -> {
                if(author.getFirstName().equalsIgnoreCase(firstName) && isAgeInRange(author.getAge(), ageRange))
                    result.add(author);
            });
        }
        else if(!StringUtils.isEmpty(lastName) && StringUtils.isEmpty(firstName) && StringUtils.isEmpty(email)){
            authors.forEach(author -> {
                if(author.getLastName().equalsIgnoreCase(lastName) && isAgeInRange(author.getAge(), ageRange))
                    result.add(author);
            });
        }
        else if(!StringUtils.isEmpty(email) && StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName)){
            authors.forEach(author -> {
                if(author.getEmail().equalsIgnoreCase(email) && isAgeInRange(author.getAge(), ageRange))
                    result.add(author);
            });
        }
        else if(!StringUtils.isEmpty(email) && !StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName)){
            authors.forEach(author -> {
                if(author.getEmail().equalsIgnoreCase(email) && author.getFirstName().equalsIgnoreCase(firstName) && isAgeInRange(author.getAge(), ageRange))
                    result.add(author);
            });
        }
        else if(!StringUtils.isEmpty(email) && StringUtils.isEmpty(firstName) && !StringUtils.isEmpty(lastName)){
            authors.forEach(author -> {
                if(author.getEmail().equalsIgnoreCase(email) && author.getLastName().equalsIgnoreCase(lastName) && isAgeInRange(author.getAge(), ageRange))
                    result.add(author);
            });
        }
        else if(StringUtils.isEmpty(email) && !StringUtils.isEmpty(firstName) && !StringUtils.isEmpty(lastName)){
            authors.forEach(author -> {
                if(author.getFirstName().equalsIgnoreCase(firstName) && author.getLastName().equalsIgnoreCase(lastName) && isAgeInRange(author.getAge(), ageRange))
                    result.add(author);
            });
        }
        else if(!StringUtils.isEmpty(email) && !StringUtils.isEmpty(firstName) && !StringUtils.isEmpty(lastName)){
            authors.forEach(author -> {
                if(author.getEmail().equalsIgnoreCase(email) && author.getFirstName().equalsIgnoreCase(firstName) && author.getLastName().equalsIgnoreCase(lastName) && isAgeInRange(author.getAge(), ageRange))
                    result.add(author);
            });
        }
        else{
            authors.forEach(author -> {
                if(isAgeInRange(author.getAge(), ageRange))
                    result.add(author);
            });
        }
        return result;
    }

    // Ordering the Data set basis of all the properties in asc or desc
    private List<Author> order(List<Author> authors, String sort, String order){
        Collections.sort(authors, new Comparator<Author>() {
            @Override
            public int compare(Author a1, Author a2) {
                if (order.equals("desc")) {
                    if (sort.equals("first_name"))
                        return a2.getFirstName().compareToIgnoreCase(a1.getFirstName());
                    else if (sort.equals("last_name"))
                        return a2.getLastName().compareToIgnoreCase(a1.getLastName());
                    else if (sort.equals("age"))
                        return a2.getAge().compareTo(a1.getAge());
                    else if (sort.equals("email"))
                        return a2.getEmail().compareToIgnoreCase(a1.getEmail());
                    else if (sort.equals("blogs"))
                        return a2.getBlogs().compareTo(a1.getBlogs());
                    else
                        return a2.getId().compareTo(a1.getId());
                } else {
                    if (sort.equals("first_name"))
                        return a1.getFirstName().compareToIgnoreCase(a2.getFirstName());
                    else if (sort.equals("last_name"))
                        return a1.getLastName().compareToIgnoreCase(a2.getLastName());
                    else if (sort.equals("age"))
                        return a1.getAge().compareTo(a2.getAge());
                    else if (sort.equals("email"))
                        return a1.getEmail().compareToIgnoreCase(a2.getEmail());
                    else if (sort.equals("blogs"))
                        return a1.getBlogs().compareTo(a2.getBlogs());
                    else
                        return a1.getId().compareTo(a2.getId());
                }
            }
        });
        return authors;
    }

    // Paginating the data set
    private GetAllAuthorsResponse paginate(List<Author> authors, String page, String limit){
        Map<String, Integer> paginationInfo = new HashMap<>();
        int p = Integer.parseInt(page), l = Integer.parseInt(limit), s = authors.size();
        int beg = (p-1)*l;
        int end = p*l>s ? s : p*l;
        double t = (double) s/l;
        List<Author> response = authors.subList(beg, end);
        paginationInfo.put("current_page", p);
        paginationInfo.put("total_page", (int)Math.ceil(t));
        paginationInfo.put("page_records", response.size());
        paginationInfo.put("total_records", s);
        return new GetAllAuthorsResponse(response, paginationInfo);
    }




    public GetAllAuthorsResponse getAllAuthors(String first_name, String last_name, String email, List<Integer> age_range,
                                           String page, String limit,
                                           String sort, String order) throws Exception {
        try{
            Database dbSnapShot = databaseManager.getData();
            List<Author> authors = dbSnapShot.getAuthors();
            List<Author> results = filter(authors, first_name, last_name, email, age_range);
            results = order(results, sort, order);
            GetAllAuthorsResponse response = paginate(results, page, limit);
            return response;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    public Author getAuthorById(String id) throws Exception {
        try {
            Database dbSnapShot = databaseManager.getData();
            List<Author> authors = dbSnapShot.getAuthors();
            for(Author author: authors){
                if(Integer.valueOf(id) == author.getId())
                    return author;
            }
            throw new Exception("NO AUTHOR FOUND! Make sure you're providing the correct id");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    public Author addAuthor(AuthorRequestBody requestBody) throws Exception {
        try{
            Database dbSnapShot = databaseManager.getData();
            List<Author> authors = dbSnapShot.getAuthors();
            MetaData metaData = dbSnapShot.getMetaData();
            Author author = new Author(metaData.getAuthorsId()+1,
                    requestBody.getFirstName(), requestBody.getLastName(),
                    requestBody.getAge(), requestBody.getEmail(),
                    requestBody.getBlogs(), String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()));
            authors.add(author);
            metaData.setAuthorsId(metaData.getAuthorsId()+1);
            metaData.setModifiedAt(String.valueOf(System.currentTimeMillis()));
            dbSnapShot.setAuthors(authors);
            dbSnapShot.setMetaData(metaData);
            databaseManager.updateData(dbSnapShot);
            return author;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    public Author updateAuthor(String id, AuthorRequestBody requestBody) throws Exception{
        try {
            Database dbSnapShot = databaseManager.getData();
            List<Author> authors = dbSnapShot.getAuthors();
            MetaData metaData = dbSnapShot.getMetaData();
            Author result = null;
            for(Author author: authors){
                if(Integer.valueOf(id) == author.getId())
                {
                    result = author;
                    break;
                }
            }
            if(result == null){
                throw new Exception("NO AUTHOR FOUND! Make sure you're providing the correct id");
            }
            else {
                result.setFirstName(requestBody.getFirstName());
                result.setLastName(requestBody.getLastName());
                result.setAge(requestBody.getAge());
                result.setEmail(requestBody.getEmail());
                result.setBlogs(requestBody.getBlogs());
                result.setModifiedAt(String.valueOf(System.currentTimeMillis()));
                metaData.setModifiedAt(String.valueOf(System.currentTimeMillis()));
                dbSnapShot.setAuthors(authors);
                dbSnapShot.setMetaData(metaData);
                databaseManager.updateData(dbSnapShot);
                return result;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    public Author deleteAuthor(String id) throws Exception {
        try{
            if(id.equals("7")){
                throw new Exception("PERMISSION DENIED! You're trying to delete the master author!");
            }
            Database dbSnapShot = databaseManager.getData();
            List<Author> authors = dbSnapShot.getAuthors();
            List<Blog> blogs = dbSnapShot.getBlogs();
            MetaData metaData = dbSnapShot.getMetaData();
            Author result = null;
            for(Author author: authors){
                if(Integer.valueOf(id) == author.getId())
                {
                    result = author;
                    break;
                }
            }
            if(result == null){
                throw new Exception("NO AUTHOR FOUND! Make sure you're providing the correct id");
            }
            else{
                for(Blog blog: blogs){
                    if(blog.getAuthorId()==result.getId())
                        blog.setAuthorId(7);
                }
                authors.remove(result);
                metaData.setModifiedAt(String.valueOf(System.currentTimeMillis()));
                dbSnapShot.setBlogs(blogs);
                dbSnapShot.setAuthors(authors);
                dbSnapShot.setMetaData(metaData);
                databaseManager.updateData(dbSnapShot);
                return result;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
