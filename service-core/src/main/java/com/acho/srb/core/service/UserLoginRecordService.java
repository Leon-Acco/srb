package com.acho.srb.core.service;

import com.acho.srb.core.bean.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    /**
     * 获取该员工的登陆日志
     * @param userId
     * @return
     */
    List<UserLoginRecord> listTop50(Long userId);
}
