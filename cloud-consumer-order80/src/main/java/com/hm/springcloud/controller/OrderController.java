package com.hm.springcloud.controller;

import com.hm.springcloud.entities.CommonResult;
import com.hm.springcloud.entities.Payment;
import com.hm.springcloud.lb.LoadBalance;
import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class OrderController {
    //public static final String PAYMENT_URL = "http://localhost:8001";
    public static final String PAYMENT_URL="http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private LoadBalance loadBalance;

    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);
    }


    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") long id){
        log.info("llll");
        return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
    }
    @GetMapping(value = "/consumer/payment/getforEntity/{id}")
    public CommonResult<Payment> getPaymentforEntity(@PathVariable("id") long id){
        ResponseEntity<CommonResult> forEntity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
        log.info(forEntity.getHeaders().toString()+"***********"+forEntity.getStatusCode());
        if (forEntity.getStatusCode().is2xxSuccessful()){
            return forEntity.getBody();
        }else {
            return new CommonResult<>(444,"操作失败");
        }
    }

    @GetMapping(value = "/consumer/payment/lb")
    public String getPaymentLB() {
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        ServiceInstance instances1 = loadBalance.instances(instances);
        return restTemplate.getForObject(instances1.getUri()+"/payment/lb",String.class);
    }

}
