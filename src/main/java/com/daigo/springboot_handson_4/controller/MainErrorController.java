package com.daigo.springboot_handson_4.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    /**
     * エラーのステータスコードによってユーザに表示するメッセージを返却するメソッド。
     *
     * @param statusCode ステータスコード
     * @return ユーザに表示するメッセージ
     */
    public String errorMessenger(Object statusCode) {
        final int code = Integer.parseInt(String.valueOf(statusCode));
        String message;
        switch (code) {
            case 404:
                message = "お探しのページがありませんでした。";
                break;
            case 500:
                message = "サーバ側でエラーが発生しました。";
                break;
            default:
                message = "不明なエラーです。";
                break;
        }
        return message;
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
        mav.addObject("message", errorMessenger(statusCode));
        return mav;
    }
}
