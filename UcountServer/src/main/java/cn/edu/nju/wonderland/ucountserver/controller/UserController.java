package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.UserService;
import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static cn.edu.nju.wonderland.ucountserver.util.KeyName.CONTENT;
import static cn.edu.nju.wonderland.ucountserver.util.KeyName.MESSAGE;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "用户登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password) {
        UserInfoVO userInfoVO = userService.login(username,password);
        Map<String,Object> result = new HashMap<>();
        result.put(CONTENT,userInfoVO);
        return result;
    }

    @ApiOperation(value = "创建用户", notes = "根据SignUpVO创建用户")
    @ApiImplicitParam(name = "signUpVO", value = "用户注册信息vo", required = true, dataType = "SignUpVO")
    @PostMapping
    public Map<String, Object> signUp(@RequestBody SignUpVO signUpVO) {
        String username = userService.signUp(signUpVO);
        Map<String,Object> result = new HashMap<>();
        result.put(CONTENT,username);
        return result;
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据用户名获取用户详细信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping(value = "/{username}")
    public Map<String, Object> getUserInfo(@PathVariable String username) {
        UserInfoVO userInfoVO = userService.getUserInfo(username);
        Map<String,Object> result = new HashMap<>();
        result.put(CONTENT,userInfoVO);
        return result;
    }

    @ApiOperation(value = "更改用户详细信息", notes = "根据用户名更新相应用户,根据传的userModifyVO更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userModifyVO", value = "用户更新信息vo", required = true, dataType = "UserModifyVO")
    })
    @PutMapping(value = "/{username}")
    public Map<String, Object> modifyUserInfo(@PathVariable String username,
                                              @RequestBody UserInfoVO userInfoVO) {
        Map<String,Object> result = new HashMap<>();
        userService.modifyUserInfo(username,userInfoVO);
        result.put(MESSAGE,"修改成功");
        return result;
    }

}
