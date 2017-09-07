package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Alipay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlipayRespositoryTest {

    @Autowired
    public AlipayRepository alipayRepository;

    @Test
    public void test() {
        Timestamp start = Timestamp.valueOf("2017-06-01 00:00:00");
        Timestamp end = Timestamp.valueOf("2017-07-01 00:00:00");
        List<Alipay> alipayList = alipayRepository.findByUsernameAndCreateTimeBetween("sigma", start, end);
        alipayList.forEach(e -> {
            System.out.println(e.getId() + "\t" + e.getPayTime() + "\t" + e.getConsumeType() + "\t" + e.getMoney());
        });
    }

}
