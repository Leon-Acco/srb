package com.acho.srb.core.mapper;

import com.acho.srb.core.bean.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    void updateAccount( @Param("bindCode") String bindCode,
                        @Param("amount")BigDecimal amount,
                        @Param("freezeAmount")BigDecimal freezeAmount);

}
