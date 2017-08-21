package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Collection;
import cn.edu.nju.wonderland.ucountserver.entity.Post;
import cn.edu.nju.wonderland.ucountserver.entity.Reply;
import cn.edu.nju.wonderland.ucountserver.entity.Support;
import cn.edu.nju.wonderland.ucountserver.exception.InvalidRequestException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.CollectionRepository;
import cn.edu.nju.wonderland.ucountserver.repository.PostRepository;
import cn.edu.nju.wonderland.ucountserver.repository.ReplyRepository;
import cn.edu.nju.wonderland.ucountserver.repository.SupportRepository;
import cn.edu.nju.wonderland.ucountserver.service.PostService;
import cn.edu.nju.wonderland.ucountserver.vo.PostAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final SupportRepository supportRepository;
    private final CollectionRepository collectionRepository;

    public PostServiceImpl(PostRepository postRepository, ReplyRepository replyRepository, SupportRepository supportRepository, CollectionRepository collectionRepository) {
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        this.supportRepository = supportRepository;
        this.collectionRepository = collectionRepository;
    }

    /**
     * Post实体转vo
     * @param entity    entity
     * @return          vo
     */
    private PostInfoVO postEntityToVO(Post entity) {
        PostInfoVO vo = new PostInfoVO();
        vo.username = entity.getUsername();
        vo.title = entity.getTitle();
        vo.content = entity.getContent();
        // TODO 时间格式
        vo.time = entity.getTime().toString();
        vo.supportNum = supportRepository.countByPostId(entity.getPostId());
        return vo;
    }

    /**
     * Reply实体转vo
     * @param entity    entity
     * @return          vo
     */
    private PostReplyVO replyEntityToVO(Reply entity) {
        PostReplyVO vo = new PostReplyVO();
        vo.username = entity.getUsername();
        vo.content = entity.getContent();
        // TODO 时间格式
        vo.time = entity.getTime().toString();
        vo.supportNum = supportRepository.countByReplyId(entity.getReplyId());
        return vo;
    }


    @Override
    public List<PostInfoVO> getPosts(Pageable pageable) {
        return null;
    }

    @Override
    public PostInfoVO getPostInfo(Long postId) {
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new ResourceNotFoundException("未找到帖子");
        }
        return postEntityToVO(post);
    }

    @Override
    public Long addPost(PostAddVO vo) {
        // 参数检查
        if (vo.username == null || vo.title == null || vo.content == null) {
            throw new InvalidRequestException("发帖信息缺失");
        }
        // 持久化
        Post post = new Post();
        post.setUsername(vo.username);
        post.setTitle(vo.title);
        post.setContent(vo.content);
        post.setTime(Timestamp.valueOf(LocalDateTime.now()));

        return postRepository.save(post).getPostId();
    }

    @Override
    public List<PostInfoVO> getPostsSharedByUser(String username) {
        List<Post> posts = postRepository.findByUsernameOrderByTimeDesc(username);
        return posts.stream()
                .map(this::postEntityToVO)
                .collect(Collectors.toList());
    }

    @Override
    public void collectPost(String username, Long postId) {
        // 判断用户是否已经收藏
        Collection collection = collectionRepository.findByUsernameAndPostId(username, postId);
        if (collection != null) {
            throw new ResourceConflictException("用户已收藏");
        }
        // 持久化
        collection = new Collection();
        collection.setUsername(username);
        collection.setPostId(postId);
        collection.setColTime(Timestamp.valueOf(LocalDateTime.now()));
        collectionRepository.save(collection);
    }

    @Override
    public void deleteCollection(String username, Long postId) {
        Collection collection = collectionRepository.findByUsernameAndPostId(username, postId);
        if (collection == null) {
            throw new ResourceNotFoundException("用户未收藏");
        }

        collectionRepository.delete(collection);
    }

    @Override
    public List<PostInfoVO> getPostsCollectedByUser(String username) {
        List<Collection> collections = collectionRepository.findByUsernameOrderByColTimeDesc(username);
        return collections
                .stream()
                .map(c -> postEntityToVO(postRepository.findOne(c.getPostId())))
                .collect(Collectors.toList());
    }

    @Override
    public void praisePost(String username, Long id, boolean isReply) {
        Support support = isReply
                ? supportRepository.findByUsernameAndReplyId(username, id)
                : supportRepository.findByUsernameAndPostId(username, id);
        // 用户已经点过赞
        if (support != null) {
            throw new ResourceConflictException("用户已经点赞");
        }

        // 持久化
        support = new Support();
        support.setUsername(username);
        if (isReply) {
            support.setReplyId(id);
        } else {
            support.setPostId(id);
        }
        supportRepository.save(support);
    }

    @Override
    public void cancelPraisePost(String username, Long id, boolean isReply) {
        Support support = isReply
                ? supportRepository.findByUsernameAndReplyId(username, id)
                : supportRepository.findByUsernameAndPostId(username, id);
        // 用户未点赞
        if (support == null) {
            throw new ResourceNotFoundException("用户未点赞");
        }

        supportRepository.delete(support);
    }

    @Override
    public Long replyPost(Long postId, PostReplyAddVO vo) {
        // 参数检查
        if (vo.username == null || vo.content == null) {
            throw new InvalidRequestException("回复信息缺失");
        }
        // 获取帖子
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new ResourceNotFoundException("帖子不存在");
        }
        // 持久化
        Reply reply = new Reply();
        reply.setPost(post);
        reply.setUsername(vo.username);
        reply.setContent(vo.content);
        reply.setTime(Timestamp.valueOf(LocalDateTime.now()));
        return replyRepository.save(reply).getReplyId();
    }

    @Override
    public List<PostReplyVO> getPostReplies(Long postId) {
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new ResourceNotFoundException("帖子不存在");
        }

        return post
                .getReplies()
                .stream()
                .map(this::replyEntityToVO)
                .collect(Collectors.toList());
    }

}
