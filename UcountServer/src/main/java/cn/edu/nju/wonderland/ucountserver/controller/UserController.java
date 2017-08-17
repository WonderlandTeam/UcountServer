package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserModifyVO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * 添加用户／用户注册
     */
    @PostMapping
    public Map<String, Object> signUp(@RequestBody SignUpVO signUpVO) {
        return null;
    }

    /**
     * 获取用户信息
     */
    @GetMapping(value = "/{user_id}")
    public Map<String, Object> getUserInfo(@PathVariable("user_id") Long userId) {
        return null;
    }

    /**
     * 修改用户信息
     */
    @PutMapping(value = "/{user_id}")
    public Map<String, Object> modifyUserInfo(@PathVariable("user_id") Long userId,
                                              @RequestBody UserModifyVO userModifyVO) {
        return null;
    }

}
