package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.service.BillService;
import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    @Override
    public BillInfoVO getBill(Long accountId, Long billId) {
        return null;
    }

    @Override
    public List<BillInfoVO> getBillsByAccount(Long accountId, Pageable pageable) {
        return null;
    }

    @Override
    public List<BillInfoVO> getBillsByUser(Long userId, Pageable pageable) {
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
}
