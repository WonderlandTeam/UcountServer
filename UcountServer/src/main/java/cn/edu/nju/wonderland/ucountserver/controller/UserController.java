package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.UserService;
import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
    public UserInfoVO login(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam(required = false) String userAgent) {
        return userService.login(username, password, userAgent);
    }

    @ApiOperation(value = "创建用户", notes = "根据SignUpVO创建用户")
    @ApiImplicitParam(name = "signUpVO", value = "用户注册信息vo", required = true, dataType = "SignUpVO")
    @PostMapping
    public void signUp(@RequestBody SignUpVO signUpVO) {
        userService.signUp(signUpVO);
//        return "注册成功";
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据用户名获取用户详细信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping(value = "/{username}")
    public UserInfoVO getUserInfo(@PathVariable String username) {
        return userService.getUserInfo(username);
    }

    @ApiOperation(value = "更改用户详细信息", notes = "根据用户名更新相应用户,根据传的userModifyVO更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userModifyVO", value = "用户更新信息vo", required = true, dataType = "UserInfoVO")
    })
    @PutMapping(value = "/{username}")
    public void modifyUserInfo(@PathVariable String username,
                               @RequestBody UserInfoVO userInfoVO) {
        userService.modifyUserInfo(username,userInfoVO);
//        return "修改成功";
    }

}
