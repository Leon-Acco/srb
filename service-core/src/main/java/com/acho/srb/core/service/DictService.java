package com.acho.srb.core.service;

import com.acho.srb.core.bean.Dict;
import com.acho.srb.core.bean.dto.ExcelDictDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
public interface DictService extends IService<Dict> {
    /**
     *读取Excel数据
     * @param inputStream
     */
    void  importData(InputStream inputStream);

    /**
     *查询数据库列表，封装
     * @return
     */
    List<ExcelDictDTO> listDictData();

    /**
     *利用parentId查询主节点数据
     * @param parentId
     * @return
     */
    List<Dict> listByParentId(Long parentId);

    List<Dict> findByDictCode(String dictCode);

    String getNameByParentDictCodeAndValue(String dictCode, Integer value);

}
