package com.daigo.springboot_handson_4.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ContentsGeoCoder {
    private ResultInfo ResultInfo;
    private List<Feature> Feature;
}
