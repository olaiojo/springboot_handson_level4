package com.daigo.springboot_handson_4.domains;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class ResultInfo {
    private Integer count;
    private Integer total;
    private Integer start;
    private Double latency;
    private Integer status;
    private String description;
    private String copyright;
    private String compressType;
}
