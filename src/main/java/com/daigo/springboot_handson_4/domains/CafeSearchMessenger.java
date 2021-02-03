package com.daigo.springboot_handson_4.domains;

import com.daigo.springboot_handson_4.domains.localsearch.Feature;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CafeSearchMessenger {
    private String userLocation;
    private String message;
    private String resultsNumber;
    private String coordinates;
    private List<Feature> features;
}
