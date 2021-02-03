package com.daigo.springboot_handson_4.domains.geocoder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Geometry {
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Coordinates")
    private String coordinates;
}
