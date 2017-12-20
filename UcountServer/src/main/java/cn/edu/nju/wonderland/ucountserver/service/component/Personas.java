package cn.edu.nju.wonderland.ucountserver.service.component;

import cn.edu.nju.wonderland.ucountserver.entity.User;
import cn.edu.nju.wonderland.ucountserver.repository.UserRepository;
import cn.edu.nju.wonderland.ucountserver.service.PersonasData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户画像处理
 */
@Component
public class Personas {

    private final UserRepository userRepository;

    private final PersonasData personasData;

    public Personas(UserRepository userRepository, PersonasData personasData) {
        this.userRepository = userRepository;
        this.personasData = personasData;
    }

    public void setPersonas() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            double totalBalance = personasData.getCurrentTotalBalanceByUser(user.getUsername());
            double averageExpense = personasData.getExpenditurePerDay(user.getUsername());
            //
        }
    }

}
