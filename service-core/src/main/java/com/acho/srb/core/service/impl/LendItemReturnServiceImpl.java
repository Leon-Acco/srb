package com.acho.srb.core.service.impl;

import com.acho.srb.core.bean.Lend;
import com.acho.srb.core.bean.LendItem;
import com.acho.srb.core.bean.LendItemReturn;
import com.acho.srb.core.bean.LendReturn;
import com.acho.srb.core.mapper.LendItemMapper;
import com.acho.srb.core.mapper.LendItemReturnMapper;
import com.acho.srb.core.mapper.LendMapper;
import com.acho.srb.core.mapper.LendReturnMapper;
import com.acho.srb.core.service.LendItemReturnService;
import com.acho.srb.core.service.UserBindService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务实现类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
@Service
public class LendItemReturnServiceImpl extends ServiceImpl<LendItemReturnMapper, LendItemReturn> implements LendItemReturnService {

    @Resource
    private UserBindService userBindService;

    @Resource
    private LendItemMapper lendItemMapper;

    @Resource
    private LendMapper lendMapper;

    @Resource
    private LendReturnMapper lendReturnMapper;
    @Override
    public List<LendItemReturn> selectByLendId(Long lendId, Long userId) {
        QueryWrapper<LendItemReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("lend_id", lendId)
                .eq("invest_user_id", userId)
                .orderByAsc("current_period");
        return baseMapper.selectList(queryWrapper);
    }



    /**
     * 把所有投这个标的投资者的回款计划的每一期都填充为一个Map对象，填充到集合中返回
     * 添加还款明细
     * @param lendReturnId
     */
    @Override
    public List<Map<String, Object>> addReturnDetail(Long lendReturnId) {

        //获取还款记录
        LendReturn lendReturn = lendReturnMapper.selectById(lendReturnId);
        //获取标的信息
        Lend lend = lendMapper.selectById(lendReturn.getLendId());

        //根据还款id获取回款列表
        List<LendItemReturn> lendItemReturnList = this.selectLendItemReturnList(lendReturnId);//根据还款计划id获取对应的回款计划列表
        List<Map<String, Object>> lendItemReturnDetailList = new ArrayList<>();
        for(LendItemReturn lendItemReturn : lendItemReturnList) {
            LendItem lendItem = lendItemMapper.selectById(lendItemReturn.getLendItemId());
            String bindCode = userBindService.getBindCodeByUserId(lendItem.getInvestUserId());

            Map<String, Object> map = new HashMap<>();
            //项目编号
            map.put("agentProjectCode", lend.getLendNo());
            //出借编号
            map.put("voteBillNo", lendItem.getLendItemNo());
            //收款人（出借人）
            map.put("toBindCode", bindCode);
            //还款金额
            map.put("transitAmt", lendItemReturn.getTotal());
            //还款本金
            map.put("baseAmt", lendItemReturn.getPrincipal());
            //还款利息
            map.put("benifitAmt", lendItemReturn.getInterest());
            //商户手续费
            map.put("feeAmt", new BigDecimal("0"));

            lendItemReturnDetailList.add(map);
        }
        return lendItemReturnDetailList;
    }

    /**
     * 根据还款计划Id查询出所有的投资者投这个标的回款计划的相应期数
     * @param lendReturnId（某一期的指示Id）
     * @return
     */
    @Override
    public List<LendItemReturn> selectLendItemReturnList(Long lendReturnId) {
        QueryWrapper<LendItemReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lend_return_id", lendReturnId);
        List<LendItemReturn> lendItemReturnList = baseMapper.selectList(queryWrapper);
        return lendItemReturnList;
    }
}
