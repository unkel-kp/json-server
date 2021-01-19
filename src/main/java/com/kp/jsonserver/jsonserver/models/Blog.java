package com.kp.jsonserver.jsonserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Blog {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author_id")
    private Integer authorId;

    @JsonProperty("read_time")
    private Integer readTime;

    @JsonProperty("reads")
    private Integer reads;

    @JsonProperty("reviews")
    private Integer reviews;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("modified_at")
    private String modifiedAt;
}
