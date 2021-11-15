package com.acho.srb.core.service;

import com.acho.srb.core.bean.Borrower;
import com.acho.srb.core.bean.vo.BorrowerApprovalVO;
import com.acho.srb.core.bean.vo.BorrowerDetailVO;
import com.acho.srb.core.bean.vo.BorrowerVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface BorrowerService extends IService<Borrower> {

    void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId);

    Integer getStatusByUserId(Long userId);

    IPage<Borrower> listPage (Page<Borrower> pageParam, String keyword);

    BorrowerDetailVO getBorrowerDetailVOById(Long id);

    void approval(BorrowerApprovalVO borrowerApprovalVO);



}
