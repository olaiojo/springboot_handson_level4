package com.daigo.springboot_handson_4.domains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ContentsGeoCoder {
    @JsonProperty("ResultInfo")
    private ResultInfo resultInfo;
    @JsonProperty("Feature")
    private List<Feature> featureList;
}

