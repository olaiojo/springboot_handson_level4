package com.daigo.springboot_handson_4.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Feature {
    private Integer Id;
    private String Name;
    private String Description;
    private Geometry Geometry;
    private Property Property;
}
