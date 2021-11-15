package com.acho.srb.sms.receiver;

import com.acho.srb.base.dto.SmsDTO;
import com.acho.srb.rabbitutil.constant.MQConst;
import com.acho.srb.sms.service.SmsService;
import com.acho.srb.sms.util.SmsProperties;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 监控队列并发送短信
 * @Author:Acho-leon
 * @Modified By:
 * @params:
 * @creat:2021-11-13-10:42
 */
@Service
@Slf4j
public class SmsReceiver {
    @Resource
    SmsService smsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConst.QUEUE_SMS_ITEM,durable = "true"),
            exchange = @Exchange(value = MQConst.EXCHANGE_TOPIC_SMS),
            key = {MQConst.ROUTING_SMS_ITEM}
    ))
    public void  send(SmsDTO smsDTO){
        log.info("开始监听消息");
        Map<String,Object> param = new HashMap<>();
        param.put("code",smsDTO.getMessage());
        smsService.send(smsDTO.getMobile(), SmsProperties.TEMPLATE_CODE, param);
    }



}
