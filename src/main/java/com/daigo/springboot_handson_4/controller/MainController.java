package com.daigo.springboot_handson_4.controller;

import com.daigo.springboot_handson_4.domains.CafeSearchMessenger;
import com.daigo.springboot_handson_4.service.MainService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@Slf4j
public class MainController {
    @Autowired
    MainService mainService;

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
    public String search(Model model, @ModelAttribute("userLocation") String userLocation) {
        CafeSearchMessenger cafeSearchMessenger = mainService.searchCafe(userLocation);
        if (Objects.nonNull(cafeSearchMessenger)) {
            model.addAttribute("userLocation", cafeSearchMessenger.getUserLocation());
            model.addAttribute("message", cafeSearchMessenger.getMessage());
            model.addAttribute("coordinates", cafeSearchMessenger.getCoordinates());
            model.addAttribute("resultsNumber", cafeSearchMessenger.getResultsNumber());
            model.addAttribute("features", cafeSearchMessenger.getFeatures());
        }
        return "index";
    }
}
