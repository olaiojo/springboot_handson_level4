package com.daigo.springboot_handson_4.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResultInfo {
    private Integer Count;
    private Integer Total;
    private Integer Start;
    private Double Latency;
    private Integer Status;
    private String Description;
    private String Copyright;
    private String CompressType;
}
