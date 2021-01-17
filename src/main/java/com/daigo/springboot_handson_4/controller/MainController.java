package com.daigo.springboot_handson_4.controller;

import com.daigo.springboot_handson_4.cafedomains.LocalSearch;
import com.daigo.springboot_handson_4.config.GeoCoderConfig;
import com.daigo.springboot_handson_4.config.LocalSearchConfig;
import com.daigo.springboot_handson_4.domains.ContentsGeoCoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@Slf4j
public class MainController {
    private static final RestTemplate restTemplate = new RestTemplate();
    /**
     * Setting
     */
    //環境変数からappidのインジェクション
    @Value("${app.id}")
    String APPID;
    //出力形式
    final String OUTPUT = "json";
    //configクラスからのインジェクション
    private final GeoCoderConfig geoCoderConfig;
    private final LocalSearchConfig localSearchConfig;

    public MainController(GeoCoderConfig geoCoderConfig,
                          LocalSearchConfig localSearchConfig) {
        this.geoCoderConfig = geoCoderConfig;
        this.localSearchConfig = localSearchConfig;
    }

    /**
     * indexにリクエストがあったときのマッピングを行うメソッド
     *
     * @return "index"
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * searchにリクエストがあったときのマッピングを行うメソッド
     *
     * @param model        Model
     * @param userLocation フォーム(name="userStation")の入力値
     * @return "index"
     */
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("userLocation") String userLocation)
            throws JsonProcessingException {
        /*
          userLocationをコンテンツジオコーダAPIに渡してレスポンスを受け取る機能
          userLocationに何も入力されなかった場合はサンプルとして湘南台駅が使用される
          @see <a href="https://developer.yahoo.co.jp/webapi/map/openlocalplatform/v1/contentsgeocoder.html">YOLP(地図)コンテンツジオコーダAPI</a>
         */
        String LOCATION; //ロケーション
        if (userLocation.isEmpty()) {
            LOCATION = "湘南台駅";
        } else {
            LOCATION = userLocation;
        }
        log.info("LOCATION: {}", LOCATION);
        final String CATEGORY = "landmark"; //カテゴリ
        final String GEOCODER_URL = UriComponentsBuilder
                .fromHttpUrl(geoCoderConfig.getHost())
                .path(geoCoderConfig.getPath())
                .queryParam("appid", APPID)
                .queryParam("query", LOCATION)
                .queryParam("category", CATEGORY)
                .queryParam("output", OUTPUT)
                .build()
                .toString();
        log.info("GEOCODER_URL: {}", GEOCODER_URL);
        new ContentsGeoCoder();
        ContentsGeoCoder contentsGeoCoder;
        String response = restTemplate.getForObject(GEOCODER_URL, String.class);
        ObjectMapper mapper = new ObjectMapper();
        contentsGeoCoder = mapper.readValue(response, ContentsGeoCoder.class);
        if (Objects.isNull(contentsGeoCoder.getFeatureList())) {
            model.addAttribute("userLocation", userLocation);
            model.addAttribute("message", "ロケーションが見つかりませんでした。");
            model.addAttribute("resultsNumber", "0"); //検索件数
        } else {
        /*
          coordinatesをローカルサーチAPIに渡してレスポンスを受け取る
          @see <a href="https://developer.yahoo.co.jp/webapi/map/openlocalplatform/v1/localsearch.html">YOLP(地図)ローカルサーチAPI</a>
         */
            final String[] LATLON = contentsGeoCoder
                    .getFeatureList()
                    .get(0)
                    .getGeometry()
                    .getCoordinates()
                    .split(",", 0); //coordinatesを緯度と経度に分割 latLng[0]:経度 latLng[1]:緯度
            final String DIST = "10"; //中心(latLng)からの検索距離(km)
            final int Results = 10; //取得件数
            final String GC = "0115001"; //業種コード(GC)
            final String LOCAL_SEARCH_URL = UriComponentsBuilder
                    .fromHttpUrl(localSearchConfig.getHost())
                    .path(localSearchConfig.getPath())
                    .queryParam("appid", APPID)
                    .queryParam("output", OUTPUT)
                    .queryParam("results", Results)
                    .queryParam("gc", GC)
                    .queryParam("lat", LATLON[1])
                    .queryParam("lon", LATLON[0])
                    .queryParam("dist", DIST)
                    .queryParam("sort", "geo")
                    .build()
                    .toString();
            log.info("LOCAL_SEARCH_URL: {}", LOCAL_SEARCH_URL);
            new LocalSearch();
            LocalSearch localSearch;
            try {
                localSearch = restTemplate.getForObject(LOCAL_SEARCH_URL, LocalSearch.class);
            } catch (HttpClientErrorException e) {
                log.error("HttpClientErrorException, ", e);
                throw e;
            } catch (HttpServerErrorException e) {
                log.error("HttpServerErrorException, ", e);
                throw e;
            }
            model.addAttribute("userLocation", contentsGeoCoder.getFeatureList().get(0).getName()); //での検索結果
            model.addAttribute("coordinates",
                    contentsGeoCoder.getFeatureList().get(0).getGeometry().getCoordinates()); //中心地点
            if (Objects.nonNull(localSearch)) {
                model.addAttribute("resultsNumber", localSearch.getResultInfo().getCount()); //検索件数
                model.addAttribute("features", localSearch.getFeatureList()); //検索結果
            } else {
                model.addAttribute("resultsNumber", "0");
            }
        }
        return "index";
    }
}
