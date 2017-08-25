package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.TaskService;
import cn.edu.nju.wonderland.ucountserver.vo.TaskAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TaskModifyVO;
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

/**
 * 其中涉及到的时间格式为yyyy-MM-dd
 * 如果已攒金额小于0，说明已经超额消费，根本攒不到钱
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @ApiOperation(value = "获取单个计划信息", notes = "根据计划id获取计划信息")
    @ApiImplicitParam(name = "taskId", value = "计划id", required = true, dataType = "Long")
    @GetMapping("/{task_id}")
    public Map<String, Object> getTask(@PathVariable("task_id") Long taskId) {
        Map<String, Object> result = new HashMap<>();
        TaskInfoVO taskInfoVO=taskService.getTask(taskId);
        result.put(CONTENT,taskInfoVO);
        return result;
    }


    @ApiOperation(value = "获取用户不同状态的攒钱计划信息", notes = "根据状态获取计划信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "taskState", value = "计划状态", required = true, dataType = "String")
    })
    @GetMapping("/states")
    public Map<String, Object> getTasksByState(@RequestParam String username,@RequestParam String taskState) {
        Map<String, Object> result = new HashMap<>();
        List<TaskInfoVO> taskInfoVOS=taskService.getTasksByState(username,taskState);
        result.put(CONTENT,taskInfoVOS);
        return result;
    }

    @ApiOperation(value = "获取用户所有攒钱计划信息", notes = "根据用户名获取计划信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
    })
    @GetMapping
    public Map<String, Object> getTasksByUser(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        List<TaskInfoVO> taskInfoVOS=taskService.getTasksByUser(username);
        result.put(CONTENT,taskInfoVOS);
        return result;
    }

    @ApiOperation(value = "用户添加攒钱计划")
    @ApiImplicitParam(name = "taskAddVO", value = "攒钱计划添加信息VO", required = true, dataType = "TaskAddVO")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Map<String, Object> addTask(@RequestBody TaskAddVO taskAddVO) {
        Map<String, Object> result = new HashMap<>();
        Long taskId=taskService.addTask(taskAddVO);
        result.put(CONTENT,taskId);
        return result;
    }

    @ApiOperation(value = "更新攒钱计划", notes = "根据计划id更新攒钱计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "计划id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "taskModifyVO", value = "计划更新信息vo", required = true, dataType = "TaskModifyVO")
    })
    @PostMapping("/{task_id}")
    public Map<String, Object> updateTask(@PathVariable("task_id") Long taskId,
                                            @RequestBody TaskModifyVO taskModifyVO) {
        Map<String, Object> result = new HashMap<>();
        taskService.updateTask(taskId,taskModifyVO);
        result.put(MESSAGE,"更新攒钱计划成功");
        return result;
    }

    @ApiOperation(value = "删除攒钱计划", notes = "根据计划id删除预算")
    @ApiImplicitParam(name = "taskId", value = "计划id", required = true, dataType = "Long")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{task_id}")
    public Map<String, Object> deleteTask(@PathVariable("task_id") Long taskId) {
        Map<String, Object> result = new HashMap<>();
        taskService.deleteTask(taskId);
        result.put(MESSAGE,"删除攒钱计划成功");
        return result;
    }
}
