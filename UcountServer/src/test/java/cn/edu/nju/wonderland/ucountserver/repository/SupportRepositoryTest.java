package cn.edu.nju.wonderland.ucountserver.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupportRepositoryTest {

    @Autowired
    public SupportRepository supportRepository;

    @Test
    public void testSave() {
//        Support support = new Support();
//        support.setUsername("sigma");
//        support.setPostId(1L);
//        supportRepository.save(support);
    }

    @Test
    public void testCountByPostId() {
        Long postId = 1L;
        System.out.println("帖子id：\t" + postId);
        System.out.println("赞数：\t" + supportRepository.countByPostId(postId));
    }

    @Test
    public void testCountByReplyId() {
        Long replyId = 1L;
        System.out.println("回复id：\t" + replyId);
        System.out.println("赞数：\t" + supportRepository.countByPostId(replyId));
    }

}
