package com.acho.srb.core.service;

import com.acho.srb.core.bean.TransFlow;
import com.acho.srb.core.bean.bo.TransFlowBO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 交易流水表 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface TransFlowService extends IService<TransFlow> {

    void saveTransFlow(TransFlowBO transFlowBO);

    boolean isSaveTransFlow(String agentBillNo);

    List<TransFlow> selectByUserId(Long userId);
}
