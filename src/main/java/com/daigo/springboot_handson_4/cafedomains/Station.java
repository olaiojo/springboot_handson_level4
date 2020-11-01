package com.daigo.springboot_handson_4.cafedomains;

import com.daigo.springboot_handson_4.domains.Geometry;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Station {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("SubId")
    private String subId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Railway")
    private String railway;
    @JsonProperty("Exit")
    private String exit;
    @JsonProperty("ExitId")
    private String exitId;
    @JsonProperty("Distance")
    private String distance;
    @JsonProperty("Time")
    private String time;
    //domainsのGeometryを流用
    @JsonProperty("Geometry")
    private Geometry geometry;
}
