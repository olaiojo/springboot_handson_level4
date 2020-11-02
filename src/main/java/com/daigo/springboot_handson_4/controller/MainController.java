package com.daigo.springboot_handson_4.controller;

import com.daigo.springboot_handson_4.cafedomains.LocalSearch;
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
        /**
         * Setting
         */
        //appid
        final String APPID = "dj00aiZpPVd0eW04MVNNRGtUbSZzPWNvbnN1bWVyc2VjcmV0Jng9YTc-";
        //出力形式
        final String OUTPUT = "json";

        /**
         * 駅名の指定を行う機能
         */
        //TODO: ユーザ入力を受け取る機能
        //開発用に湘南台駅の文字列を指定。
        final SampleLocation userLocation = new SampleLocation("湘南台駅");

        /**
         * userLocationをコンテンツジオコーダAPIに渡してレスポンスを受け取る機能
         */
        //クエリとなるロケーション
        final String LOCATION = userLocation.getSAMPLE_LOCATION();
        //カテゴリ
        final String CATEGORY = "landmark";
        //リクエストURLの合成
        final String REQUEST_URL = "https://map.yahooapis.jp/geocode/cont/V1/contentsGeoCoder"
                + "?appid=" + APPID
                + "&query=" + LOCATION
                + "&category=" + CATEGORY
                + "&output=" + OUTPUT;
        //GETしてContentsGeoCoderクラスへのバインドをtry
        ContentsGeoCoder contentsGeoCoder = new ContentsGeoCoder();
        try {
            contentsGeoCoder = restTemplate.getForObject(REQUEST_URL, ContentsGeoCoder.class);
        } catch (HttpClientErrorException e) {
            System.out.println("|||||||||| Error 4XX ||||||||||");
            throw e;
        } catch (HttpServerErrorException e) {
            System.out.println("|||||||||| Error 5XX ||||||||||");
            throw e;
        }

        /**
         * coordinatesをローカルサーチAPIに渡してレスポンスを受け取る機能
         */
        //coordinatesを緯度と経度に分割 latLng[0]:経度 latLng[1]:緯度
        final String[] LATLON = contentsGeoCoder.getFeatureList().get(0).getGeometry().getCoordinates().split(",", 0);
        //中心(latLng)からの検索距離(km)
        final String DIST = "3";
        //取得件数
        final Integer Results = 10;
        //業種コード(GC)
        final String GC = "0115001";
        //リクエスト用URLの合成
        final String REQUEST_URL_2 = "https://map.yahooapis.jp/search/local/V1/localSearch"
                + "?appid=" + APPID
                + "&output=" + OUTPUT
                + "&results=" + Results
                + "&gc=" + GC
                + "&lat=" + LATLON[1]
                + "&lon=" + LATLON[0]
                + "&dist=" + DIST;
        System.out.println(REQUEST_URL_2);
        //GETしてLocalSearchクラスへのバインドをtry
        LocalSearch localSearch = new LocalSearch();
        try {
            localSearch = restTemplate.getForObject(REQUEST_URL_2, LocalSearch.class);
        } catch (HttpClientErrorException e) {
            System.out.println("|||||||||| Error 4XX ||||||||||");
            throw e;
        } catch (HttpServerErrorException e) {
            System.out.println("|||||||||| Error 5XX ||||||||||");
            throw e;
        }

        /**
         * Modelにadd
         */
        model.addAttribute("userLocation", contentsGeoCoder.getFeatureList().get(0).getName()); //での検索結果
        model.addAttribute("coordinates", contentsGeoCoder.getFeatureList().get(0).getGeometry().getCoordinates()); //中心地点
        model.addAttribute("resultsNumber", localSearch.getResultInfo().getCount()); //検索件数
        model.addAttribute("features", localSearch.getFeatures_list()); //検索結果

        return "index";
    }
}
