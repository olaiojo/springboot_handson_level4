package com.daigo.springboot_handson_4.domains.localsearch;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class ResultInfo {
    private Integer count;
    private Integer total;
    private Integer start;
    private Integer status;
    private String description;
    private String copyright;
    private Double latency;
}
