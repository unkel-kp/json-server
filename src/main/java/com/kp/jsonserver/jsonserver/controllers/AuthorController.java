package com.kp.jsonserver.jsonserver.controllers;


import com.kp.jsonserver.jsonserver.request.AuthorRequestBody;
import com.kp.jsonserver.jsonserver.response.ServiceResponse;
import com.kp.jsonserver.jsonserver.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping(value = "/all")
    public ServiceResponse<?> getAllAuthors(
            @RequestParam(value = "first_name", required = false) String firstName,
            @RequestParam(value = "last_name", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "age_range", required = false) List<Integer> ageRange,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") String limit,
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order){
        try {
            if(CollectionUtils.isEmpty(ageRange)){
                List<Integer> temp = new ArrayList<>();
                temp.add(5);
                temp.add(150);
                ageRange=temp;
            } else {
                if(ageRange.size()==1){
                    ageRange.add(150);
                }
            }
            return new ServiceResponse<>(authorService.getAllAuthors(firstName, lastName, email, ageRange, page, limit, sort, order), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/{id}")
    public ServiceResponse<?> getAuthorById(@PathVariable String id){
        try{
            return new ServiceResponse<>(authorService.getAuthorById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/add")
    public ServiceResponse<?> addAuthor(@Valid @NotNull @RequestBody AuthorRequestBody req){
        try{
            return new ServiceResponse<>(authorService.addAuthor(req), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "{id}/update")
    public ServiceResponse<?> updateAuthor(@PathVariable String id,
                                         @Valid @NotNull @RequestBody AuthorRequestBody req){
        try{
            return new ServiceResponse<>(authorService.updateAuthor(id, req), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "{id}/delete")
    public ServiceResponse<?> deleteAuthor(@PathVariable String id){
        try{
            return new ServiceResponse<>(authorService.deleteAuthor(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
