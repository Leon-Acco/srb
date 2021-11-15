package com.acho.srb.sms.fallback;

import com.acho.srb.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author:Acho-leon
 * @Modified By:
 * @params:
 * @creat:2021-11-05-10:56
 */
@Component
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient {


    @Override
    public boolean checkMobile(String mobile) {
        return false;//直接给一个默认值
    }
}
