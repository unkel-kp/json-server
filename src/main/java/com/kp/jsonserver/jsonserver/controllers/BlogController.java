package com.kp.jsonserver.jsonserver.controllers;


import com.kp.jsonserver.jsonserver.request.BlogRequestBody;
import com.kp.jsonserver.jsonserver.response.ServiceResponse;
import com.kp.jsonserver.jsonserver.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping(value = "/all")
    public ServiceResponse<?> getAllBlogs(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author_id", required = false) String authorId,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") String limit,
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order){
        try {
            return new ServiceResponse<>(blogService.getAllBlogs(title, authorId, page, limit, sort, order), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/{id}")
    public ServiceResponse<?> getBlogById(@PathVariable String id){
        try{
            return new ServiceResponse<>(blogService.getBlogById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/add")
    public ServiceResponse<?> addBlog(@Valid @NotNull @RequestBody BlogRequestBody req){
        try{
            return new ServiceResponse<>(blogService.addBlog(req), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "{id}/update")
    public ServiceResponse<?> updateBlog(@PathVariable String id,
                                      @Valid @NotNull @RequestBody BlogRequestBody req){
        try{
            return new ServiceResponse<>(blogService.updateBlog(id, req), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "{id}/delete")
    public ServiceResponse<?> updateBlog(@PathVariable String id){
        try{
            return new ServiceResponse<>(blogService.deleteBlog(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
