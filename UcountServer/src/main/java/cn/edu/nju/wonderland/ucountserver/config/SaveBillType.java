package cn.edu.nju.wonderland.ucountserver.config;

import cn.edu.nju.wonderland.ucountserver.entity.Alipay;
import cn.edu.nju.wonderland.ucountserver.entity.IcbcCard;
import cn.edu.nju.wonderland.ucountserver.entity.SchoolCard;
import cn.edu.nju.wonderland.ucountserver.repository.AlipayRepository;
import cn.edu.nju.wonderland.ucountserver.repository.IcbcCardRepository;
import cn.edu.nju.wonderland.ucountserver.repository.SchoolCardRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Deprecated
@Component
public class SaveBillType {

    private AlipayRepository alipayRepository;
    private IcbcCardRepository icbcCardRepository;
    private SchoolCardRepository schoolCardRepository;

    public SaveBillType(AlipayRepository alipayRepository, IcbcCardRepository icbcCardRepository, SchoolCardRepository schoolCardRepository) {
        this.alipayRepository = alipayRepository;
        this.icbcCardRepository = icbcCardRepository;
        this.schoolCardRepository = schoolCardRepository;
    }

    public void classifyAlipayAndSave() {
        List<Alipay> alipayList = alipayRepository.findAll();
        for (Alipay alipay : alipayList) {
            alipay.setConsumeType(Classify.classifyAlipay(alipay).billType);
            alipayRepository.save(alipay);
        }

        List<SchoolCard> schoolCardList = schoolCardRepository.findAll();
        for (SchoolCard schoolCard : schoolCardList) {
            schoolCard.setConsumeType(Classify.classifySchoolCard(schoolCard).billType);
            schoolCardRepository.save(schoolCard);
        }
    }

    public void classifyICBCCardAndSave() {
        List<IcbcCard> icbcCardList = icbcCardRepository.findAll();
        for (IcbcCard icbcCard : icbcCardList) {
            icbcCard.setConsumeType(Classify.classifyICBC(icbcCard).billType);
            icbcCardRepository.save(icbcCard);
        }
    }

    public void classifySchoolCardAndSave() {
        List<SchoolCard> schoolCardList = schoolCardRepository.findAll();
        for (SchoolCard schoolCard : schoolCardList) {
            if (schoolCard.getConsumeType() != null) {
                continue;
            }
            schoolCard.setConsumeType(Classify.classifySchoolCard(schoolCard).billType);
            schoolCardRepository.save(schoolCard);
        }
    }

}
