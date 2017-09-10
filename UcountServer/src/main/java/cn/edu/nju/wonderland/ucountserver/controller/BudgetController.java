package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.BudgetService;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.BudgetModifyVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    @ApiOperation(value = "获取单个预算信息", notes = "根据预算id获取预算信息")
    @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long")
    @GetMapping("/{budget_id}")
    public BudgetInfoVO getBudget(@PathVariable("budget_id") Long budgetId) {
        return budgetService.getBudget(budgetId);
    }

    @ApiOperation(value = "获取用户当月之后的所有预算信息", notes = "根据用户名获取预算信息，月份可选（可指定获取某个月份的预算）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "yyyy-mm (比如\"2017-07\")", dataType = "String")
    })
    @GetMapping
    public List<BudgetInfoVO> getBudgetsByUser(@RequestParam String username,
                                               @RequestParam(required = false) String date) {
        if (date == null) {
            return budgetService.getBudgetsByUser(username);
        }
        return budgetService.getBudgetsByUserAndTime(username, date);
    }

    @ApiOperation(value = "用户添加预算")
    @ApiImplicitParam(name = "budgetAddVO", value = "预算添加信息VO,其中设置的时间格式仍为yyyy-mm", required = true, dataType = "BudgetAddVO")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Long addBudget(@RequestBody BudgetAddVO budgetAddVO) {
        return budgetService.addBudget(budgetAddVO);
    }

    @ApiOperation(value = "更新预算信息", notes = "根据预算id更新预算信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "budgetModifyVO", value = "预算更新信息vo", required = true, dataType = "BudgetModifyVO")
    })
    @PostMapping("/{budget_id}")
    public String updateBudget(@PathVariable("budget_id") Long budgetId,
                               @RequestBody BudgetModifyVO budgetModifyVO) {
        budgetService.updateBudget(budgetId, budgetModifyVO);
        return "预算修改成功";
    }

    //在实际测试中delete只有返回的状态，没有message
    @ApiOperation(value = "删除预算", notes = "根据预算id删除预算")
    @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{budget_id}")
    public String deleteBudget(@PathVariable("budget_id") Long budgetId) {
        budgetService.deleteBudget(budgetId);
        return "删除预算成功";
    }

}
