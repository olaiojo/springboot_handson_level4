package com.daigo.springboot_handson_4.controller;

import com.daigo.springboot_handson_4.dev.SampleLocation;
import com.daigo.springboot_handson_4.domains.ContentsGeoCoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {
    private static final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/index")
    public String index(Model model) {
        //開発用に湘南台駅の文字列を指定。
        //TODO: ユーザ入力を受け取る機能
        final SampleLocation userLocation = new SampleLocation("湘南台駅");

        //userLocationをコンテンツジオコーダAPIに渡してレスポンスをバインド
        //リクエスト先: コンテンツジオコーダAPI
        final String LOCATION = userLocation.getSAMPLE_LOCATION();
        final String REQUEST_URL = "https://map.yahooapis.jp/geocode/cont/V1/contentsGeoCoder?appid=dj00aiZpPVd0eW04MVNNRGtUbSZzPWNvbnN1bWVyc2VjcmV0Jng9YTc-&query=" + LOCATION + "&category=landmark&output=json";
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

        //Modelにadd
        model.addAttribute("userLocation", contentsGeoCoder.getFeatureList().get(0).getName()); //での検索結果
        model.addAttribute("coordinates", contentsGeoCoder.getFeatureList().get(0).getGeometry().getCoordinates()); //中心地点

        //coordinatesをローカルサーチAPIに渡してレスポンスをバインド
        //リクエスト先: ローカルサーチAPI
        //coordinatesを緯度と経度に分割 latLng[0]:経度 latLng[1]:緯度
        String[] latLng = contentsGeoCoder.getFeatureList().get(0).getGeometry().getCoordinates().split(",",0);
        //中心(latLng)からの距離(km)を指定
        String dist = "1";
        //リクエスト用URL
        final String REQUEST_URL_2 = "https://map.yahooapis.jp/search/local/V1/localSearch?appid=dj00aiZpPVd0eW04MVNNRGtUbSZzPWNvbnN1bWVyc2VjcmV0Jng9YTc-&output=json&results=10&gc=0115001&lat=" + latLng[1] + "&lng=" + latLng[0] + "&dist=" + dist;
        //GET
        String response = restTemplate.getForObject(REQUEST_URL_2, String.class);
        //TODO: レスポンス用のクラスを作成してバインド

        //Modelにadd
        model.addAttribute("cafes", response);

        return "index";
    }
}
