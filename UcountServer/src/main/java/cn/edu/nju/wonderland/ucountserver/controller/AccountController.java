package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.AccountAddVO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    /**
     * 获取账户信息
     */
    @GetMapping("/{account_id}")
    public Map<String, Object> getAccount(@PathVariable("account_id") Long accountId) {
        return null;
    }

    /**
     * 获取用户账户信息
     */
    @GetMapping
    public Map<String, Object> getAccountsByUser(@RequestParam Long userId) {
        return null;
    }

    /**
     * 添加账户
     */
    @PostMapping
    public Map<String, Object> addAccount(@RequestBody AccountAddVO accountAddVO) {
        return null;
    }

    /**
     * 删除账户
     */
    @DeleteMapping("/{account_id}")
    public Map<String, Object> deleteAccount(@PathVariable("account_id") Long accountId) {
        return null;
    }

}
