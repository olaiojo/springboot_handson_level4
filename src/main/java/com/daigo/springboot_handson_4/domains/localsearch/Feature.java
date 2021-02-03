package com.daigo.springboot_handson_4.domains.localsearch;

import com.daigo.springboot_handson_4.domains.geocoder.Geometry;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<String> styleList;
    @JsonProperty("Property")
    private Property property;
}
