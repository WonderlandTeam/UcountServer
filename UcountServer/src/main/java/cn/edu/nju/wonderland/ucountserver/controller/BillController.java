package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BillController {

    /**
     * 获取某条账目信息
     */
    @GetMapping("accounts/{account_id}/bills/{bill_id}")
    public Map<String, Object> getBill(@PathVariable("account_id") Long accountId,
                                       @PathVariable("bill_id") Long billId) {
        return null;
    }

    /**
     * 获取某资产账户账目信息
     */
    @GetMapping("accounts/{account_id}/bills")
    public Map<String, Object> getBillsByAccount(@PathVariable("account_id") Long accountId,
                                                 Pageable pageable) {
        return null;
    }

    /**
     * 获取用户所有资产账户账目信息
     */
    @GetMapping("users/{user_id}/bills")
    public Map<String, Object> getBillsByUser(@PathVariable Long userId,
                                              Pageable pageable) {
        return null;
    }

    /**
     * 手动／自动记账
     */
    @PostMapping("accounts/{account_id}/bills")
    public Map<String, Object> addBill(@PathVariable("account_id") Long accountId,
                                       @RequestBody(required = false) BillAddVO billAddVO) {
        return null;
    }

    /**
     * 删除账目
     */
    @DeleteMapping("accounts/{account_id}/bills/{bill_id}")
    public Map<String, Object> deleteBill(@PathVariable("account_id") Long accountId,
                                          @PathVariable("bill_id") Long billId) {
        return null;
    }

}
