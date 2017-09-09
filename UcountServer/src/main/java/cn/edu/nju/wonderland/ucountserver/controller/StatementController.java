package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.StatementService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static cn.edu.nju.wonderland.ucountserver.util.KeyName.CONTENT;

@RestController
@RequestMapping("/statements")
public class StatementController {

    private final StatementService statementService;

    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @ApiOperation(value = "获取资产负债表", notes = "根据用户名及起始日期获取资产负债表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "日期，格式为：yyyy-MM-dd", required = false, dataType = "String"),
    })
    @GetMapping("/balanceSheet")
    public Map<String, Object> getBalanceSheet(@RequestParam String username,
                                               @RequestParam(required = false) String date) {
        Map<String, Object> result = new HashMap<>();
        result.put(CONTENT, statementService.getBalanceSheet(username, date));
        return result;
    }

    @ApiOperation(value = "获取收支储蓄表（利润表）", notes = "根据用户名及起始日期获取收支储蓄表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "beginDate", value = "开始日期，格式为：yyyy-MM-dd", required = true, dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "截止日期，格式为：yyyy-MM-dd", required = false, dataType = "String")
    })
    @GetMapping("/incomeStatement")
    public Map<String, Object> getIncomeStatement(@RequestParam String username,
                                                  @RequestParam String beginDate,
                                                  @RequestParam(required = false) String endDate) {
        Map<String, Object> result = new HashMap<>();
        result.put(CONTENT, statementService.getIncomeStatement(username, beginDate, endDate));
        return result;
    }

    @ApiOperation(value = "获取现金流量表", notes = "根据用户名及起始日期获取现金流量表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "beginDate", value = "开始日期，格式为：yyyy-MM-dd", required = true, dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "截止日期，格式为：yyyy-MM-dd", required = false, dataType = "String")
    })
    @GetMapping("/cashFlows")
    public Map<String, Object> getCashFlowsStatement(@RequestParam String username,
                                                     @RequestParam String beginDate,
                                                     @RequestParam(required = false) String endDate) {
        Map<String, Object> result = new HashMap<>();
        result.put(CONTENT, statementService.getCashFlows(username, beginDate, endDate));
        return result;
    }

}
