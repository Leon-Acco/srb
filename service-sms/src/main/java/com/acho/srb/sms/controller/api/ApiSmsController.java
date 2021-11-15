package com.acho.srb.sms.controller.api;

import com.acho.common.exception.Assert;
import com.acho.common.result.R;
import com.acho.common.result.ResponseEnum;
import com.acho.common.utils.RandomUtils;
import com.acho.common.utils.RegexValidateUtils;
import com.acho.srb.sms.client.CoreUserInfoClient;
import com.acho.srb.sms.service.SmsService;
import com.acho.srb.sms.util.SmsProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {

    @Resource
    private SmsService smsService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CoreUserInfoClient coreUserInfoClient;


    /**
     * 远程调用的步骤
     * 需求：在sms微服务中调用core的方法判断
     * 步骤：1.导入openFeign依赖
     *      2.启动类添加注解:@EnableFeignClients
     *      3.接口的远程调用@FeignClient(value = "service-core")----需要调用服务的应用名
     *      4.接口中创建同名同参数，请求地址的方法
     *      5，调用接口的方法
     *
     * @param mobile
     * @return
     */
    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public R send(
            @ApiParam(value = "手机号", required = true)
            @PathVariable String mobile){

        //MOBILE_NULL_ERROR(-202, "手机号不能为空"),
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        //MOBILE_ERROR(-203, "手机号不正确"),
        //Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);

        //判断电话号码是不是已经被注册过了，注册过就不要生成验证码发送了

        boolean b = coreUserInfoClient.checkMobile(mobile);
        System.out.println(b);
        if(b){
            return R.error().message("该手机号已经注册过了");
        }

        //生成验证码
        String code = RandomUtils.getFourBitRandom();
        //组装短信模板参数
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        //发送短信

        smsService.send(mobile, SmsProperties.TEMPLATE_CODE, param);

        //将验证码存入redis
        redisTemplate.opsForValue().set("srb:sms:code:" + mobile, code, 5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }
}