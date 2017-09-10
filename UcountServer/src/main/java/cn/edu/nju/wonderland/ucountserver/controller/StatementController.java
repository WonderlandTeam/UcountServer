package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.StatementService;
import cn.edu.nju.wonderland.ucountserver.vo.BalanceSheetVO;
import cn.edu.nju.wonderland.ucountserver.vo.CashFlowItemVO;
import cn.edu.nju.wonderland.ucountserver.vo.IncomeStatementVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public BalanceSheetVO getBalanceSheet(@RequestParam String username,
                                          @RequestParam(required = false) String date) {
        return statementService.getBalanceSheet(username, date);
    }

    @ApiOperation(value = "获取收支储蓄表（利润表）", notes = "根据用户名及起始日期获取收支储蓄表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "beginDate", value = "开始日期，格式为：yyyy-MM-dd", required = true, dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "截止日期，格式为：yyyy-MM-dd", required = false, dataType = "String")
    })
    @GetMapping("/incomeStatement")
    public IncomeStatementVO getIncomeStatement(@RequestParam String username,
                                                @RequestParam String beginDate,
                                                @RequestParam(required = false) String endDate) {
        return statementService.getIncomeStatement(username, beginDate, endDate);
    }

    @ApiOperation(value = "获取现金流量表", notes = "根据用户名及起始日期获取现金流量表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "beginDate", value = "开始日期，格式为：yyyy-MM-dd", required = true, dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "截止日期，格式为：yyyy-MM-dd", required = false, dataType = "String")
    })
    @GetMapping("/cashFlows")
    public List<CashFlowItemVO> getCashFlowsStatement(@RequestParam String username,
                                                      @RequestParam String beginDate,
                                                      @RequestParam(required = false) String endDate) {
        return statementService.getCashFlows(username, beginDate, endDate);
    }

}
