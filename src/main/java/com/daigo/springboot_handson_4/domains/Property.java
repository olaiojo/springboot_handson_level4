package com.daigo.springboot_handson_4.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Property {
    private String Genre;
    private String Query;
    private String Address;
    private String AddressKana;
    private List<Element> AddressElement;
}
