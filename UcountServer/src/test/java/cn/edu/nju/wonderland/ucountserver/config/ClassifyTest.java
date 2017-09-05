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

import static org.junit.Assert.assertEquals;


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
    public void testClassifyAlipay1() {
        Alipay alipay = alipayRepository.findOne(2690L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(BillType.MANAGEMENT_INCOME,billType);
    }

    @Test
    public void testClassifyAlipay2() {
        Alipay alipay = alipayRepository.findOne(6178L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(BillType.DIET,billType);
    }
    @Test
    public void testClassifyAlipay3() {
        Alipay alipay = alipayRepository.findOne(6150L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(BillType.DIET,billType);
    }
    @Test
    public void testClassifyAlipay4() {
        Alipay alipay = alipayRepository.findOne(6121L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(BillType.OTHER_INCOME,billType);
    }
    @Test
    public void testClassifyAlipay5() {
        Alipay alipay = alipayRepository.findOne(6117L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(BillType.MANAGEMENT_INCOME,billType);
    }
    @Test
    public void testClassifyAlipay6() {
        Alipay alipay = alipayRepository.findOne(6060L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(BillType.DIET,billType);
    }

    @Test
    public void testClassifyAlipay7() {
        Alipay alipay = alipayRepository.findOne(6242L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(BillType.BOOK,billType);
    }
    @Test
    public void testClassifyICBC() {
        IcbcCard icbcCard = icbcCardRepository.findOne(133L);
        BillType billType = Classify.classifyICBC(icbcCard);
        assertEquals(BillType.DIET,billType);
    }

    @Test
    public void testClassifySchoolCard1() {
        SchoolCard schoolCard = schoolCardRepository.findOne(30L);
        BillType billType = Classify.classifySchoolCard(schoolCard);
        assertEquals(BillType.COMMODITY,billType);
    }

    @Test
    public void testClassifySchoolCard2() {
        SchoolCard schoolCard = schoolCardRepository.findOne(181L);
        BillType billType = Classify.classifySchoolCard(schoolCard);
        assertEquals(BillType.UTILITIES,billType);
    }

}
