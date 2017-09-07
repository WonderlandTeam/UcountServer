package cn.edu.nju.wonderland.ucountserver.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassifyAndSave {

    @Autowired
    public SaveBillType saveBillType;

    @Test
    public void saveAlipay() {
        saveBillType.classifyAlipayAndSave();
    }

    @Test
    public void saveICBC() {
        saveBillType.classifyICBCCardAndSave();
    }

    @Test
    public void saveSchoolCard() {
        saveBillType.classifySchoolCardAndSave();
    }

}
