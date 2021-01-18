package com.daigo.springboot_handson_4.domains.geocoder;

import com.daigo.springboot_handson_4.domains.yahooapi.ResultInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContentsGeoCoder {
    @JsonProperty("ResultInfo")
    private ResultInfo resultInfo;
    @JsonProperty("Feature")
    private List<Feature> featureList;
}

