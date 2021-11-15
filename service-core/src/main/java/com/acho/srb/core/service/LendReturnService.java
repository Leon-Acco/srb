package com.acho.srb.core.service;

import com.acho.srb.core.bean.LendReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface LendReturnService extends IService<LendReturn> {


    List<LendReturn> selectByLendId(Long lendId);

    String commitReturn(Long lendReturnId, Long userId);

    /**
     * 用户还款回调函数
     * @param paramMap
     */
    void notify(Map<String, Object> paramMap);
}
