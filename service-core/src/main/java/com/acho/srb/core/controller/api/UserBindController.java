package com.acho.srb.core.controller.api;


import com.acho.common.result.R;
import com.acho.srb.base.utils.JwtUtils;
import com.acho.srb.core.bean.vo.UserBindVO;
import com.acho.srb.core.hfb.RequestHelper;
import com.acho.srb.core.service.UserBindService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */

@Api(tags = "会员账号绑定")
@RestController
@RequestMapping("/api/core/userBind")
@Slf4j
//@CrossOrigin
public class UserBindController {

    @Resource
    private UserBindService userBindService;

    @ApiOperation("账户绑定提交数据")
    @PostMapping("/auth/bind")
    public R bind(@RequestBody UserBindVO userBindVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String formStr = userBindService.commitBindUser(userBindVO, userId);
        return R.ok().data("formStr", formStr);//返回一个动态的自动提交的表单;
    }

    @ApiOperation("账户绑定异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request){
        //获取HFB来的请求参数
        Map<String, Object> switchMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("请求参数的数据为："+switchMap);
        //校验签名的有效性
        boolean signEquals = RequestHelper.isSignEquals(switchMap);
        if (!signEquals){
        log.error("汇付宝传来的签名验证不正确"+ JSON.toJSONString(switchMap));
        return "fail";
        }
        log.info("签名验证成功");
        //调用业务层的方法，修改绑定状态,填充绑定码
        userBindService.notify(switchMap);
        return "success";
    }


}
