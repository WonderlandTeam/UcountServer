package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserModifyVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @ApiOperation(value = "用户登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password) {
        return null;
    }

    @ApiOperation(value = "创建用户", notes = "根据SignUpVO创建用户")
    @ApiImplicitParam(name = "signUpVO", value = "用户注册信息vo", required = true, dataType = "SignUpVO")
    @PostMapping
    public Map<String, Object> signUp(@RequestBody SignUpVO signUpVO) {
        return null;
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据用户名获取用户详细信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping(value = "/{username}")
    public Map<String, Object> getUserInfo(@PathVariable String username) {
        return null;
    }

    @ApiOperation(value = "更改用户详细信息", notes = "根据用户名更新相应用户,根据传的userModifyVO更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userModifyVO", value = "用户更新信息vo", required = true, dataType = "UserModifyVO")
    })
    @PutMapping(value = "/{username}")
    public Map<String, Object> modifyUserInfo(@PathVariable String username,
                                              @RequestBody UserModifyVO userModifyVO) {
        return null;
    }

}
