package com.acho.srb.core.service.impl;

import com.acho.srb.core.bean.Borrower;
import com.acho.srb.core.bean.BorrowerAttach;
import com.acho.srb.core.bean.UserInfo;
import com.acho.srb.core.bean.UserIntegral;
import com.acho.srb.core.bean.vo.BorrowerApprovalVO;
import com.acho.srb.core.bean.vo.BorrowerAttachVO;
import com.acho.srb.core.bean.vo.BorrowerDetailVO;
import com.acho.srb.core.bean.vo.BorrowerVO;
import com.acho.srb.core.enums.BorrowerStatusEnum;
import com.acho.srb.core.enums.IntegralEnum;
import com.acho.srb.core.mapper.BorrowerAttachMapper;
import com.acho.srb.core.mapper.BorrowerMapper;
import com.acho.srb.core.mapper.UserInfoMapper;
import com.acho.srb.core.mapper.UserIntegralMapper;
import com.acho.srb.core.service.BorrowerAttachService;
import com.acho.srb.core.service.BorrowerService;
import com.acho.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
@Service
@Slf4j
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private DictService dictService;

    @Resource
    private BorrowerAttachService borrowerAttachService;

    @Resource
    private BorrowerAttachMapper borrowerAttachMapper;

    @Resource
    private UserIntegralMapper userIntegralMapper;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId) {
        //整合borrowerVO和userInfo的数据

        log.info(borrowerVO.toString());

        UserInfo userInfo = userInfoMapper.selectById(userId);

        //保存借款人信息
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO, borrower);//合并
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());//认证中
        baseMapper.insert(borrower);

        //保存附件
        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        });

        //更新会员状态，更新为认证中
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer getStatusByUserId(Long userId) {
        QueryWrapper<Borrower> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status")
                .eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(queryWrapper);

        if (objects.size() == 0) {
            //借款人尚未提交信息
            return BorrowerStatusEnum.NO_AUTH.getStatus();
        }
        Integer status = (Integer) objects.get(0);
        return status;


    }

    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword) {
        //关键字（手机，姓名，或者身份证）
        QueryWrapper<Borrower> objectQueryWrapper = new QueryWrapper<>();

        //判断是否为空
        if (StringUtils.isEmpty(keyword)) {
            return baseMapper.selectPage(pageParam, null);
        }

        objectQueryWrapper.like("name", keyword)
                .or().like("id_card", keyword)
                .or().like("mobile", keyword)
                .orderByDesc("id");


        return baseMapper.selectPage(pageParam, objectQueryWrapper);
    }

    @Override
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {
        //获取借款人信息
        Borrower borrower = baseMapper.selectById(id);

        //填充基本借款人信息
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower, borrowerDetailVO);


        //婚否(因为和BorrowerDetailVO的类型不一样)
        borrowerDetailVO.setMarry(borrower.getMarry()?"是":"否");
        //性别（~）
        borrowerDetailVO.setSex(borrower.getSex()==1?"男":"女");

        //计算下拉列表选中内容
        String education = dictService.getNameByParentDictCodeAndValue("education", borrower.getEducation());
        String industry = dictService.getNameByParentDictCodeAndValue("moneyUse", borrower.getIndustry());
        String income = dictService.getNameByParentDictCodeAndValue("income", borrower.getIncome());
        String returnSource = dictService.getNameByParentDictCodeAndValue("returnSource", borrower.getReturnSource());
        String contactsRelation = dictService.getNameByParentDictCodeAndValue("relation", borrower.getContactsRelation());

        //设置下拉列表选中内容
        borrowerDetailVO.setEducation(education);
        borrowerDetailVO.setIndustry(industry);
        borrowerDetailVO.setIncome(income);
        borrowerDetailVO.setReturnSource(returnSource);
        borrowerDetailVO.setContactsRelation(contactsRelation);

        //审批状态
        String status = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
        borrowerDetailVO.setStatus(status);


        //获取附件VO列表
        List<BorrowerAttachVO> borrowerAttachVOList =  borrowerAttachService.selectBorrowerAttachVOList(id);
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachVOList);

        return borrowerDetailVO;

    }

    /**
     * 执行流程：
     * 1.borrowerApprovalVO(前端获取)--->
     *                    利用其Id获取Borrower对象并修改其状态更新表中的Borrower
     * 2.利用Borrower获取UserId，获取相应UserInfO对象---->
     * 3.根据转过来的信息判断是否给积分加分
     * 4.最后修改UserInfo表的审核状态和积分
     * @param borrowerApprovalVO
     */
    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {

        //借款人认证状态
        Long borrowerId = borrowerApprovalVO.getBorrowerId();
        Borrower borrower = baseMapper.selectById(borrowerId);
        borrower.setStatus(borrowerApprovalVO.getStatus());
        baseMapper.updateById(borrower);

        Long userId = borrower.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //添加积分
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        userIntegral.setIntegral(borrowerApprovalVO.getInfoIntegral());
        userIntegral.setContent("借款人基本信息");
        userIntegralMapper.insert(userIntegral);

        int curIntegral = userInfo.getIntegral() + borrowerApprovalVO.getInfoIntegral();
        if(borrowerApprovalVO.getIsIdCardOk()) {
            curIntegral += IntegralEnum.BORROWER_IDCARD.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insert(userIntegral);
        }

        if(borrowerApprovalVO.getIsHouseOk()) {
            curIntegral += IntegralEnum.BORROWER_HOUSE.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insert(userIntegral);
        }

        if(borrowerApprovalVO.getIsCarOk()) {
            curIntegral += IntegralEnum.BORROWER_CAR.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insert(userIntegral);
        }

        userInfo.setIntegral(curIntegral);
        //修改审核状态
        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        userInfoMapper.updateById(userInfo);

    }
}
