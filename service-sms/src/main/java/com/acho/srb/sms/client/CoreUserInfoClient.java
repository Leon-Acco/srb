package com.acho.srb.sms.client;

import com.acho.srb.sms.fallback.CoreUserInfoClientFallback;
import com.alibaba.csp.sentinel.command.annotation.CommandMapping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description:
 * @Author:Acho-leon
 * @Modified By:
 * @params:
 * @creat:2021-11-04-22:37
 */
@FeignClient(value = "service-core",fallback = CoreUserInfoClientFallback.class)
public interface CoreUserInfoClient {



    @GetMapping("/api/core/userInfo/checkMobile/{mobile}")
    public boolean  checkMobile(@PathVariable String mobile);

}
