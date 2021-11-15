package com.acho.srb.core.service;

import com.acho.srb.core.bean.UserInfo;
import com.acho.srb.core.bean.query.UserInfoQuery;
import com.acho.srb.core.bean.vo.LoginVO;
import com.acho.srb.core.bean.vo.RegisterVO;
import com.acho.srb.core.bean.vo.UserIndexVO;
import com.acho.srb.core.bean.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 注册表单提交
     * @param registerVO
     */
    void register(RegisterVO registerVO);

    /**
     * 登陆认证
     * @param loginVO
     * @param ip
     * @return
     */
    UserInfoVO login(LoginVO loginVO, String ip);

    /**
     * 会员显示
     * @param infoPage
     * @param userInfoQuery
     * @return
     */
    IPage<UserInfo> listPage(Page<UserInfo> infoPage, UserInfoQuery userInfoQuery);

    /**
     * 修改会员的状态
     * @param id
     * @param status
     */
    void lock(Long id, Integer status);

    boolean checkMobil(String mobil);

    UserIndexVO getIndexUserInfo(Long userId);

    String getMobileByBindCode(String bindCode);
}
