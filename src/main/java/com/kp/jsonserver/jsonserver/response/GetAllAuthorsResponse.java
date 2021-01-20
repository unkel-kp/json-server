package com.kp.jsonserver.jsonserver.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.kp.jsonserver.jsonserver.models.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAllAuthorsResponse {
    @JsonProperty("authors")
    private List<Author> authorList;

    @JsonProperty("pagination_info")
    private Object paginationInfo;
}
