package com.hinsliu.bestroute.service;

import com.hinsliu.bestroute.model.output.WxUserMessageModel;
import com.hinsliu.bestroute.utils.wechat.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: Hins Liu
 * @description: Wechat service.
 */
@Slf4j
@Service
public class WxService {

    @Resource
    private BestRouteService bestRouteService;

    public WxUserMessageModel getResponseModel(WxUserMessageModel wxUserMessageModel) {
        // 返回数据
        WxUserMessageModel responseModel = WeChatUtil.generateResponesModel(wxUserMessageModel, "text",
                getDefaultContent());

        // 文字消息
        if (wxUserMessageModel.getMsgType().equals("text")) {
            String content = wxUserMessageModel.getContent();
            String fromUser = wxUserMessageModel.getFromUserName();
            log.info("接收到公众号消息:content={}, from={}", content, fromUser);

            // 其余为不符合要求的输入模式
        }
        return responseModel;
    }

    private String getDefaultContent() {
        return "";
    }
}
