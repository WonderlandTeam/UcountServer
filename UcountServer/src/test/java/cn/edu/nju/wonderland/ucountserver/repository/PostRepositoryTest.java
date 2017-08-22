package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    public PostRepository postRepository;

    @Test
    public void testSave() {
        Post post = new Post();
        post.setUsername("sigma");
        post.setTitle("测试贴1");
        post.setContent("测试内容");
        post.setTime(Timestamp.valueOf(LocalDateTime.now()));

        System.out.println("帖子id：\t" + postRepository.save(post).getPostId());
    }

    @Test
    public void testFindOne() {
        Post post = postRepository.findOne(1L);
        System.out.println("标题：\t" + post.getTitle());
        System.out.println("内容：\t" + post.getContent());
        System.out.println("时间：\t" + post.getTime().toString());
    }

}
