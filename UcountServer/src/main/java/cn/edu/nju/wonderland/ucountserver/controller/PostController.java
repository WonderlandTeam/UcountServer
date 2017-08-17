package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostShareVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    /**
     * 获取帖子
     */
    @GetMapping
    public Map<String, Object> getPosts(Pageable pageable) {
        return null;
    }

    /**
     * 获取具体帖子信息
     */
    @GetMapping("/{post_id}")
    public Map<String, Object> getPostInfo(@PathVariable("post_id") Long postId) {
        return null;
    }

    /**
     * 分享／发布帖子
     */
    @PostMapping
    public Map<String, Object> addPost(@RequestBody PostShareVO postShareVO) {
        return null;
    }

    /**
     * 获取用户发布帖子
     */
    @GetMapping("/release")
    public Map<String, Object> getPostsSharedByUser(@RequestParam Long userId) {
        return null;
    }

    /**
     * 获取用户收藏帖子
     */
    @GetMapping("/collection")
    public Map<String, Object> getPostsCollectedByUser(@RequestParam Long userId) {
        return null;
    }

    /**
     * 称赞帖子
     */
    @PutMapping("/{post_id}")
    public Map<String, Object> praisePost(@PathVariable("post_id") Long postId) {
        return null;
    }

    /**
     * 回复帖子
     */
    @PostMapping("/{post_id}/replies")
    public Map<String, Object> replyPost(@PathVariable("post_id") Long postId,
                                         @RequestBody PostReplyVO postReplyVO) {
        return null;
    }

    /**
     * 获取帖子所有回复
     */
    @GetMapping("/{post_id}/replies")
    public Map<String, Object> getPostReplies(@PathVariable("post_id") Long postId) {
        return null;
    }

}
