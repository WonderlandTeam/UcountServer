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
        Alipay alipay = alipayRepository.findOne(540L);
        BillType billType = Classify.classifyAlipay(alipay);
        System.out.println(alipay.getCommodity()+""+alipay.getTrader());
        assertEquals(DIET,billType);
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
            } else if (billType == null) {
                System.out.println("支付宝错误：\t" + alipay.getId());
            }
        }
        System.out.println("支付宝可分类比例：\t" + classifyNum / alipayList.size());

        classifyNum = 0;
        List<IcbcCard> icbcCardList = icbcCardRepository.findAll();
        for (IcbcCard icbcCard : icbcCardList) {
            BillType billType = Classify.classifyICBC(icbcCard);
            if (billType != OTHER_INCOME && billType != OTHER_EXPENDITURE) {
                classifyNum++;
            } else if (billType == null) {
                System.out.println("工行卡错误：\t" + icbcCard.getId());
            }
        }
        System.out.println("工行卡可分类比例：\t" + classifyNum / icbcCardList.size());

        classifyNum = 0;
        List<SchoolCard> schoolCardList = schoolCardRepository.findAll();
        for (SchoolCard schoolCard : schoolCardList) {
            BillType billType = Classify.classifySchoolCard(schoolCard);
            if (billType != OTHER_INCOME && billType != OTHER_EXPENDITURE) {
                classifyNum++;
            } else if (billType == null) {
                System.out.println("校园卡错误：\t" + schoolCard.getId());
            }
        }
        System.out.println("校园卡可分类比例：\t" + classifyNum / schoolCardList.size());
    }

    @Test
    public void testClassifyAilpay() {
        double classifyNum = 0;

        List<Alipay> alipayList = alipayRepository.findAll();
        int i = 0;
        for (Alipay alipay : alipayList) {
            BillType billType = Classify.classifyAlipay(alipay);
            if (billType == OTHER_INCOME || billType == OTHER_EXPENDITURE) {
                classifyNum++;
                System.out.println("交易内容   "+alipay.getCommodity()+"  "+billType);
            }
            if (i>=800){
                break;
            }
            i++;
        }

        System.out.println(classifyNum);
//        System.out.println("支付宝可分类比例：\t" + classifyNum / alipayList.size());
    }

    @Test
    public void classifyAndSave() {
        List<Alipay> alipays = alipayRepository.findByConsumeTypeIsNull();
        for (Alipay alipay : alipays) {
            BillType billType = Classify.classifyAlipay(alipay);
//            System.out.println("支付宝：" + alipay.getId() + "\t" + billType);
            alipay.setConsumeType(billType.billType);
            alipayRepository.save(alipay);
        }

        System.out.println();

        List<IcbcCard> icbcCards = icbcCardRepository.findByConsumeTypeIsNull();
        for (IcbcCard icbcCard : icbcCards) {
            BillType billType = Classify.classifyICBC(icbcCard);
//            System.out.println("工行卡：" + icbcCard.getId() + "\t" + billType);
            icbcCard.setConsumeType(billType.billType);
            icbcCardRepository.save(icbcCard);
        }

        System.out.println();

        List<SchoolCard> schoolCards = schoolCardRepository.findByConsumeTypeIsNull();
        for (SchoolCard schoolCard : schoolCards) {
            BillType billType = Classify.classifySchoolCard(schoolCard);
//            System.out.println("校园卡：" + schoolCard.getId() + "\t" + billType);
            schoolCard.setConsumeType(billType.billType);
            schoolCardRepository.save(schoolCard);
        }
    }

}
