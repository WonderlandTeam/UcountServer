package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BillServiceTest {

    @Autowired
    public BillService billService;

    @Test
    public void testGetBill() {
        BillInfoVO vo = billService.getBill(15L, 4325L);
        System.out.println(vo.amount);
//        System.out.println(vo.type);
        System.out.println(vo.time);
        System.out.println(vo.trader);
    }
    @Test
    public void testAddBill(){
//        BillAddVO billAddVO = new BillAddVO();
//        billAddVO.incomeExpenditure = 8.0;
//        billAddVO.commodity ="breakfast";
//        billAddVO.consumeType = "饮食";
//        billAddVO.remark = "take the breakfast";
//        billAddVO.time = "2017-9-15 00:00:00";
//        billService.addBillManually(27L, billAddVO);
    }
    @Test
    public void testDeleteBill(){
//        billService.deleteBill(27L, 5L);
    }
    @Test
    public void testGetBillsByAccount() {
        List<BillInfoVO> bills = billService.getBillsByAccount(1L, null);
        System.out.println(bills.size());
    }

    @Test
    public void testGetConsumedMoneyByTypeAndTime(){
        double result = billService.getConsumedMoneyByTypeAndTime("sigma","饮食","2017-09-11 00:00:00");
        System.out.println(result);
    }

    @Test
    public void testGetMonthBillsByUser(){
        List<BillInfoVO> billInfoVOList = billService.getMonthBillsByUser("sigma",null);
        System.out.println(billInfoVOList.size());
    }
}
