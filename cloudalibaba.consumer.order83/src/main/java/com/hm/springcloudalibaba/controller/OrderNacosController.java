package com.hm.springcloudalibaba.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderNacosController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service.url.nacos-user-service}")
    private String ServeiceUrl;

    @GetMapping("/consumer/echo/{string}")
    public String echoAppName(@PathVariable String string){
        return restTemplate.getForObject(ServeiceUrl+"/echo/"+string,String.class);
    }

}

