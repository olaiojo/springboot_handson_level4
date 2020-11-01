package com.daigo.springboot_handson_4.cafedomains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Property {
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("CassetteId")
    private String cassetteId;
    @JsonProperty("Yomi")
    private String yomi;
    @JsonProperty("Country")
    private Country country;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("GovernmentCode")
    private String governmentCode;
    @JsonProperty("AddressMatchingLevel")
    private String addressMatchingLevel;
    @JsonProperty("Tel1")
    private String tel1;
    @JsonProperty("Genre")
    private List<Genre> genreList;
    @JsonProperty("Station")
    private List<Station> stationList;
    @JsonProperty("LeadImage")
    private String leadImage;
    //フラグ類は実質booleanだがダブルクオートで囲まれている
    @JsonProperty("ParkingFlag")
    private String parkingFlag;
    @JsonProperty("CouponFlag")
    private String couponFlag;
    @JsonProperty("SmartPhoneCouponFlag")
    private String smartPhoneCouponFlag;
    @JsonProperty("Coupon")
    private List<Coupon> couponList;
    @JsonProperty("KeepCount")
    private String keepCount;
}
