package com.acho.srb.core.controller;


import com.acho.srb.core.bean.IntegralGrade;
import com.acho.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
@Api(tags = "网站积分管理")
@RestController
@RequestMapping("/api/core/integralGrade")
public class IntegralGradeController {

    @Resource
    private IntegralGradeService integralGradeService;
    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public List<IntegralGrade> listAll(){
        return integralGradeService.list();
    }
}

