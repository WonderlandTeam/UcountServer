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

    @ApiOperation(value = "获取用户发布所有帖子", notes = "根据用户id获取其发布帖子")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long")
    @GetMapping("/release")
    public Map<String, Object> getPostsSharedByUser(@RequestParam Long userId) {
        return null;
    }

    @ApiOperation(value = "获取用户收藏所有帖子", notes = "根据用户id获取其收藏帖子")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long")
    @GetMapping("/collections")
    public Map<String, Object> getPostsCollectedByUser(@RequestParam Long userId) {
        return null;
    }

    @ApiOperation(value = "用户点赞", notes = "根据用户id和帖子id增加点赞信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long")
    })
    @PostMapping("/{post_id}/praises")
    public Map<String, Object> praisePost(@PathVariable("post_id") Long postId,
                                          @RequestParam Long userId) {
        return null;
    }

    @ApiOperation(value = "用户取消点赞", notes = "根据用户id和帖子id删除点赞信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long")
    })
    @DeleteMapping("/{post_id}/praises")
    public Map<String, Object> cancelPraisePost(@PathVariable("post_id") Long postId,
                                                @RequestParam Long userId) {
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
