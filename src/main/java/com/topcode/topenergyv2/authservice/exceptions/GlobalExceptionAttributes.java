package com.topcode.topenergyv2.authservice.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;


@Component
public class GlobalExceptionAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request,
                                                  ErrorAttributeOptions options) {

        Throwable error = super.getError(request);
        System.out.println();
        Map<String, Object> map =  new HashMap<>();//super.getErrorAttributes(request, options);
        map.put("message","Something went wrong");
        map.put("success", false);
        map.put("data",error.getMessage());
        return map;
    }

}
