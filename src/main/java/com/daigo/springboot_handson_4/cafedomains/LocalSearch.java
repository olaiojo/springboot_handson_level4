package com.daigo.springboot_handson_4.cafedomains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class LocalSearch {
    @JsonProperty("ResultInfo")
    private ResultInfo resultInfo;
    @JsonProperty("Feature")
    private List<Feature> features_list;
}
