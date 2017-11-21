package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.Post;
import cn.edu.nju.wonderland.ucountserver.entity.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    public PostRepository postRepository;
    @Autowired
    public TagRepository tagRepository;

    @Test
    public void testSave() {
//        Post post = new Post();
//        post.setUsername("bian");
//        post.setTitle("选股策略");
//        post.setContent("动量策略、均值回归");
//        post.setTime(Timestamp.valueOf(LocalDateTime.now()));
//        post.setTags(new HashSet<Tag>(){{
//            Tag tag = new Tag();
//            tag.setName("股票");add(tag);
//        }});
//
//        System.out.println("帖子id：\t" + postRepository.save(post).getPostId());
    }

    @Test
    public void testFindOne() {
        Post post = postRepository.findOne(1L);
        System.out.println("标题：\t" + post.getTitle());
        System.out.println("内容：\t" + post.getContent());
        System.out.println("时间：\t" + post.getTime().toString());
        System.out.println("回复数：\t" + post.getReplies().size());
    }

    @Test
    public void testGetTags() {
        Post post = postRepository.findOne(1L);
        post.getTags().forEach(t -> System.out.println(t.getName()));
    }

    @Test
    public void testGetFavouriteTags() {
        List<Tag> tags = tagRepository.getFavouriteTags("sigma");
        tags.forEach(e -> System.out.println(e.getName()));
    }

    @Test
    public void testGetPostsByTag() {
        List<Post> posts = postRepository.getPostsByTag("股票");
        for (Post post : posts) {
            System.out.println(post.getPostId());
        }
    }

}
