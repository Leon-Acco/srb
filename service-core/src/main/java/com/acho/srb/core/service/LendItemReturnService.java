package com.acho.srb.core.service;

import com.acho.srb.core.bean.LendItemReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface LendItemReturnService extends IService<LendItemReturn> {

    List<LendItemReturn> selectByLendId(Long lendId, Long userId);


    List<Map<String, Object>> addReturnDetail(Long lendReturnId);

    /**
     * 根据还款计划id获取对应的回款计划列表(还款计划id《==》回款计划的相应期数 )
     * @param lendReturnId
     * @return
     */
    List<LendItemReturn> selectLendItemReturnList(Long lendReturnId);
}
