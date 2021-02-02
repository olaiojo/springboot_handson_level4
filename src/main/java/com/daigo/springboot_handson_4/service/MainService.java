package com.daigo.springboot_handson_4.service;

import com.daigo.springboot_handson_4.config.GeoCoderConfig;
import com.daigo.springboot_handson_4.config.LocalSearchConfig;
import com.daigo.springboot_handson_4.config.YahooApiConfig;
import com.daigo.springboot_handson_4.domains.CafeSearchMessenger;
import com.daigo.springboot_handson_4.domains.geocoder.ContentsGeoCoder;
import com.daigo.springboot_handson_4.domains.localsearch.LocalSearch;
import java.util.NoSuchElementException;
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
        ContentsGeoCoder geoCoderResponse;
        LocalSearch localSearchResponse;
        final CafeSearchMessenger messenger = new CafeSearchMessenger();
        final String userLocation = userInputLocation.replaceAll("[?&=/\r\n]", ""); //リクエストパラメータに関係する文字は切る
        final String category = "landmark";

        if (userLocation.isEmpty()) {
            invalidLocation(messenger);
            return messenger;
        }

        // コンテンツジオコーダAPIへのアクセス
        try {
            geoCoderResponse = requestToGeocoderApi(userLocation, category).orElseThrow();
        } catch (NoSuchElementException e) {
            // コンテンツジオコーダAPIで不具合があった場合
            log.error("NoSuchElementException", e);
            locationNotFound(messenger, userLocation);
            return messenger;
        }

        // コンテンツジオコーダAPIへのリクエストは成功したが結果がなかった場合
        if (Objects.isNull(geoCoderResponse.getFeatureList())) {
            locationNotFound(messenger, userLocation);
            return messenger;
        }

        // ローカルサーチAPIへのアクセス
        try {
            localSearchResponse =
                    requestToLocalSearchApi("0115001", extractLatLon(geoCoderResponse), 10, 10).orElseThrow();
        } catch (NoSuchElementException e) {
            // ローカルサーチAPIで不具合があった場合
            log.error("NoSuchElementException", e);
            cafeNotFound(messenger, userLocation);
            return messenger;
        }

        // ローカルサーチAPIへのリクエストは成功したが結果がなかった場合
        if (Objects.isNull(localSearchResponse.getFeatureList())) {
            cafeNotFound(messenger, userLocation);
            return messenger;
        }

        // 正常系
        cafesAreFound(geoCoderResponse, localSearchResponse, messenger);
        return messenger;
    }

    /**
     * 検索地点が空であるとき、CafeSearchMessenger型インスタンスにその旨をセットするメソッド
     *
     * @param messenger CafeSearchMessenger型のインスタンス
     */
    private void invalidLocation(CafeSearchMessenger messenger) {
        messenger.setMessage("検索地点を入力してください。");
    }

    /**
     * カフェが見つかったとき、CafeSearchMessenger型インスタンスにその旨をセットするメソッド
     *
     * @param messenger CafeSearchMessenger型のインスタンス
     */
    private void cafesAreFound(final ContentsGeoCoder geoCoderResponse,
                               final LocalSearch localSearchResponse,
                               final CafeSearchMessenger messenger) {
        messenger.setUserLocation(geoCoderResponse.getFeatureList().get(0).getName());
        messenger.setCoordinates(geoCoderResponse.getFeatureList().get(0).getGeometry().getCoordinates());
        messenger.setMessage("近くにカフェが見つかりました。");
        messenger.setResultsNumber(String.valueOf(localSearchResponse.getResultInfo().getCount()));
        messenger.setFeatures(localSearchResponse.getFeatureList());
    }

    /**
     * ロケーションが見つからなかったとき、CafeSearchMessenger型インスタンスにその旨をセットするメソッド
     *
     * @param messenger CafeSearchMessenger型のインスタンス
     */
    private void locationNotFound(final CafeSearchMessenger messenger, final String location) {
        messenger.setMessage("ロケーションが見つかりませんでした。");
        messenger.setUserLocation(location);
        messenger.setResultsNumber("0");
    }

    /**
     * カフェが見つからなかったとき、CafeSearchMessenger型インスタンスにその旨をセットするメソッド
     *
     * @param messenger CafeSearchMessenger型のインスタンス
     */
    private void cafeNotFound(final CafeSearchMessenger messenger, final String location) {
        messenger.setMessage("近くにカフェがありませんでした。");
        messenger.setUserLocation(location);
        messenger.setResultsNumber("0");
    }


    /**
     * ContentsGeoCoder型から緯度経度を抽出して配列で返却するメソッド。[経度, 緯度]
     *
     * @param geoCoder コンテンツジオコーダAPIのレスポンスをバインドしたインスタンス
     * @return [経度, 緯度]
     */
    private String[] extractLatLon(final ContentsGeoCoder geoCoder) {
        return geoCoder
                .getFeatureList()
                .get(0)
                .getGeometry()
                .getCoordinates()
                .split(",", 0);
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
