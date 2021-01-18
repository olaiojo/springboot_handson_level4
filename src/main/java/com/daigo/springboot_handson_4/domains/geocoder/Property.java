package com.daigo.springboot_handson_4.domains.geocoder;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Property {
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("Query")
    private String query;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("AddressKana")
    private String addressKana;
    @JsonProperty("AddressElement")
    private List<Element> addressElementList;
}
