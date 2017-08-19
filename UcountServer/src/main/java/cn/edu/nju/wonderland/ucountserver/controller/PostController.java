package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostShareVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    @ApiOperation(value = "根据筛选条件获取帖子信息列表", notes = "根据过滤信息获取帖子列表")
    @ApiImplicitParam(name = "pageable", value = "过滤信息", dataType = "pageable")
    @GetMapping
    public Map<String, Object> getPosts(Pageable pageable) {
        return null;
    }

    @ApiOperation(value = "获取单个帖子信息", notes = "根据帖子id获取帖子信息")
    @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long")
    @GetMapping("/{post_id}")
    public Map<String, Object> getPostInfo(@PathVariable("post_id") Long postId) {
        return null;
    }

    @ApiOperation(value = "新建帖子", notes = "用户发帖")
    @ApiImplicitParam(name = "postShareVO", value = "发帖信息vo", required = true, dataType = "PostShareVO")
    @PostMapping
    public Map<String, Object> addPost(@RequestBody PostShareVO postShareVO) {
        return null;
    }

    @ApiOperation(value = "获取用户发布所有帖子", notes = "根据用户名获取其发布帖子")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/release")
    public Map<String, Object> getPostsSharedByUser(@RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "用户收藏原贴", notes = "根据用户名和帖子id增加收藏信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    })
    @PostMapping("/{post_id}/collections")
    public Map<String, Object> collectPost(@PathVariable("post_id") Long postId,
                                           @RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "用户取消收藏", notes = "根据用户名和帖子id删除收藏信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    })
    @DeleteMapping("/{post_id}/collections")
    public Map<String, Object> deleteCollection(@PathVariable("post_id") Long post_id,
                                                @RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "获取用户收藏所有帖子", notes = "根据用户名获取其收藏帖子")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/collections")
    public Map<String, Object> getPostsCollectedByUser(@RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "用户点赞原贴", notes = "根据用户名和帖子id增加点赞信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    })
    @PostMapping("/{post_id}/praises")
    public Map<String, Object> praisePost(@PathVariable("post_id") Long postId,
                                          @RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "用户取消原贴点赞", notes = "根据用户名和帖子id删除原贴点赞信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    })
    @DeleteMapping("/{post_id}/praises")
    public Map<String, Object> cancelPraisePost(@PathVariable("post_id") Long postId,
                                                @RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "用户点赞帖子回复", notes = "根据用户名和帖子回复id增加点赞信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "replyId", value = "帖子回复id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    })
    @PostMapping("/replies/{reply_id}/praises")
    public Map<String, Object> praisePostReply(@PathVariable("replyId") Long replyId,
                                               @RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "用户取消帖子回复点赞", notes = "根据用户名和帖子id删除帖子回复点赞信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    })
    @DeleteMapping("/replies/{reply_id}/praises")
    public Map<String, Object> cancelPraisePostReply(@PathVariable("reply_id") Long replyId,
                                                     @RequestParam String username) {
        return null;
    }

    @ApiOperation(value = "用户回复帖子")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "postReplyVO", value = "用户回复帖子信息vo", required = true, dataType = "postReplyVO")
    })
    @PostMapping("/{post_id}/replies")
    public Map<String, Object> replyPost(@PathVariable("post_id") Long postId,
                                         @RequestBody PostReplyVO postReplyVO) {
        return null;
    }

    @ApiOperation(value = "获取帖子所有回复信息列表", notes = "根据帖子id获取帖子所有回复")
    @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long")
    @GetMapping("/{post_id}/replies")
    public Map<String, Object> getPostReplies(@PathVariable("post_id") Long postId) {
        return null;
    }

}
