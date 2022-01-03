package com.hinsliu.bestroute.controller;

import com.hinsliu.bestroute.model.output.WxUserMessageModel;
import com.hinsliu.bestroute.service.WxService;
import com.hinsliu.bestroute.utils.wechat.DigestUtil;
import com.hinsliu.bestroute.utils.wechat.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: Hins Liu
 * @description: WeChat Controller
 */
@Slf4j
@RestController
public class WxController {

    /**
     * 微信公众号的token
     */
    @Value("${wechat.token}")
    private String wechatToken;

    @Resource
    private WxService wxService;

    /**
     * 微信接口验证使用
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/wx")
    public String replyWxServe(ServletRequest request, ServletResponse response) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (isMessageNull(signature, timestamp, nonce, echostr)) {
            log.info("not wehcat message");
            return "";
        }

        List<String> list = new ArrayList<>();
        // token
        list.add(this.wechatToken);
        list.add(timestamp);
        list.add(nonce);

        // sort as alphabet order
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        list.forEach(sb::append);

        // encrypt
        String hashCode = DigestUtil.sha1DigestAsHex(sb.toString());

        if (signature.equals(hashCode)) {
            return echostr;
        } else {
            return "";
        }
    }

    /**
     * 来自微信的数据获取
     */
    @PostMapping("/wx")
    public String replyUserMessage(ServletRequest request, ServletResponse response) {
        WxUserMessageModel wxUserMessageModel;
        try {
            wxUserMessageModel = WeChatUtil.resolveXmlData(request.getInputStream());
        } catch (IOException e) {
            log.error("parse xml fail", e);
            return "success";
        }
        if (null == wxUserMessageModel) {
            return "success";
        }

        // 根据需要实现业务逻辑
        WxUserMessageModel responseModel = wxService.getResponseModel(wxUserMessageModel);
        return WeChatUtil.toXML(responseModel);
    }

    private boolean isMessageNull(String signature, String timestamp, String nonce, String echostr) {
        return null == signature || null == timestamp || null == nonce || null == echostr;
    }
}
