package com.swiftdelivery.authenticator.response;

import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomResponse {

    public static ResponseEntity response(int statusCode,String message,Object data){
        Map<String,Object> formattedResponse = new LinkedHashMap();
        formattedResponse.put("statusCode",statusCode);
        formattedResponse.put("message",message);
        formattedResponse.put("data",data);

        return ResponseEntity.status(statusCode).body(formattedResponse);
    }
}
