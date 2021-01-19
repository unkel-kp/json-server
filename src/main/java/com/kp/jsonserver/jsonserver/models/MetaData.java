package com.kp.jsonserver.jsonserver.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetaData {
    @JsonProperty("blogs_id")
    private Integer blogsId;

    @JsonProperty("authors_id")
    private Integer authorsId;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("modified_at")
    private String modifiedAt;

}
