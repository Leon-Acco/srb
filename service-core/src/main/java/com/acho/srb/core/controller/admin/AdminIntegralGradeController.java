package com.acho.srb.core.controller.admin;


import com.acho.common.result.R;
import com.acho.srb.core.bean.IntegralGrade;
import com.acho.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 管理员
 * 积分等级表 前端控制器
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */
@Slf4j
@Api(tags = "积分等级管理")
//@CrossOrigin//跨域
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {
    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public R listAll() {
        log.error("hahahahahaha");
        log.info("hahahahahaha");
        log.warn("hahahahahaha");
        log.debug("hahahahahaha");

        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list", list).message("查询列表成功");
    }

    /**
     * 执行逻辑删除
     */
    @ApiOperation(value = "根据id删除数据记录", notes = "逻辑删除数据记录")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(value = "数据id", example = "100")
                        @PathVariable("id") long id) {

        boolean b = integralGradeService.removeById(id);

        if (b) {
            return R.ok().message("删除成功！！");
        }

        return R.error().message("删除失败！！");
    }

    /**
     * 执行修改
     *
     * @param integralGrade
     * @return
     */
    @ApiOperation(value = "利用积分等级对象更新积分等级")
    @PutMapping("/update")
    public R updateById(@ApiParam(value = "积分等级对象", required = true)
                        @RequestBody IntegralGrade integralGrade) {


        boolean b = integralGradeService.updateById(integralGrade);
        if (b) {
            return R.ok().message("修改成功！！");
        }

        return R.error().message("修改失败！！");
    }

    /**
     * 利用id执行查询
     */
    @ApiOperation(value = "利用id查询积分等级")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam(value = "数据id", example = "1", required = true)
                     @PathVariable("id") long id) {

        IntegralGrade integralGrade = integralGradeService.getById(id);

        if (integralGrade != null) {
            return R.ok().data("record", integralGrade);
        } else {
            return R.error().message("数据不存在");
        }


    }

    /**
     * 执行新增积分等级对象
     */
    @ApiOperation("新增积分等级")
    @PostMapping("/save")
    public R save(
            @ApiParam(value = "积分等级对象", required = true)
            @RequestBody IntegralGrade integralGrade){
        boolean result = integralGradeService.save(integralGrade);
        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }
}

