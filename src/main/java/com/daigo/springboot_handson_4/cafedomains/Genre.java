package com.daigo.springboot_handson_4.cafedomains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Genre {
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Name")
    private String name;
}
