package com.daigo.springboot_handson_4.domains.localsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LocalSearch {
    @JsonProperty("ResultInfo")
    private ResultInfo resultInfo;
    @JsonProperty("Feature")
    private List<Feature> featureList;
}
