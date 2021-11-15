package com.acho.srb.core.controller.admin;


import com.acho.common.exception.BusinessException;
import com.acho.common.result.R;
import com.acho.common.result.ResponseEnum;
import com.acho.srb.core.bean.Dict;
import com.acho.srb.core.bean.dto.ExcelDictDTO;
import com.acho.srb.core.service.DictService;
import com.alibaba.excel.EasyExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
@Api(tags = "数据字典管理")
@RestController
@RequestMapping("/admin/core/dict")
@Slf4j
//@CrossOrigin
public class AdminDictController {
    @Resource
    private DictService dictService;

    @ApiOperation("数据的批量导入")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "Excel数据文件",required = true)
            @RequestParam("file") MultipartFile multipartFile){
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            dictService.importData(inputStream);
            return  R.ok().message("数据导入成功！");
        } catch (Exception e) {
            throw  new BusinessException(ResponseEnum.EXPORT_DATA_ERROR,e);
        }

    }

    @ApiOperation("数据的导出")
    @GetMapping("/export")
    public void exportData(HttpServletResponse response){
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("数据字典", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("数据字典").doWrite(dictService.listDictData());
        } catch (IOException e) {
            throw  new BusinessException(ResponseEnum.EXPORT_DATA_ERROR, e);
        }

    }


    @ApiOperation("根据上级id获取子节点数据列表")
    @GetMapping("/listByParentId/{parentId}")
    public R listByParentId(
            @ApiParam(value = "上级节点id", required = true)
            @PathVariable Long parentId) {
        List<Dict> dictList = dictService.listByParentId(parentId);
        return R.ok().data("list", dictList);
    }
}

