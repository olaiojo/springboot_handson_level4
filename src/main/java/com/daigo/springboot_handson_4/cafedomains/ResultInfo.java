package com.daigo.springboot_handson_4.cafedomains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResultInfo {
    @JsonProperty("Count")
    private Integer count;
    @JsonProperty("Total")
    private Integer total;
    @JsonProperty("Start")
    private Integer start;
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Copyright")
    private String copyright;
    @JsonProperty("Latency")
    private Double latency;
}
