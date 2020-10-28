package com.daigo.springboot_handson_4.controller;

import com.daigo.springboot_handson_4.dev.SampleLocation;
import com.daigo.springboot_handson_4.domains.ContentsGeoCoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
public class MainController {
    private static final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/index")
    public String index(Model model) {
        //開発用に湘南台駅の文字列を指定。
        final SampleLocation userLocation = new SampleLocation("湘南台駅");
        model.addAttribute("userLocation", userLocation.getSAMPLE_LOCATION());

        //TODO: userLocationをコンテンツジオコーダAPIに渡してレスポンスをバインド
        //リクエスト先URL: コンテンツジオコーダAPI
        final String REQUEST_URL = "https://map.yahooapis.jp/geocode/cont/V1/contentsGeoCoder?appid=dj00aiZpPVd0eW04MVNNRGtUbSZzPWNvbnN1bWVyc2VjcmV0Jng9YTc-&query=湘南台駅&category=address&output=json";
        //ContentsGeoCoderクラスへのバインドをtry
        ContentsGeoCoder contentsGeoCoder = new ContentsGeoCoder();
        try {
            contentsGeoCoder = restTemplate.getForObject(REQUEST_URL, ContentsGeoCoder.class);
        }catch (HttpClientErrorException e) {
            System.out.println("|||||||||| Error 4XX ||||||||||");
            throw e;
        }catch (HttpServerErrorException e){
            System.out.println("|||||||||| Error 5XX ||||||||||");
            throw e;
        }

        System.out.println("==========debug zone===========");
        System.out.println(REQUEST_URL);
        System.out.println("contentsGeoCoder nonNull? : " + Objects.nonNull(contentsGeoCoder)); //true
        System.out.println("ResultInfo nonNull? : " + Objects.nonNull(contentsGeoCoder.getResultInfo())); // false
        System.out.println("Feature nonNull? : " + Objects.nonNull(contentsGeoCoder.getFeature())); // false
        System.out.println("Request Response" + restTemplate.getForObject(REQUEST_URL, String.class));
        System.out.println("==========/debug zone===========");

        return "index";
    }
}
