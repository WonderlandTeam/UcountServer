package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

}
