package cn.edu.nju.wonderland.ucountserver.stub;

import cn.edu.nju.wonderland.ucountserver.service.BillService;
import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by green-cherry on 2017/8/25.
 */

@Service
public class BillServiceStub implements BillService {
    @Override
    public BillInfoVO getBill(Long accountId, Long billId) {
        return null;
    }

    @Override
    public List<BillInfoVO> getBillsByAccount(Long accountId, Pageable pageable) {
        return null;
    }

    @Override
    public List<BillInfoVO> getBillsByUser(String username, Pageable pageable) {
        return null;
    }

    @Override
    public Long addBillManually(Long accountId, BillAddVO billAddVO) {
        return null;
    }

    @Override
    public List<BillInfoVO> addBillAutomatically(Long accountId) {
        return null;
    }

    @Override
    public void deleteBill(Long accountId, Long billId) {

    }

    @Override
    public double getConsumedMoneyByTypeAndTime(String username, String consumeType, String time) {
        return 100;
    }
}
