package com.acho.srb.core.service;

import com.acho.srb.core.bean.BorrowInfo;
import com.acho.srb.core.bean.vo.BorrowInfoApprovalVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface BorrowInfoService extends IService<BorrowInfo> {

    BigDecimal getBorrowAmount(Long userId);

    void saveBorrowInfo(BorrowInfo borrowInfo, Long userId);

    Integer getStatusByUserId(Long userId);

    List<BorrowInfo> selectList();

    void approval(BorrowInfoApprovalVO borrowInfoApprovalVO);

    Map<String, Object> getBorrowInfoDetail(Long id);
}
