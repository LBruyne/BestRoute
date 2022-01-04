package com.hinsliu.bestroute.service;

import com.hinsliu.bestroute.model.input.QueryForm;
import com.hinsliu.bestroute.model.output.WxUserMessageModel;
import com.hinsliu.bestroute.utils.wechat.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author: Hins Liu
 * @description: Wechat service.
 */
@Slf4j
@Service
public class WxService extends BaseService {

    @Resource
    private BestRouteService bestRouteService;

    public WxUserMessageModel getResponseModel(WxUserMessageModel wxUserMessageModel) {
        // 返回数据
        WxUserMessageModel responseModel = WeChatUtil.generateResponesModel(wxUserMessageModel, "text",
                getDefaultContent());
        setThreadLocal(responseModel);

        // 文字消息
        if (wxUserMessageModel.getMsgType().equals("text")) {
            String content = wxUserMessageModel.getContent();
            String fromUser = wxUserMessageModel.getFromUserName();
            log.info("接收到公众号消息:content={}, from={}", content, fromUser);

            String[] inputs = content.split("\n");

            if (inputs.length == 2 && inputs[0].startsWith("起点") && inputs[1].startsWith("景点")) {
                QueryForm query = new QueryForm();
                // 起点
                String start = inputs[0].replaceAll(" ", "").replaceFirst("起点+", "");
                query.setStart(start);
                query.setEnd(start);

                // 游览景点
                String locations = inputs[1].replaceAll(" ", "").replaceFirst("景点+", "");
                query.setLocations(Arrays.asList(locations.split("\\+")));

                responseModel.setContent(bestRouteService.getBestRoute(query));
            }

            // 其余为不符合要求的输入模式
        }
        return responseModel;
    }

    private String getDefaultContent() {
        return "请按要求输入";
    }
}
