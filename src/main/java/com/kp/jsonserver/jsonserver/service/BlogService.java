package com.kp.jsonserver.jsonserver.service;


import com.kp.jsonserver.jsonserver.models.Author;
import com.kp.jsonserver.jsonserver.models.Blog;
import com.kp.jsonserver.jsonserver.models.Database;
import com.kp.jsonserver.jsonserver.models.MetaData;
import com.kp.jsonserver.jsonserver.provider.DatabaseManager;
import com.kp.jsonserver.jsonserver.request.BlogRequestBody;
import com.kp.jsonserver.jsonserver.response.GetAllBlogsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class BlogService {

    @Autowired
    private DatabaseManager databaseManager;

    @Autowired
    private AuthorService authorService;

    // Filtering the Data set basis of title or author_id
    private List<Blog> filter(List<Blog> blogs, String title, String author_id){
        List<Blog> result = new ArrayList<>();

        if(!StringUtils.isEmpty(title) && StringUtils.isEmpty(author_id)){
            blogs.forEach(blog -> {
                if(blog.getTitle().equalsIgnoreCase(title))
                    result.add(blog);
            });
        }
        else if(!StringUtils.isEmpty(author_id) && StringUtils.isEmpty(title)){
            blogs.forEach(blog -> {
                if(author_id.equals(String.valueOf(blog.getAuthorId())))
                    result.add(blog);
            });
        }
        else if(!StringUtils.isEmpty(title) && !StringUtils.isEmpty(author_id)){
            blogs.forEach(blog -> {
                if(blog.getTitle().equalsIgnoreCase(title) && author_id.equals(String.valueOf(blog.getAuthorId())))
                    result.add(blog);
            });
        }
        else{
            blogs.forEach(blog -> {
                result.add(blog);
            });
        }
        return result;
    }

    // Ordering the Data set basis of all the properties in asc or desc
    private List<Blog> order(List<Blog> blogs, String sort, String order){
        Collections.sort(blogs, new Comparator<Blog>() {
            @Override
            public int compare(Blog b1, Blog b2) {
                if (order.equals("desc")) {
                    if (sort.equals("title"))
                        return b2.getTitle().compareToIgnoreCase(b1.getTitle());
                    else if (sort.equals("author_id"))
                        return b2.getAuthorId().compareTo(b1.getAuthorId());
                    else if (sort.equals("read_time"))
                        return b2.getReadTime().compareTo(b1.getReadTime());
                    else if (sort.equals("reads"))
                        return b2.getReads().compareTo(b1.getReads());
                    else if (sort.equals("reviews"))
                        return b2.getReviews().compareTo(b1.getReviews());
                    else
                        return b2.getId().compareTo(b1.getId());
                } else {
                    if (sort.equals("title"))
                        return b1.getTitle().compareToIgnoreCase(b2.getTitle());
                    else if (sort.equals("author_id"))
                        return b1.getAuthorId().compareTo(b2.getAuthorId());
                    else if (sort.equals("read_time"))
                        return b1.getReadTime().compareTo(b2.getReadTime());
                    else if (sort.equals("reads"))
                        return b1.getReads().compareTo(b2.getReads());
                    else if (sort.equals("reviews"))
                        return b1.getReviews().compareTo(b2.getReviews());
                    else
                        return b1.getId().compareTo(b2.getId());
                }
            }
        });
        return blogs;
    }

    // Paginating the data set
    private GetAllBlogsResponse paginate(List<Blog> blogs, String page, String limit){
        Map<String, Integer> paginationInfo = new HashMap<>();
        int p = Integer.parseInt(page), l = Integer.parseInt(limit), s = blogs.size();
        int beg = (p-1)*l;
        int end = p*l>s ? s : p*l;
        double t = (double) s/l;
        List<Blog> response = blogs.subList(beg, end);
        paginationInfo.put("current_page", p);
        paginationInfo.put("total_page", (int)Math.ceil(t));
        paginationInfo.put("page_records", response.size());
        paginationInfo.put("total_records", s);
        return new GetAllBlogsResponse(response, paginationInfo);
    }




    public GetAllBlogsResponse getAllBlogs(String title, String author_id,
                                  String page, String limit,
                                  String sort, String order) throws Exception {
        try{
            Database dbSnapShot = databaseManager.getData();
            List<Blog> blogs = dbSnapShot.getBlogs();
            List<Blog> results = filter(blogs, title, author_id);
            results = order(results, sort, order);
            GetAllBlogsResponse response = paginate(results, page, limit);
            return response;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    public Blog getBlogById(String id) throws Exception {
        try {
            Database dbSnapShot = databaseManager.getData();
            List<Blog> blogs = dbSnapShot.getBlogs();
            for(Blog blog: blogs){
                if(Integer.valueOf(id) == blog.getId())
                    return blog;
            }
            throw new Exception("NO BLOG FOUND! Make sure you're providing the correct id");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    public Blog addBlog(BlogRequestBody requestBody) throws Exception {
        try{
            Database dbSnapShot = databaseManager.getData();
            List<Blog> blogs = dbSnapShot.getBlogs();
            MetaData metaData = dbSnapShot.getMetaData();
            Author author = authorService.getAuthorById(String.valueOf(requestBody.getAuthorId()));
            Blog blog = new Blog(metaData.getBlogsId()+1,
                    requestBody.getTitle(), requestBody.getAuthorId(),
                    requestBody.getReadTime(), requestBody.getReads(),
                    requestBody.getReviews(), String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()));
            blogs.add(blog);
            metaData.setBlogsId(metaData.getBlogsId()+1);
            metaData.setModifiedAt(String.valueOf(System.currentTimeMillis()));
            dbSnapShot.setBlogs(blogs);
            dbSnapShot.setMetaData(metaData);
            databaseManager.updateData(dbSnapShot);
            return blog;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    public Blog updateBlog(String id, BlogRequestBody requestBody) throws Exception{
        try {
            Database dbSnapShot = databaseManager.getData();
            List<Blog> blogs = dbSnapShot.getBlogs();
            MetaData metaData = dbSnapShot.getMetaData();
            Blog result = null;
            for(Blog blog: blogs){
                if(Integer.valueOf(id) == blog.getId())
                {
                    result = blog;
                    break;
                }
            }
            if(result == null){
                throw new Exception("NO BLOG FOUND! Make sure you're providing the correct id");
            }
            else {
                authorService.getAuthorById(String.valueOf(requestBody.getAuthorId()));
                result.setTitle(requestBody.getTitle());
                result.setAuthorId(requestBody.getAuthorId());
                result.setReadTime(requestBody.getReadTime());
                result.setReads(requestBody.getReads());
                result.setReviews(requestBody.getReviews());
                result.setModifiedAt(String.valueOf(System.currentTimeMillis()));
                metaData.setModifiedAt(String.valueOf(System.currentTimeMillis()));
                dbSnapShot.setBlogs(blogs);
                dbSnapShot.setMetaData(metaData);
                databaseManager.updateData(dbSnapShot);
                return result;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    public Blog deleteBlog(String id) throws Exception {
        try{
            Database dbSnapShot = databaseManager.getData();
            List<Blog> blogs = dbSnapShot.getBlogs();
            MetaData metaData = dbSnapShot.getMetaData();
            Blog result = null;
            for(Blog blog: blogs){
                if(Integer.valueOf(id) == blog.getId())
                {
                    result = blog;
                    break;
                }
            }
            if(result == null){
                throw new Exception("NO BLOG FOUND! Make sure you're providing the correct id");
            }
            else{
                blogs.remove(result);
                metaData.setModifiedAt(String.valueOf(System.currentTimeMillis()));
                dbSnapShot.setBlogs(blogs);
                dbSnapShot.setMetaData(metaData);
                databaseManager.updateData(dbSnapShot);
                return result;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
