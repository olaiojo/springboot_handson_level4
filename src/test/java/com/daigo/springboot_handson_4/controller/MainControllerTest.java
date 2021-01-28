package com.daigo.springboot_handson_4.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 1-1. 定義済のパス"/index"にgetリクエストがあったとき、ステータスコード200が返ってくることをテスト
     */
    @Test
    public void getIndexTest() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andExpect(status().isOk()); //200
    }

    /**
     * 1-2. 定義済のパス"/search"にgetリクエストがあったとき、ステータスコード200が返ってくることをテスト
     */
    @Test
    public void getSearchIsOkTest() throws Exception {
        this.mockMvc.perform(get("/search"))
                .andExpect(status().isOk()); //200
    }

    /**
     * 2. 未定義のパス"/hoge"にgetリクエストがあったとき、ステータスコード404が返ってくることをテスト
     */
    @Test
    public void getUndefinedIsNotFoundTest() throws Exception {
        this.mockMvc.perform(get("/hoge"))
                .andExpect(status().isNotFound()); //404
    }

    /**
     * 3-1. 定義済のパス"/index"にgetリクエストがあったとき、indexというView名をreturnしていることをテスト
     */
    @Test
    public void getIndexThenReturnIndexTest() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andExpect(view().name("index"));
    }

    /**
     * 3-2. 定義済のパス"/search"にgetリクエストがあったとき、indexというview名をreturnしていることをテスト
     */
    @Test
    public void getSearchThenReturnIndexTest() throws Exception {
        this.mockMvc.perform(get("/search"))
                .andExpect(view().name("index"));
    }

    /**
     * 4-1. 定義済のパス"/search"にパラメータuserLocation=横浜駅でgetリクエストがあったとき、正しい情報がModelにAddされることをテスト
     */
    @Test
    public void addAttributeModelWhenNormalTest() throws Exception {
        this.mockMvc.perform(get("/search").param("userLocation", "横浜駅"))
                .andExpect(model().attribute("userLocation", "横浜駅"))
                .andExpect(model().attribute("message", "近くにカフェが見つかりました。"));
    }

    /**
     * 4-2. 定義済のパス"/search"にパラメータuserLocation=知床半島でgetリクエストがあったとき、正しい情報がModelにAddされることをテスト
     */
    @Test
    public void addAttributeModelWhenNoCafesTest() throws Exception {
        this.mockMvc.perform(get("/search").param("userLocation", "知床半島"))
                .andExpect(model().attribute("userLocation", "知床半島"))
                .andExpect(model().attribute("message", "近くにカフェがありませんでした。"));
    }

    /**
     * 4-3. 定義済のパス"/search"にパラメータuserLocation=あああでgetリクエストがあったとき、正しい情報がModelにAddされることをテスト
     */
    @Test
    public void addAttributeModelWhenInvalidLocationTest() throws Exception {
        this.mockMvc.perform(get("/search").param("userLocation", "あああ"))
                .andExpect(model().attribute("userLocation", "あああ"))
                .andExpect(model().attribute("message", "ロケーションが見つかりませんでした。"));
    }

    /**
     * 4-4. 定義済のパス"/search"にパラメータuserLocation=空でgetリクエストがあったとき、正しい情報がModelにAddされることをテスト
     */
    @Test
    public void addAttributeModelWhenEmptyLocationTest() throws Exception {
        this.mockMvc.perform(get("/search").param("userLocation", ""))
                .andExpect(model().attribute("message", "検索地点を入力してください。"));
    }
}
