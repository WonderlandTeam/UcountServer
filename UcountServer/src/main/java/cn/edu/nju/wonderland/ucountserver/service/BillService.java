package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface BillService {

    /**
     * 获取账目信息
     * @param accountId         账户id
     * @param billId            账目id
     * @return                  账目信息vo
     */
    BillInfoVO getBill(Long accountId, Long billId);

    /**
     * 获取账户所有账目信息
     * @param accountId         账户id
     * @param pageable          筛选信息
     * @return                  账目列表
     */
    List<BillInfoVO> getBillsByAccount(Long accountId, Pageable pageable);

    /**
     * 获取账户所有账目信息
     * @param userId            用户id
     * @param pageable          筛选信息
     * @return                  账目列表
     */
    List<BillInfoVO> getBillsByUser(String username, Pageable pageable);

    /**
     * 用户手动记账
     * @param accountId         账户id
     * @param billAddVO         账目添加vo
     * @return                  新增账目id
     */
    Long addBillManually(Long accountId, BillAddVO billAddVO);

    /**
     * 用户实时同步记账
     * @param accountId         账户id
     * @return                  同步账目信息列表
     */
    List<BillInfoVO> addBillAutomatically(Long accountId);

    /**
     * 删除账目
     * @param accountId         账户id
     * @param billId            账目id
     */
    void deleteBill(Long accountId, Long billId);

}
