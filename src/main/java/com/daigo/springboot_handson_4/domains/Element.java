package com.daigo.springboot_handson_4.domains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Element {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Kana")
    private String kana;
}
