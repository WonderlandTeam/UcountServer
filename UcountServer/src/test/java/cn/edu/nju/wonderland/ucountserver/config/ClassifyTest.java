package cn.edu.nju.wonderland.ucountserver.config;

import cn.edu.nju.wonderland.ucountserver.entity.Alipay;
import cn.edu.nju.wonderland.ucountserver.entity.IcbcCard;
import cn.edu.nju.wonderland.ucountserver.entity.SchoolCard;
import cn.edu.nju.wonderland.ucountserver.repository.AlipayRepository;
import cn.edu.nju.wonderland.ucountserver.repository.IcbcCardRepository;
import cn.edu.nju.wonderland.ucountserver.repository.SchoolCardRepository;
import cn.edu.nju.wonderland.ucountserver.util.BillType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassifyTest {

    @Autowired
    public AlipayRepository alipayRepository;

    @Autowired
    public IcbcCardRepository icbcCardRepository;

    @Autowired
    public SchoolCardRepository schoolCardRepository;


    @Test
    public void testClassifyAlipay() {
        Alipay alipay = alipayRepository.findOne(1L);
        BillType billType = Classify.classifyAlipay(alipay);
        System.out.println(billType);
    }

    @Test
    public void testClassifyICBC() {
        IcbcCard icbcCard = icbcCardRepository.findOne(1L);
        BillType billType = Classify.classifyICBC(icbcCard);
        System.out.println(billType);
    }

    @Test
    public void testClassifySchoolCard() {
        SchoolCard schoolCard = schoolCardRepository.findOne(1L);
        BillType billType = Classify.classifySchoolCard(schoolCard);
        System.out.println(billType);
    }

}
