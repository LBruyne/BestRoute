package com.hinsliu.bestroute.handler;

import com.hinsliu.bestroute.model.output.WxUserMessageModel;
import com.hinsliu.bestroute.service.BaseService;
import com.hinsliu.bestroute.utils.wechat.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ValidatorHandler {

    /**
     * 处理验证失败
     * 
     * @param exception
     * @return
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public String validHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {
        // 返回数据
        WxUserMessageModel responseModel = BaseService.getThreadLocal();

        BindingResult bindingResult = exception.getBindingResult();
        // 失败表单
        Object instance = bindingResult.getTarget();
        // 失败原因
        String msg = bindingResult.getFieldError().getDefaultMessage();
        String sb = request.getRequestURI() +
                " 出错: " +
                msg;
        log.warn(msg, instance);

        responseModel.setContent(sb);
        return WeChatUtil.toXML(responseModel);
    }

}
