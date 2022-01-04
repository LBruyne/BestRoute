package com.hinsliu.bestroute.service;

import com.hinsliu.bestroute.model.output.WxUserMessageModel;
import org.springframework.stereotype.Service;

/**
 * @author: Hins Liu
 * @description: 基本服务
 */
@Service
public class BaseService {

    public static final ThreadLocal<WxUserMessageModel> threadLocal = new ThreadLocal<>();

    public static void setThreadLocal(WxUserMessageModel messageModel) {
        threadLocal.set(messageModel);
    }

    public static WxUserMessageModel getThreadLocal() {
        return threadLocal.get();
    }

}
