package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.BudgetAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetModifyVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @ApiOperation(value = "获取单个预算信息", notes = "根据预算id获取预算信息")
    @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long")
    @GetMapping("/{budget_id}")
    public Map<String, Object> getBudget(@PathVariable("budget_id") Long budgetId) {
        return null;
    }

    @ApiOperation(value = "获取用户所有预算信息", notes = "根据用户名获取预算信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "date", value = "年份-月份（待定）", dataType = "String")
    })
    @GetMapping
    public Map<String, Object> getBudgetsByUser(@RequestParam String username,
                                                @RequestParam(required = false) String date) {
        return null;
    }

    @ApiOperation(value = "用户添加预算")
    @ApiImplicitParam(name = "budgetAddVO", value = "预算添加信息VO", required = true, dataType = "BudgetAddVO")
    @PostMapping
    public Map<String, Object> addBudget(@RequestBody BudgetAddVO budgetAddVO) {
        return null;
    }

    @ApiOperation(value = "更新预算信息", notes = "根据预算id更新预算信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "budgetModifyVO", value = "预算更新信息vo", required = true, dataType = "BudgetModifyVO")
    })
    @PutMapping("/{budget_id}")
    public Map<String, Object> updateBudget(@PathVariable("budget_id") Long budgetId,
                                            @RequestBody BudgetModifyVO budgetModifyVO) {
        return null;
    }

    @ApiOperation(value = "删除预算", notes = "根据预算id删除预算")
    @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long")
    @DeleteMapping("/{budget_id}")
    public Map<String, Object> deleteBudget(@PathVariable("budget_id") Long budgetId) {
        return null;
    }

}
