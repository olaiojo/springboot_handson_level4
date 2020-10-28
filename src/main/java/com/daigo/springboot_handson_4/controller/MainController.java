package com.daigo.springboot_handson_4.controller;

import com.daigo.springboot_handson_4.dev.SampleLocation;
import com.daigo.springboot_handson_4.domains.ContentsGeoCoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {
    private static final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/index")
    public String index(Model model) {
        //開発用に湘南台駅の文字列を指定。
        final SampleLocation userLocation = new SampleLocation("湘南台駅");

        //TODO: userLocationをコンテンツジオコーダAPIに渡してレスポンスをバインド
        String REQUEST_URL = "https://map.yahooapis.jp/geocode/cont/V1/contentsGeoCoder?appid=dj00aiZpPVd0eW04MVNNRGtUbSZzPWNvbnN1bWVyc2VjcmV0Jng9YTc-&query=湘南台駅&category=address&output=json";
        final ContentsGeoCoder contentsGeoCoder = restTemplate.getForObject(REQUEST_URL, ContentsGeoCoder.class);

        System.out.println("==========debug zone===========");
        System.out.println(REQUEST_URL);
        System.out.println("==========/debug zone===========");

        model.addAttribute("userLocation", userLocation.getSAMPLE_LOCATION());

        return "index";
    }
}
