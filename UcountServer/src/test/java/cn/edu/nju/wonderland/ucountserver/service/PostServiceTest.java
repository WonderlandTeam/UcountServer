package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.PostAddVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTest {

    @Autowired
    public PostService postService;

    @Test
    public void testAddPost() {
        PostAddVO vo = new PostAddVO();
        vo.username = "sense";
        vo.title = "测试帖2";
        vo.content = "测试内容";

        Long id = postService.addPost(vo);
        System.out.println("帖子id：\t" + id);
    }

}
