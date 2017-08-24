package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.PostService;
import cn.edu.nju.wonderland.ucountserver.vo.PostAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.edu.nju.wonderland.ucountserver.util.KeyName.CONTENT;
import static cn.edu.nju.wonderland.ucountserver.util.KeyName.MESSAGE;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @ApiOperation(value = "根据筛选条件获取帖子信息列表", notes = "根据过滤信息获取帖子列表")
    @ApiImplicitParam(name = "pageable", value = "过滤信息", dataType = "pageable")
    @GetMapping
    public Map<String, Object> getPosts(@PageableDefault(sort = {"time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        List<PostInfoVO> posts = postService.getPosts(pageable);
        result.put(CONTENT, posts);
        return result;
    }

    @ApiOperation(value = "获取单个帖子信息", notes = "根据帖子id获取帖子信息")
    @GetMapping("/{post_id}")
    public Map<String, Object> getPostInfo(@PathVariable("post_id") Long postId) {
        Map<String, Object> result = new HashMap<>();
        PostInfoVO vo = postService.getPostInfo(postId);
        result.put(CONTENT, vo);
        return result;
    }

    @ApiOperation(value = "新建帖子", notes = "用户发帖")
    @ApiImplicitParam(name = "postAddVO", value = "发帖信息vo", required = true, dataType = "PostAddVO")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Map<String, Object> addPost(@RequestBody PostAddVO postAddVO) {
        Map<String, Object> result = new HashMap<>();
        Long id = postService.addPost(postAddVO);

        result.put(CONTENT, id);
        return result;
    }

    @ApiOperation(value = "获取用户发布所有帖子", notes = "根据用户名获取其发布帖子")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/release")
    public Map<String, Object> getPostsSharedByUser(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        List<PostInfoVO> posts = postService.getPostsSharedByUser(username);
        result.put(CONTENT, posts);
        return result;
    }

    @ApiOperation(value = "用户收藏原贴", notes = "根据用户名和帖子id增加收藏信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @PostMapping("/{post_id}/collections")
    public Map<String, Object> collectPost(@PathVariable("post_id") Long postId,
                                           @RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        postService.collectPost(username, postId);
        result.put(MESSAGE, "收藏成功");
        return result;
    }

    @ApiOperation(value = "用户取消收藏", notes = "根据用户名和帖子id删除收藏信息")
    @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{post_id}/collections")
    public Map<String, Object> deleteCollection(@PathVariable("post_id") Long postId,
                                                @RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        postService.collectPost(username, postId);
        result.put(MESSAGE, "取消收藏成功");
        return result;
    }

    @ApiOperation(value = "获取用户收藏所有帖子", notes = "根据用户名获取其收藏帖子")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/collections")
    public Map<String, Object> getPostsCollectedByUser(@RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "用户点赞原贴", notes = "根据用户名和帖子id增加点赞信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @PostMapping("/{post_id}/praises")
    public Map<String, Object> praisePost(@PathVariable("post_id") Long postId,
                                          @RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        postService.praisePost(username, postId, false);
        result.put(MESSAGE, "点赞成功");
        return result;
    }

    @ApiOperation(value = "用户取消原贴点赞", notes = "根据用户名和帖子id删除原贴点赞信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @DeleteMapping("/{post_id}/praises")
    public Map<String, Object> cancelPraisePost(@PathVariable("post_id") Long postId,
                                                @RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        postService.cancelPraisePost(username, postId, false);
        result.put(MESSAGE, "取消点赞成功");
        return result;
    }

    @ApiOperation(value = "用户点赞帖子回复", notes = "根据用户名和帖子回复id增加点赞信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @PostMapping("/replies/{reply_id}/praises")
    public Map<String, Object> praisePostReply(@PathVariable("replyId") Long replyId,
                                               @RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        postService.praisePost(username, replyId, true);
        result.put(MESSAGE, "点赞成功");
        return result;
    }

    @ApiOperation(value = "用户取消帖子回复点赞", notes = "根据用户名和帖子id删除帖子回复点赞信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @DeleteMapping("/replies/{reply_id}/praises")
    public Map<String, Object> cancelPraisePostReply(@PathVariable("reply_id") Long replyId,
                                                     @RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        postService.cancelPraisePost(username, replyId, true);
        result.put(MESSAGE, "取消点赞成功");
        return result;
    }

    @ApiOperation(value = "用户回复帖子")
    @ApiImplicitParam(name = "postReplyVO", value = "用户回复帖子信息vo", required = true, dataType = "postReplyVO")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{post_id}/replies")
    public Map<String, Object> replyPost(@PathVariable("post_id") Long postId,
                                         @RequestBody PostReplyAddVO postReplyAddVO) {
        Map<String, Object> result = new HashMap<>();
        Long replyId = postService.replyPost(postId, postReplyAddVO);
        result.put(CONTENT, replyId);
        return result;
    }

    @ApiOperation(value = "获取帖子回复信息", notes = "根据帖子回复id获取回复信息")
    @GetMapping("/replies/{reply_id}")
    public Map<String, Object> getPostReply(@PathVariable("reply_id") Long replyId) {
        Map<String, Object> result = new HashMap<>();
        PostReplyVO vo = postService.getPostReplyInfo(replyId);
        result.put(CONTENT, vo);
        return result;
    }

    @ApiOperation(value = "获取帖子所有回复信息列表", notes = "根据帖子id获取帖子所有回复")
    @GetMapping("/{post_id}/replies")
    public Map<String, Object> getPostReplies(@PathVariable("post_id") Long postId) {
        Map<String, Object> result = new HashMap<>();
        List<PostReplyVO> replies = postService.getPostReplies(postId);
        result.put(CONTENT, replies);
        return result;
    }

}
