package com.acho.srb.core.service;

import com.acho.srb.core.bean.BorrowerAttach;
import com.acho.srb.core.bean.vo.BorrowerAttachVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {

    List<BorrowerAttachVO> selectBorrowerAttachVOList(Long id);
}
