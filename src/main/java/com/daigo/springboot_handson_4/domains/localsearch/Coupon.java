package com.daigo.springboot_handson_4.domains.localsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Coupon {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("PcUrl")
    private String pcUrl;
    @JsonProperty("MobileFlag")
    private String mobileFlag;
    @JsonProperty("SmartPhoneUrl")
    private String smartPhoneUrl;
    @JsonProperty("MobileUrl")
    private String mobileUrl;
}
