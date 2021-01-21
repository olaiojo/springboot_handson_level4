package com.daigo.springboot_handson_4.service;

import com.daigo.springboot_handson_4.config.GeoCoderConfig;
import com.daigo.springboot_handson_4.config.LocalSearchConfig;
import com.daigo.springboot_handson_4.config.YahooApiConfig;
import com.daigo.springboot_handson_4.domains.CafeSearchMessenger;
import com.daigo.springboot_handson_4.domains.geocoder.ContentsGeoCoder;
import com.daigo.springboot_handson_4.domains.localsearch.LocalSearch;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * SpringbootHandson4Applicationのサービスクラス
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MainService {
    // inject Config class
    private final YahooApiConfig yahooApiConfig;
    private final GeoCoderConfig geoCoderConfig;
    private final LocalSearchConfig localSearchConfig;

    // variables
    static final String OUTPUT = "json";

    // instances
    private static final RestTemplate restTemplate = new RestTemplate();

    /**
     * userLocationから近い位置にあるcafeを探し、その情報を返却するメソッド
     *
     * @param userLocation 検索の中心となる地点の名称
     * @return cafeSearchMessenger 情報を格納したCafeSearchMessenger型のインスタンス
     */
    public CafeSearchMessenger searchCafe(String userLocation) {
        CafeSearchMessenger cafeSearchMessenger = new CafeSearchMessenger();
        final String category = "landmark";

        if (userLocation.isEmpty()) {
            cafeSearchMessenger.setMessage("検索地点を入力してください。");
            return cafeSearchMessenger;
        }

        // Access to ContentsGeoCoderAPI
        ContentsGeoCoder geoCoderResponse = requestToGeocoderApi(userLocation, category);

        if (Objects.nonNull(geoCoderResponse.getFeatureList())) {
            // extract lat and lon from geoCoderResponse
            final String[] LATLON = geoCoderResponse
                    .getFeatureList()
                    .get(0)
                    .getGeometry()
                    .getCoordinates()
                    .split(",", 0);
            final int DIST = 10;  //中心からの検索距離
            final int RESULTS = 10;  //取得する件数
            final String GC = "0115001";  //業種コード

            cafeSearchMessenger.setUserLocation(geoCoderResponse.getFeatureList().get(0).getName());
            cafeSearchMessenger.setCoordinates(geoCoderResponse.getFeatureList().get(0).getGeometry().getCoordinates());

            // Access to LocalSearchAPI
            LocalSearch localSearchResponse = requestToLocalSearchApi(GC, LATLON, DIST, RESULTS);

            if (Objects.nonNull(localSearchResponse.getFeatureList())) {
                cafeSearchMessenger.setMessage("近くにカフェが見つかりました。");
                cafeSearchMessenger.setResultsNumber(String.valueOf(localSearchResponse.getResultInfo().getCount()));
                cafeSearchMessenger.setFeatures(localSearchResponse.getFeatureList());
            } else {
                cafeSearchMessenger.setMessage("近くにカフェがありませんでした。");
                cafeSearchMessenger.setResultsNumber("0");
            }
        } else {
            cafeSearchMessenger.setMessage("ロケーションが見つかりませんでした。");
            cafeSearchMessenger.setUserLocation(userLocation);
            cafeSearchMessenger.setResultsNumber("0");
        }
        return cafeSearchMessenger;
    }


    /**
     * コンテンツジオコーダAPIにリクエストを行い、そのレスポンスを返却するメソッド
     * リクエスト->バインドに失敗するとnullを入れて返す
     *
     * @param location 検索対象の地点名称
     * @param CATEGORY 検索カテゴリ
     * @return レスポンスをバインドしたContentsGeoCoder型のインスタンス
     */
    private ContentsGeoCoder requestToGeocoderApi(String location, String CATEGORY) {
        final String GEOCODER_URL = UriComponentsBuilder
                .fromHttpUrl(geoCoderConfig.getHost())
                .path(geoCoderConfig.getPath())
                .queryParam("appid", yahooApiConfig.getAppId())
                .queryParam("query", location)
                .queryParam("category", CATEGORY)
                .queryParam("output", OUTPUT)
                .build()
                .toString();
        log.info("GEOCODER_URL: {}", GEOCODER_URL);

        try {
            log.info("Request to ContentsGeoCoder is succeeded!");
            return restTemplate.getForObject(GEOCODER_URL, ContentsGeoCoder.class);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException, " + e);
            return null;
        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException, " + e);
            return null;
        }
    }

    /**
     * ローカルサーチAPIにリクエストを行い、そのレスポンスを返すメソッド
     * リクエスト->バインドに失敗するとnullを入れて返す
     *
     * @param gc      業種コード
     * @param latlon  配列[lon(経度), lat(緯度)]
     * @param dist    検索距離(km)
     * @param results 検索結果の取得件数
     * @return レスポンスをバインドしたLocalSearch型のインスタンス
     */
    private LocalSearch requestToLocalSearchApi(String gc, String[] latlon, int dist, int results) {
        final String LOCAL_SEARCH_URL = UriComponentsBuilder
                .fromHttpUrl(localSearchConfig.getHost())
                .path(localSearchConfig.getPath())
                .queryParam("appid", yahooApiConfig.getAppId())
                .queryParam("output", OUTPUT)
                .queryParam("results", results)
                .queryParam("gc", gc)
                .queryParam("lat", latlon[1])
                .queryParam("lon", latlon[0])
                .queryParam("dist", dist)
                .queryParam("sort", "geo")
                .build()
                .toString();
        log.info("LOCAL_SEARCH_URL: {}", LOCAL_SEARCH_URL);

        try {
            log.info("Request to LocalSearch is succeeded!");
            return restTemplate.getForObject(LOCAL_SEARCH_URL, LocalSearch.class);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException, " + e);
            return null;
        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException, " + e);
            return null;
        }
    }
}
