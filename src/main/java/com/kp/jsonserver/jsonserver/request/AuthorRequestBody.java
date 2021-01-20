package com.kp.jsonserver.jsonserver.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AuthorRequestBody{

    @NotNull(message = "Please provide the first_name of the author")
    @JsonProperty("first_name")
    private String firstName;

    @NotNull(message = "Please provide the last_name of the author")
    @JsonProperty("last_name")
    private String lastName;

    @NotNull(message = "Please provide the age of the author")
    @Min(5)
    @Max(150)
    @JsonProperty("age")
    private Integer age;

    @Email
    @NotNull(message = "Please provide the email of the author")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "Please provide the blogs of the author")
    @JsonProperty("blogs")
    private Integer blogs;
}
