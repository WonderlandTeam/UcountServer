package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.vo.AccountAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.edu.nju.wonderland.ucountserver.util.KeyName.CONTENT;
import static cn.edu.nju.wonderland.ucountserver.util.KeyName.MESSAGE;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "获取资产账户信息", notes = "根据账户id获取账户信息")
    @ApiImplicitParam(name = "accountId", value = "账户id", required = true, dataType = "Long")
    @GetMapping("/{account_id}")
    public Map<String, Object> getAccount(@PathVariable("account_id") Long accountId) {
        Map<String, Object> result = new HashMap<>();
        AccountInfoVO accountInfoVO = accountService.getAccountById(accountId);
        result.put(CONTENT, accountInfoVO);
        return result;
    }

    @ApiOperation(value = "获取某用户所有资产账户信息", notes = "根据用户名获取其资产账户信息列表")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping
    public Map<String, Object> getAccountsByUser(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        List<AccountInfoVO> accountInfoVOList = accountService.getAccountsByUser(username);
        result.put(CONTENT, accountInfoVOList);
        return result;
    }

    @ApiOperation(value = "添加资产账户")
    @ApiImplicitParam(name = "accountAddVO", value = "账户添加信息vo", required = true, dataType = "AccountAddVO")
    @PostMapping
    public Map<String, Object> addAccount(@RequestBody AccountAddVO vo) {
        Map<String, Object> result = new HashMap<>();
        Long id = accountService.addAccount(vo);
        result.put(CONTENT, id);
        return result;
    }

    @ApiOperation(value = "删除资产账户", notes = "根据账户id删除账户")
    @ApiImplicitParam(name = "accountId", value = "账户id", required = true, dataType = "Long")
    @DeleteMapping("/{account_id}")
    public Map<String, Object> deleteAccount(@PathVariable("account_id") Long accountId) {
        accountService.deleteAccount(accountId);
        Map<String, Object> result = new HashMap<>();
        result.put(MESSAGE, "删除成功");
        return result;
    }

}
