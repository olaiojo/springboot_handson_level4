package com.daigo.springboot_handson_4.cafedomains;

import com.daigo.springboot_handson_4.domains.Geometry;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Feature {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Gid")
    private String gid;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Geometry")
    private Geometry geometry;
    @JsonProperty("Category")
    private List<String> categoryList;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Style")
    private List<String> style;
    @JsonProperty("Property")
    private Property property;
}
