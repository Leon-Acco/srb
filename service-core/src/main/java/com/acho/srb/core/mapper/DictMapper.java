package com.acho.srb.core.mapper;

import com.acho.srb.core.bean.Dict;
import com.acho.srb.core.bean.dto.ExcelDictDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface DictMapper extends BaseMapper<Dict> {

    void insertBatch(List<ExcelDictDTO> list);


}
