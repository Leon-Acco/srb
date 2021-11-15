package com.acho.srb.core.service;

import com.acho.srb.core.bean.LendItem;
import com.acho.srb.core.bean.vo.InvestVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface LendItemService extends IService<LendItem> {


    /**
     * 提交投标的表单
     * @param investVO
     * @return
     */
    String commitInvest(InvestVO investVO);

    /**
     * 投标的回调方法
     * @param paramMap
     */
    void notify(Map<String, Object> paramMap);


    List<LendItem> selectByLendId(Long lendId, Integer status);

    /**
     * 查询所有投资该标的的投资项
     * @param lendId
     * @return
     */
    List<LendItem> selectByLendId(Long lendId);
}
