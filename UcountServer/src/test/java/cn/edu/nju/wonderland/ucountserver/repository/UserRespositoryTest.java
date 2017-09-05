package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.User;
import org.junit.Test;

public class UserRespositoryTest {
    UserRepository userRepository;

    @Test
    public void testSave() {
        User user = new User();
        user.setUsername("123");
        user.setPassword("123");
        user.setTel("123");
        user.setEmail("123");
        userRepository.save(user);
    }

    @Test
    public void testFindByUsername() {
        userRepository.findByUsername("123");
    }
}
