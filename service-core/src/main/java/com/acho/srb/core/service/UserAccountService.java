package com.acho.srb.core.service;

import com.acho.srb.core.bean.UserAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface UserAccountService extends IService<UserAccount> {

    /**
     * 充值账户
     * @param chargeAmt
     * @param userId
     * @return
     */
    String commitCharge(BigDecimal chargeAmt, Long userId);

    /**
     * 充值的回调函数
     * @param paramMap
     * @return
     */
    String notify(Map<String, Object> paramMap);

    /**
     * 查询账户余额
     * @param userId
     * @return
     */
    BigDecimal getAccount(Long userId);

    /**
     * 充值提交
     * @param fetchAmt
     * @param userId
     * @return
     */
    String commitWithdraw(BigDecimal fetchAmt, Long userId);

    void notifyWithdraw(Map<String, Object> paramMap);
}
