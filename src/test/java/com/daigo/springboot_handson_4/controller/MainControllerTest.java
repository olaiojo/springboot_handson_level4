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
     * 1. 定義済のパス"/index"にgetリクエストがあったとき、ステータスコード200が返ってくることをテスト
     */
    @Test
    public void getIndexTest() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andExpect(status().isOk()); //200
    }

    /**
     * 2. 未定義のパス"/hoge"にgetリクエストがあったとき、ステータスコード404が返ってくることをテスト
     */
    @Test
    public void getUndefinedTest() throws Exception {
        this.mockMvc.perform(get("/hoge"))
                .andExpect(status().isNotFound());
    }

    /**
     * 3. 定義済のパス"/index"にgetリクエストがあったとき、indexというView名をreturnしていることをテスト
     */
    @Test
    public void returnIndexTest() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andExpect(view().name("index"));
    }

    /**
     * 4. 定義済のパス"/search"にパラメータuserLocation=横浜駅でgetリクエストがあったとき、正しい情報がModelにAddされることをテスト
     */
    @Test
    public void addAttributeModelTest() throws Exception {
        //TODO: テスト時に環境変数が取れてない
        // -> AppIDが入ってないからYahooのAPIが動かない
        // -> ModelにAddされた値もNullになってる
        // のを解消
        // (application.propertiesに直でAppID入れるとpathできる)
        this.mockMvc.perform(get("/search")
                .param("userLocation", "横浜駅"))
                .andExpect(model()
                        .attribute("userLocation", "横浜駅"))
                .andExpect(view().name("index"));
    }
}
