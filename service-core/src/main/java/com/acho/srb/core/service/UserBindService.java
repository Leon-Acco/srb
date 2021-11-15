package com.acho.srb.core.service;

import com.acho.srb.core.bean.UserBind;
import com.acho.srb.core.bean.vo.UserBindVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface UserBindService extends IService<UserBind> {

    String commitBindUser(UserBindVO userBindVO, Long userId);

    void notify(Map<String, Object> switchMap);

    String getBindCodeByUserId(Long investUserId);
}
