package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.TaskAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskModifyVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 其中涉及到的时间格式为yyyy-MM-dd
 * 如果已攒金额小于0，说明已经超额消费，根本攒不到钱
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @ApiOperation(value = "获取单个计划信息", notes = "根据计划id获取计划信息")
    @ApiImplicitParam(name = "taskId", value = "计划id", required = true, dataType = "Long")
    @GetMapping("/{task_id}")
    public Map<String, Object> getTask(@PathVariable("task_id") Long taskId) {
        return null;
    }


    @ApiOperation(value = "获取用户不同状态的攒钱计划信息", notes = "根据状态获取计划信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
    })
    @GetMapping("/{task_state}")
    public Map<String, Object> getTasksByState(@RequestParam String username,@PathVariable("task_state") Long taskState) {
        return null;
    }

    @ApiOperation(value = "获取用户所有攒钱计划信息", notes = "根据用户名获取计划信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
    })
    @GetMapping
    public Map<String, Object> getTasksByUser(@RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "用户添加攒钱计划")
    @ApiImplicitParam(name = "taskAddVO", value = "攒钱计划添加信息VO", required = true, dataType = "TaskAddVO")
    @PostMapping
    public Map<String, Object> addTask(@RequestBody TaskAddVO taskAddVO) {
        return null;
    }

    @ApiOperation(value = "更新攒钱计划", notes = "根据计划id更新攒钱计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "计划id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "taskModifyVO", value = "计划更新信息vo", required = true, dataType = "TaskModifyVO")
    })
    @PutMapping("/{task_id}")
    public Map<String, Object> updateTask(@PathVariable("task_id") Long taskId,
                                            @RequestBody TaskModifyVO taskModifyVO) {
        return null;
    }

    @ApiOperation(value = "删除攒钱计划", notes = "根据计划id删除预算")
    @ApiImplicitParam(name = "taskId", value = "计划id", required = true, dataType = "Long")
    @DeleteMapping("/{task_id}")
    public Map<String, Object> deleteTask(@PathVariable("task_id") Long taskId) {
        return null;
    }
}
