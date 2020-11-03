package com.daigo.springboot_handson_4.controller;

import com.daigo.springboot_handson_4.cafedomains.LocalSearch;
import com.daigo.springboot_handson_4.domains.ContentsGeoCoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {
    private static final RestTemplate restTemplate = new RestTemplate();
    /**
     * Setting
     */
    //appid
    final String APPID = "dj00aiZpPVd0eW04MVNNRGtUbSZzPWNvbnN1bWVyc2VjcmV0Jng9YTc-";
    //出力形式
    final String OUTPUT = "json";

    /**
     * indexにリクエストがあったときのマッピングを行うメソッド
     * @param model
     * @return "index"
     */
    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }

    /**
     * searchにリクエストがあったときのマッピングを行うメソッド
     * @param model
     * @param userLocation フォーム(name="userStation")の入力値
     * @return "index"
     */
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("userStation") String userLocation) {
        /**
         * userLocationをコンテンツジオコーダAPIに渡してレスポンスを受け取る機能
         * userLocationに何も入力されなかった場合はサンプルとして湘南台駅が使用される
         * @see <a href="https://developer.yahoo.co.jp/webapi/map/openlocalplatform/v1/contentsgeocoder.html">YOLP(地図)コンテンツジオコーダAPI</a>
         */
        String LOCATION; //ロケーション
        if (userLocation.isEmpty()) {
            LOCATION = "湘南台駅";
        } else {
            LOCATION = userLocation;
        }
        System.out.println("LOCATION:"+LOCATION);
        final String CATEGORY = "landmark"; //カテゴリ
        final String REQUEST_URL = "https://map.yahooapis.jp/geocode/cont/V1/contentsGeoCoder"
                + "?appid=" + APPID
                + "&query=" + LOCATION
                + "&category=" + CATEGORY
                + "&output=" + OUTPUT; //リクエストURLの合成
        //ContentsGeoCoderクラスへのバインドをtry
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
         * @see <a href="https://developer.yahoo.co.jp/webapi/map/openlocalplatform/v1/localsearch.html">YOLP(地図)ローカルサーチAPI</a>
         */
        final String[] LATLON = contentsGeoCoder.getFeatureList().get(0).getGeometry().getCoordinates().split(",", 0); //coordinatesを緯度と経度に分割 latLng[0]:経度 latLng[1]:緯度
        final String DIST = "10"; //中心(latLng)からの検索距離(km)
        final Integer Results = 10; //取得件数
        final String GC = "0115001"; //業種コード(GC)
        final String REQUEST_URL_2 = "https://map.yahooapis.jp/search/local/V1/localSearch"
                + "?appid=" + APPID
                + "&output=" + OUTPUT
                + "&results=" + Results
                + "&gc=" + GC
                + "&lat=" + LATLON[1]
                + "&lon=" + LATLON[0]
                + "&dist=" + DIST
                + "&sort=geo"; //距離順ソート
        System.out.println(REQUEST_URL_2);
        //LocalSearchクラスへのバインドをtry
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
