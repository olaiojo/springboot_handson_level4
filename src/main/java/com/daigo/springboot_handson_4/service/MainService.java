package com.daigo.springboot_handson_4.service;

import com.daigo.springboot_handson_4.config.GeoCoderConfig;
import com.daigo.springboot_handson_4.config.LocalSearchConfig;
import com.daigo.springboot_handson_4.config.YahooApiConfig;
import com.daigo.springboot_handson_4.domains.CafeSearchMessenger;
import com.daigo.springboot_handson_4.domains.geocoder.ContentsGeoCoder;
import com.daigo.springboot_handson_4.domains.localsearch.LocalSearch;
import java.util.Objects;
import java.util.Optional;
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
    // variables
    static final String OUTPUT = "json";
    // instances
    private static final RestTemplate restTemplate = new RestTemplate();
    // inject Config class
    private final YahooApiConfig yahooApiConfig;
    private final GeoCoderConfig geoCoderConfig;
    private final LocalSearchConfig localSearchConfig;

    /**
     * userLocationから近い位置にあるcafeを探し、その情報を返却するメソッド
     *
     * @param userInputLocation 検索の中心となる地点の名称
     * @return cafeSearchMessenger 情報を格納したCafeSearchMessenger型のインスタンス
     */
    public CafeSearchMessenger searchCafe(final String userInputLocation) {
        CafeSearchMessenger cafeSearchMessenger = new CafeSearchMessenger();
        final String userLocation = userInputLocation.replaceAll("[?&=/\r\n]", ""); //リクエストパラメータに関係する文字は切る
        final String category = "landmark";

        if (userLocation.isEmpty()) {
            cafeSearchMessenger.setMessage("検索地点を入力してください。");
            return cafeSearchMessenger;
        }

        // Access to ContentsGeoCoderAPI
        requestToGeocoderApi(userLocation, category).ifPresent((geoCoderResponse) -> {
            if (Objects.nonNull(geoCoderResponse.getFeatureList())) {
                cafeSearchMessenger.setUserLocation(geoCoderResponse.getFeatureList().get(0).getName());
                cafeSearchMessenger
                        .setCoordinates(geoCoderResponse.getFeatureList().get(0).getGeometry().getCoordinates());

                final String[] latlon = geoCoderResponse
                        .getFeatureList()
                        .get(0)
                        .getGeometry()
                        .getCoordinates()
                        .split(",", 0);
                final int dist = 10;  //中心からの検索距離
                final int results = 10;  //取得する件数
                final String gc = "0115001";  //業種コード

                // Access to LocalSearchAPI
                requestToLocalSearchApi(gc, latlon, dist, results).ifPresent((localSearchResponse) -> {
                    if (Objects.nonNull(localSearchResponse.getFeatureList())) {
                        cafeSearchMessenger.setMessage("近くにカフェが見つかりました。");
                        cafeSearchMessenger
                                .setResultsNumber(String.valueOf(localSearchResponse.getResultInfo().getCount()));
                        cafeSearchMessenger.setFeatures(localSearchResponse.getFeatureList());
                    } else {
                        cafeSearchMessenger.setMessage("近くにカフェがありませんでした。");
                        cafeSearchMessenger.setResultsNumber("0");
                    }
                });
            } else {
                cafeSearchMessenger.setMessage("ロケーションが見つかりませんでした。");
                cafeSearchMessenger.setUserLocation(userLocation);
                cafeSearchMessenger.setResultsNumber("0");
            }

        });

        return cafeSearchMessenger;
    }


    /**
     * コンテンツジオコーダAPIにリクエストを行い、そのレスポンスを返却するメソッド
     * リクエスト->バインドに失敗すると空のOptionalを返す。
     *
     * @param location 検索対象の地点名称
     * @param category 検索カテゴリ
     * @return レスポンスボディをバインドしたContentsGeoCoder型のOptional.
     */
    private Optional<ContentsGeoCoder> requestToGeocoderApi(final String location, final String category) {
        final String geocoderUrl = UriComponentsBuilder
                .fromHttpUrl(geoCoderConfig.getHost())
                .path(geoCoderConfig.getPath())
                .queryParam("appid", yahooApiConfig.getAppId())
                .queryParam("query", location)
                .queryParam("category", category)
                .queryParam("output", OUTPUT)
                .build()
                .toString();
        log.info("GEOCODER_URL: {}", geocoderUrl);

        try {
            return Optional.ofNullable(restTemplate.getForObject(geocoderUrl, ContentsGeoCoder.class));
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException, " + e);
            return Optional.empty();
        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException, " + e);
            return Optional.empty();
        }
    }

    /**
     * ローカルサーチAPIにリクエストを行い、そのレスポンスをOptionalで返すメソッド
     * リクエスト->バインドに失敗すると空のOptionalを返す。
     *
     * @param gc      業種コード
     * @param latlon  配列[lon(経度), lat(緯度)]
     * @param dist    検索距離(km)
     * @param results 検索結果の取得件数
     * @return レスポンスボディをバインドしたLocalSearch型のOptional.
     */
    private Optional<LocalSearch> requestToLocalSearchApi(final String gc, final String[] latlon, final int dist,
                                                          final int results) {
        final String localSearchUrl = UriComponentsBuilder
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
        log.info("LOCAL_SEARCH_URL: {}", localSearchUrl);

        try {
            return Optional.ofNullable(restTemplate.getForObject(localSearchUrl, LocalSearch.class));
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException, " + e);
            return Optional.empty();
        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException, " + e);
            return Optional.empty();
        }
    }
}
