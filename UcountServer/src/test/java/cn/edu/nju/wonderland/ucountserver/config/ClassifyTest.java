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

import java.util.List;

import static cn.edu.nju.wonderland.ucountserver.util.BillType.*;
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
        assertEquals(MANAGEMENT_INCOME,billType);
    }

    @Test
    public void testClassifyAlipay2() {
        Alipay alipay = alipayRepository.findOne(6178L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(DIET,billType);
    }
    @Test
    public void testClassifyAlipay3() {
        Alipay alipay = alipayRepository.findOne(6150L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(DIET,billType);
    }
    @Test
    public void testClassifyAlipay4() {
        Alipay alipay = alipayRepository.findOne(6121L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(OTHER_INCOME,billType);
    }
    @Test
    public void testClassifyAlipay5() {
        Alipay alipay = alipayRepository.findOne(6117L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(MANAGEMENT_INCOME,billType);
    }
    @Test
    public void testClassifyAlipay6() {
        Alipay alipay = alipayRepository.findOne(6060L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(DIET,billType);
    }

    @Test
    public void testClassifyAlipay7() {
        Alipay alipay = alipayRepository.findOne(6242L);
        System.out.println(alipay.getLastUpdateTime());
        BillType billType = Classify.classifyAlipay(alipay);
        assertEquals(BOOK,billType);
    }
    @Test
    public void testClassifyICBC() {
        IcbcCard icbcCard = icbcCardRepository.findOne(133L);
        BillType billType = Classify.classifyICBC(icbcCard);
        assertEquals(DIET,billType);
    }

    @Test
    public void testClassifySchoolCard1() {
        SchoolCard schoolCard = schoolCardRepository.findOne(30L);
        BillType billType = Classify.classifySchoolCard(schoolCard);
        assertEquals(COMMODITY,billType);
    }

    @Test
    public void testClassifySchoolCard2() {
        SchoolCard schoolCard = schoolCardRepository.findOne(181L);
        BillType billType = Classify.classifySchoolCard(schoolCard);
        assertEquals(UTILITIES,billType);
    }

    @Test
    public void testClassifyRatio() {
        double classifyNum = 0;

        List<Alipay> alipayList = alipayRepository.findAll();
        for (Alipay alipay : alipayList) {
            BillType billType = Classify.classifyAlipay(alipay);
            if (billType != OTHER_INCOME && billType != OTHER_EXPENDITURE) {
                classifyNum++;
            }
        }
        System.out.println("支付宝可分类比例：\t" + classifyNum / alipayList.size());

        classifyNum = 0;
        List<IcbcCard> icbcCardList = icbcCardRepository.findAll();
        for (IcbcCard icbcCard : icbcCardList) {
            BillType billType = Classify.classifyICBC(icbcCard);
            if (billType != OTHER_INCOME && billType != OTHER_EXPENDITURE) {
                classifyNum++;
            }
        }
        System.out.println("工行卡可分类比例：\t" + classifyNum / icbcCardList.size());

        classifyNum = 0;
        List<SchoolCard> schoolCardList = schoolCardRepository.findAll();
        for (SchoolCard schoolCard : schoolCardList) {
            BillType billType = Classify.classifySchoolCard(schoolCard);
            if (billType != OTHER_INCOME && billType != OTHER_EXPENDITURE) {
                classifyNum++;
            }
        }
        System.out.println("校园卡可分类比例：\t" + classifyNum / schoolCardList.size());
    }

}
