package cn.edu.nju.wonderland.ucountserver.service.component;

import cn.edu.nju.wonderland.ucountserver.entity.User;
import cn.edu.nju.wonderland.ucountserver.repository.CollectionRepository;
import cn.edu.nju.wonderland.ucountserver.repository.SupportRepository;
import cn.edu.nju.wonderland.ucountserver.repository.UserRepository;
import cn.edu.nju.wonderland.ucountserver.service.PersonasData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户画像处理
 */
@Component
public class Personas {

    private final CollectionRepository collectionRepository;

    private final SupportRepository supportRepository;

    private final UserRepository userRepository;

    private final PersonasData personasData;

    public Personas(CollectionRepository collectionRepository, SupportRepository supportRepository, UserRepository userRepository, PersonasData personasData) {
        this.collectionRepository = collectionRepository;
        this.supportRepository = supportRepository;
        this.userRepository = userRepository;
        this.personasData = personasData;
    }

    public void setPersonas() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
//            System.out.print(user.getUsername() + "\t");
            System.out.printf("%5.2f\t", personasData.getCurrentTotalBalanceByUser(user.getUsername()));
            System.out.printf("%5.2f\t", personasData.getExpenditurePerDay(user.getUsername()));
            System.out.println();

        }
    }

}
