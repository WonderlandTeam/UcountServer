package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static cn.edu.nju.wonderland.ucountserver.util.DateHelper.DATE_TIME_FORMATTER;
import static org.junit.Assert.assertEquals;

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
        List<BillInfoVO> billInfoVOList = billService.getMonthBillsByUser("bian","2017-08-01 00:00:00");
        LocalDateTime start = LocalDateTime.parse("2017-08-01 00:00:00", DATE_TIME_FORMATTER);
        LocalDateTime end = start.plus(1, ChronoUnit.MONTHS);
        for(BillInfoVO billInfoVO : billInfoVOList){
            if(LocalDateTime.parse(billInfoVO.time,DATE_TIME_FORMATTER).isAfter(end)||LocalDateTime.parse(billInfoVO.time,DATE_TIME_FORMATTER).isBefore(start)){
                assertEquals(false,true);
            }
        }
    }
}
