package com.daigo.springboot_handson_4.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * ErrorControllerインタフェースの実装クラス
 */

@Controller
@RequestMapping("/error")
public class MainErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping
    public ModelAndView error(HttpServletRequest request) {
        // ERROR_STATUS_CODEを取得
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE); // xxx
        // ステータスコードをHttpStatus.valueOf()でHttpStatusに変換
        HttpStatus status = HttpStatus.valueOf((Integer) statusCode); //xxx_ERROR_ERROR

        // 出力情報のセット
        ModelAndView mav = new ModelAndView();
        mav.setStatus(status);
        mav.setViewName("error");
        mav.addObject("title", "Error!");
        mav.addObject("status", status);
        return mav;
    }
}
