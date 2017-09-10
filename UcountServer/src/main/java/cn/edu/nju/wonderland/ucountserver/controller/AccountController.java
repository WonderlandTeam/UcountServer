package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.vo.AccountAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public AccountInfoVO getAccount(@PathVariable("account_id") Long accountId) {
        return accountService.getAccountById(accountId);
    }

    @ApiOperation(value = "获取某用户所有资产账户信息", notes = "根据用户名获取其资产账户信息列表")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping
    public List<AccountInfoVO> getAccountsByUser(@RequestParam String username) {
        return accountService.getAccountsByUser(username);
    }

    @ApiOperation(value = "添加资产账户")
    @ApiImplicitParam(name = "accountAddVO", value = "账户添加信息vo", required = true, dataType = "AccountAddVO")
    @PostMapping
    public Long addAccount(@RequestBody AccountAddVO vo) {
        return accountService.addAccount(vo);
    }

    @ApiOperation(value = "删除资产账户", notes = "根据账户id删除账户")
    @ApiImplicitParam(name = "accountId", value = "账户id", required = true, dataType = "Long")
    @DeleteMapping("/{account_id}")
    public String deleteAccount(@PathVariable("account_id") Long accountId) {
        accountService.deleteAccount(accountId);
        return "删除成功";
    }

}
