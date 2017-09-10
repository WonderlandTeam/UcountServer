package cn.edu.nju.wonderland.ucountserver.controller;

import cn.edu.nju.wonderland.ucountserver.service.PostService;
import cn.edu.nju.wonderland.ucountserver.vo.PostAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @ApiOperation(value = "分页获取帖子信息", notes = "根据Pageable获取帖子分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageable", value = "分页信息，提供参数：page（页数），size（每页大小），sort（排序方式，格式为\"property,asc|desc\"）"),
            @ApiImplicitParam(name = "username", value = "查看帖子用户的用户名", required = false, dataType = "String")
    })
    @GetMapping
    public Page<PostInfoVO> getPosts(@PageableDefault(sort = {"time"}, direction = Sort.Direction.DESC) Pageable pageable,
                                     @RequestParam(required = false) String username) {
        return postService.getPosts(pageable, username);
    }

    @ApiOperation(value = "获取单个帖子信息", notes = "根据帖子id获取帖子信息")
    @ApiImplicitParam(name = "username", value = "查看帖子用户的用户名", required = false, dataType = "String")
    @GetMapping("/{post_id}")
    public PostInfoVO getPostInfo(@PathVariable("post_id") Long postId,
                                  @RequestParam(required = false) String username) {
        return postService.getPostInfo(postId, username);
    }

    @ApiOperation(value = "新建帖子", notes = "用户发帖")
    @ApiImplicitParam(name = "postAddVO", value = "发帖信息vo", required = true, dataType = "PostAddVO")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Long addPost(@RequestBody PostAddVO postAddVO) {
        return postService.addPost(postAddVO);
    }

    @ApiOperation(value = "获取用户发布所有帖子", notes = "根据用户名获取其发布帖子")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/release")
    public List<PostInfoVO> getPostsSharedByUser(@RequestParam String username) {
        return postService.getPostsSharedByUser(username);
    }

    @ApiOperation(value = "用户收藏原贴", notes = "根据用户名和帖子id增加收藏信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{post_id}/collections")
    public void collectPost(@PathVariable("post_id") Long postId,
                              @RequestParam String username) {
        postService.collectPost(username, postId);
//        return "收藏成功";
    }

    @ApiOperation(value = "用户取消收藏", notes = "根据用户名和帖子id删除收藏信息")
    @ApiImplicitParam(name = "postId", value = "帖子id", required = true, dataType = "Long")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{post_id}/collections")
    public void deleteCollection(@PathVariable("post_id") Long postId,
                                   @RequestParam String username) {
        postService.collectPost(username, postId);
//        return "取消收藏成功";
    }

    @ApiOperation(value = "获取用户收藏所有帖子", notes = "根据用户名获取其收藏帖子")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/collections")
    public List<PostInfoVO> getPostsCollectedByUser(@RequestParam String username) {
        return postService.getPostsCollectedByUser(username);
    }

    @ApiOperation(value = "用户点赞原贴", notes = "根据用户名和帖子id增加点赞信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{post_id}/praises")
    public void praisePost(@PathVariable("post_id") Long postId,
                             @RequestParam String username) {
        postService.praisePost(username, postId, false);
//        return "点赞成功";
    }

    @ApiOperation(value = "用户取消原贴点赞", notes = "根据用户名和帖子id删除原贴点赞信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{post_id}/praises")
    public void cancelPraisePost(@PathVariable("post_id") Long postId,
                                   @RequestParam String username) {
        postService.cancelPraisePost(username, postId, false);
//        return "取消点赞成功";
    }

    @ApiOperation(value = "用户点赞帖子回复", notes = "根据用户名和帖子回复id增加点赞信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/replies/{reply_id}/praises")
    public void praisePostReply(@PathVariable("reply_id") Long replyId,
                                  @RequestParam String username) {
        postService.praisePost(username, replyId, true);
//        return "点赞成功";
    }

    @ApiOperation(value = "用户取消帖子回复点赞", notes = "根据用户名和帖子id删除帖子回复点赞信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/replies/{reply_id}/praises")
    public void cancelPraisePostReply(@PathVariable("reply_id") Long replyId,
                                        @RequestParam String username) {
        postService.cancelPraisePost(username, replyId, true);
//        return  "取消点赞成功";
    }

    @ApiOperation(value = "用户回复帖子")
    @ApiImplicitParam(name = "postReplyVO", value = "用户回复帖子信息vo", required = true, dataType = "postReplyVO")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{post_id}/replies")
    public Long replyPost(@PathVariable("post_id") Long postId,
                          @RequestBody PostReplyAddVO postReplyAddVO) {
        return postService.replyPost(postId, postReplyAddVO);
    }

    @ApiOperation(value = "获取帖子回复信息", notes = "根据帖子回复id获取回复信息")
    @ApiImplicitParam(name = "username", value = "查看帖子回复用户的用户名", required = false, dataType = "String")
    @GetMapping("/replies/{reply_id}")
    public PostReplyVO getPostReply(@PathVariable("reply_id") Long replyId,
                                    @RequestParam(required = false) String username) {
        return postService.getPostReplyInfo(replyId, username);
    }

    @ApiOperation(value = "获取帖子所有回复信息列表", notes = "根据帖子id获取帖子所有回复")
    @ApiImplicitParam(name = "username", value = "查看帖子回复用户的用户名", required = false, dataType = "String")
    @GetMapping("/{post_id}/replies")
    public List<PostReplyVO> getPostReplies(@PathVariable("post_id") Long postId,
                                            @RequestParam(required = false) String username) {
        return postService.getPostReplies(postId, username);
    }

}
