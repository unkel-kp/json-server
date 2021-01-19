package com.kp.jsonserver.jsonserver.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BlogRequestBody{

    @NotNull(message = "Please provide the title of the blog")
    @JsonProperty("title")
    private String title;

    @NotNull(message = "Please provide the author_id of the blog")
    @JsonProperty("author_id")
    private Integer authorId;

    @NotNull(message = "Please provide the read_time of the blog")
    @JsonProperty("read_time")
    private Integer readTime;

    @NotNull(message = "Please provide the reads of the blog")
    @JsonProperty("reads")
    private Integer reads;

    @NotNull(message = "Please provide the reviews of the blog")
    @JsonProperty("reviews")
    private Integer reviews;
}
