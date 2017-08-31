package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
     * 获取用户所有账目信息
     * @param username            用户id
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

    /**
     * 获取指定月份指定消费类型的已消费金额
     * @param username          用户名
     * @param consumeType       消费类型
     * @param time              月份，格式：2017-08-01 00：00：00
     * @return
     */
    double getConsumedMoneyByTypeAndTime(String username,String consumeType,String time);

}
