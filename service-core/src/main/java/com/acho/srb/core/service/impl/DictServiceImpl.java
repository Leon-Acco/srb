package com.acho.srb.core.service.impl;

import com.acho.srb.core.bean.Dict;
import com.acho.srb.core.bean.dto.ExcelDictDTO;
import com.acho.srb.core.listener.ExcelDictDTOListener;
import com.acho.srb.core.mapper.DictMapper;
import com.acho.srb.core.service.DictService;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.reflection.ExceptionUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private DictMapper dictMapper;

    @Transactional(rollbackFor = Exception.class)//出现任何异常都回滚
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDTO.class, new ExcelDictDTOListener(dictMapper)).sheet().doRead();
        log.info("Excel导入成功！！！");
    }

    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Dict> dictList = dictMapper.selectList(null);
        //将这个数据转换为ExcelDictDTO
        //创建ExcelDictDTO列表，将Dict列表转换成ExcelDictDTO列表
        ArrayList<ExcelDictDTO> excelDictDTOList = new ArrayList<>(dictList.size());
        dictList.forEach(dict -> {

            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict, excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        });
        return excelDictDTOList;

    }

    @Override
    public List<Dict> listByParentId(Long parentId) {
        try {
            //查询数据字典中是否有数据
            List<Dict> dictList = (List<Dict>) redisTemplate.opsForValue().get("srb-core-dictList"+parentId);
            if (dictList!=null){
                log.info("从redis中获取数据列表");
                return dictList;
            }
        } catch (Exception e) {
            //出现异常显示信息，往下执行用数据库去查
            log.error("redis出现异常", ExceptionUtils.getStackTrace(e));
        }

        //redis数据库中没有，用Mysql去查
        log.info("从Mysql中获取数据列表");
        QueryWrapper<Dict> parent_id = new QueryWrapper<Dict>().eq("parent_id", parentId);
        List<Dict> dicts = dictMapper.selectList(parent_id);
        dicts.forEach(dict -> {
            //如果有子节点，则是非叶子节点(判断有没子节点)
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });


        try {
            log.info("将Mysql查出的数据存入redis中");
            redisTemplate.opsForValue().set("srb-core-dictList"+parentId,dicts,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("无法往redis中存入数据，redis异常", ExceptionUtils.getStackTrace(e));
        }


        return dicts;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        return this.listByParentId(dict.getId());
    }

    /**
     * 执行流程：利用dictCode查询出父节点对象--->获取父节点ID---->利用父节点Id和value，定位出子节点的名称
     * @param dictCode
     * @param value
     * @return
     */
    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<Dict>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Dict parentDict = baseMapper.selectOne(dictQueryWrapper);

        if(parentDict == null) {
            return "";
        }

        dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper
                .eq("parent_id", parentDict.getId())
                .eq("value", value);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);

        if(dict == null) {
            return "";
        }

        return dict.getName();
    }

    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<Dict>().eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
}
