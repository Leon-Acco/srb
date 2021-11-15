package com.acho.srb.core.controller.admin;


import com.acho.common.result.R;
import com.acho.srb.core.bean.Lend;
import com.acho.srb.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author Acho
 * @since 2021-10-26
 */

@Api(tags = "标的管理")
@RestController
@RequestMapping("/admin/core/lend")
@Slf4j
public class AdminLendController {

    @Resource
    private LendService lendService;

    @ApiOperation("标的列表")
    @GetMapping("/list")
    public R list() {
        List<Lend> lendList = lendService.selectList();
        return R.ok().data("list", lendList);
    }

    @ApiOperation("获取标的信息")
    @GetMapping("/show/{id}")
    public R xxxx(
            @ApiParam(value = "标的id", required = true)
            @PathVariable long id) {

        Map<String, Object> result = lendService.getLendDetail(id);
        return R.ok().data("lendDetail", result);
    }


    @ApiOperation("放款")
    @GetMapping("/makeLoan/{id}")
    public R makeLoan(@ApiParam(value = "标的id", required = true)

                      @PathVariable("id") Long id) {
        lendService.makeLoan(id);
        return R.ok().message("放款成功");
    }
}
