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

    @ApiOperation(value = "创建用户", notes = "根据SignUpVO创建用户")
    @ApiImplicitParam(name = "signUpVO", value = "用户注册信息vo", required = true, dataType = "SignUpVO")
    @PostMapping
    public Map<String, Object> signUp(@RequestBody SignUpVO signUpVO) {
        return null;
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据url中用户id获取用户详细信息")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long")
    @GetMapping(value = "/{user_id}")
    public Map<String, Object> getUserInfo(@PathVariable("user_id") Long userId) {
        return null;
    }

    @ApiOperation(value = "更改用户详细信息", notes = "根据url中用户id更新响应用户,根据传的userModifyVO更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "userModifyVO", value = "用户更新信息vo", required = true, dataType = "UserModifyVO")
    })
    @PutMapping(value = "/{user_id}")
    public Map<String, Object> modifyUserInfo(@PathVariable("user_id") Long userId,
                                              @RequestBody UserModifyVO userModifyVO) {
        return null;
    }

}
