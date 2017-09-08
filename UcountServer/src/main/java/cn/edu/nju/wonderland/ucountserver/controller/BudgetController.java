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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.edu.nju.wonderland.ucountserver.util.KeyName.CONTENT;
import static cn.edu.nju.wonderland.ucountserver.util.KeyName.MESSAGE;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    @ApiOperation(value = "获取单个预算信息", notes = "根据预算id获取预算信息")
    @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long")
    @GetMapping("/{budget_id}")
    public Map<String, Object> getBudget(@PathVariable("budget_id") Long budgetId) {
        Map<String, Object> result = new HashMap<>();
        BudgetInfoVO budgetInfoVO=budgetService.getBudget(budgetId);
        result.put(CONTENT,budgetInfoVO);
        return result;
    }

    @ApiOperation(value = "获取用户当月之后的所有预算信息", notes = "根据用户名获取预算信息，月份可选（可指定获取某个月份的预算）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "yyyy-mm (比如\"2017-07\")", dataType = "String")
    })
    @GetMapping
    public Map<String, Object> getBudgetsByUser(@RequestParam String username,
                                                @RequestParam(required = false) String date) {
        Map<String, Object> result = new HashMap<>();
        List<BudgetInfoVO> vos=null;
        if(date==null){
            vos=budgetService.getBudgetsByUser(username);
        }else{
            vos=budgetService.getBudgetsByUserAndTime(username,date);
        }
        result.put(CONTENT,vos);
        return result;
    }

    @ApiOperation(value = "用户添加预算")
    @ApiImplicitParam(name = "budgetAddVO", value = "预算添加信息VO,其中设置的时间格式仍为yyyy-mm", required = true, dataType = "BudgetAddVO")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Map<String, Object> addBudget(@RequestBody BudgetAddVO budgetAddVO) {
        Map<String, Object> result = new HashMap<>();
        Long budgetID=budgetService.addBudget(budgetAddVO);
        result.put(CONTENT,budgetID);
        return result;
    }

    @ApiOperation(value = "更新预算信息", notes = "根据预算id更新预算信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "budgetModifyVO", value = "预算更新信息vo", required = true, dataType = "BudgetModifyVO")
    })
    @PostMapping("/{budget_id}")
    public Map<String, Object> updateBudget(@PathVariable("budget_id") Long budgetId,
                                            @RequestBody BudgetModifyVO budgetModifyVO) {
        Map<String, Object> result = new HashMap<>();
        budgetService.updateBudget(budgetId,budgetModifyVO);
        result.put(MESSAGE,"更新预算成功");
        return result;
    }

    //在实际测试中delete只有返回的状态，没有message
    @ApiOperation(value = "删除预算", notes = "根据预算id删除预算")
    @ApiImplicitParam(name = "budgetId", value = "预算id", required = true, dataType = "Long")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{budget_id}")
    public Map<String, Object> deleteBudget(@PathVariable("budget_id") Long budgetId) {
        Map<String, Object> result = new HashMap<>();
        budgetService.deleteBudget(budgetId);
        result.put(MESSAGE,"删除预算成功");
        return result;
    }

}
