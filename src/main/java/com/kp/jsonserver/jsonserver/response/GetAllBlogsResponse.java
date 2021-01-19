package com.kp.jsonserver.jsonserver.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.kp.jsonserver.jsonserver.models.Blog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAllBlogsResponse {
    @JsonProperty("blogs")
    private List<Blog> blogList;

    @JsonProperty("pagination_info")
    private Object paginationInfo;
}
