package cn.edu.nju.wonderland.ucountserver.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statements")
public class StatementController {

    @ApiOperation(value = "获取报表", notes = "根据用户名获取报表（参数待定）")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping
    public Map<String, Object> getStatement(@RequestParam String username) {
        return null;
    }

}
