package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BillController {

    @ApiOperation(value = "获取单条账目信息", notes = "根据资产账户id和账目id获取单条账目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "账户id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "billId", value = "账目id", required = true, dataType = "Long")
    })
    @GetMapping("accounts/{account_id}/bills/{bill_id}")
    public Map<String, Object> getBill(@PathVariable("account_id") Long accountId,
                                       @PathVariable("bill_id") Long billId) {
        return null;
    }

    @ApiOperation(value = "获取资产账户账目列表", notes = "根据资产账户id及筛选条件获取账目信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "账目id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "pageable", value = "过滤信息", dataType = "Pageable")
    })
    @GetMapping("accounts/{account_id}/bills")
    public Map<String, Object> getBillsByAccount(@PathVariable("account_id") Long accountId,
                                                 Pageable pageable) {
        return null;
    }

    @ApiOperation(value = "获取用户某月账户账目列表", notes = "根据用户名及月份获取账目信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "month", value = "年份-月份，如\"2017-09\"", required = false, dataType = "String")
    })
    @GetMapping("users/{username}/bills")
    public Map<String, Object> getBillsByUser(@PathVariable String username,
                                              @RequestParam(required = false) String month) {
        return null;
    }

    @ApiOperation(value = "用户手动／自动记账", notes = "根据资产账户id添加账目记录，请求内容为空则为自动记账")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "账户id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "billAddVO", value = "添加账目信息vo", dataType = "BillAddVO")
    })
    @PostMapping("accounts/{account_id}/bills")
    public Map<String, Object> addBill(@PathVariable("account_id") Long accountId,
                                       @RequestBody(required = false) BillAddVO billAddVO) {
        return null;
    }

    @ApiOperation(value = "删除某条账目信息", notes = "根据资产账户id和账目id删除单条账目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "账户id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "billId", value = "账目id", dataType = "Long")
    })
    @DeleteMapping("accounts/{account_id}/bills/{bill_id}")
    public Map<String, Object> deleteBill(@PathVariable("account_id") Long accountId,
                                          @PathVariable("bill_id") Long billId) {
        return null;
    }

}
