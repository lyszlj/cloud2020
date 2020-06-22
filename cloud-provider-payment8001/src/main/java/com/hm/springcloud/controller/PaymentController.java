package com.hm.springcloud.controller;

import com.hm.springcloud.entities.CommonResult;
import com.hm.springcloud.entities.Payment;
import com.hm.springcloud.service.PaymentService;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int resulet = paymentService.create(payment);
        log.info("******插入结果："+resulet);
        if (resulet>0){
            return new CommonResult(200,"插入数据库成功 ,serverPort:"+serverPort,resulet);
        }else {
            return new CommonResult(444,"插入数据库失败",null);
        }
    }


    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment resulet = paymentService.getPaymentById(id);
        log.info("******获取成功："+resulet+"hahahsadaa");
        if (resulet!=null){
            return new CommonResult(200,"查询成功,serverPort："+serverPort,resulet);
        }else {
            return new CommonResult(444,"无对应记录",null);
        }
    }

    @GetMapping("/payment/discovery")
    public Object disCovery(){
        List<String> services = discoveryClient.getServices();
        for (String element:services) {
            log.info("******element:"+element);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance: instances) {
            log.info(instance.getInstanceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }

        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return serverPort;
    }

}
