package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Reply;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReplyRepositoryTest {

    @Autowired
    public ReplyRepository replyRepository;

    @Autowired
    public PostRepository postRepository;

    @Test
    public void testSave() {
        Reply reply = new Reply();
        reply.setUsername("sigma");
        reply.setPost(postRepository.findOne(2L));
        reply.setContent("测试评论");
        reply.setTime(Timestamp.valueOf(LocalDateTime.now()));

        System.out.println("回复id：\t" + replyRepository.save(reply).getReplyId());
    }

}
