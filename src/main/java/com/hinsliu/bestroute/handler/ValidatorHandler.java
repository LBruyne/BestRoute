package com.hinsliu.bestroute.handler;

import com.hinsliu.bestroute.model.dto.output.RespBean;
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
    public RespBean validHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        Object instance = bindingResult.getTarget();
        return RespBean.fail(request.getRequestURI() + " 出错: " + bindingResult.getFieldError().getDefaultMessage());
    }

}
