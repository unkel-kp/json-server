package com.kp.jsonserver.jsonserver.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Data
@Getter
@Setter
public class Database {
    @JsonProperty("meta-data")
    private MetaData metaData;

    @JsonProperty("blogs")
    private List<Blog> blogs;

    @JsonProperty("authors")
    private List<Author> authors;
}
