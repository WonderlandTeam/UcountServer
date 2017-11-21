package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.exception.InvalidRequestException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.PostService;
import cn.edu.nju.wonderland.ucountserver.service.UserDetector;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;
import cn.edu.nju.wonderland.ucountserver.util.PostTagKeywords;
import cn.edu.nju.wonderland.ucountserver.vo.PostAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final SupportRepository supportRepository;
    private final CollectionRepository collectionRepository;
    private final TagRepository tagRepository;
    private final UserDetector userDetector;

    public PostServiceImpl(PostRepository postRepository,
                           ReplyRepository replyRepository,
                           SupportRepository supportRepository,
                           CollectionRepository collectionRepository,
                           TagRepository tagRepository,
                           UserDetector userDetector) {
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        this.supportRepository = supportRepository;
        this.collectionRepository = collectionRepository;
        this.tagRepository = tagRepository;
        this.userDetector = userDetector;
    }

    /**
     * Post实体转vo
     *
     * @param entity entity
     * @return vo
     */
    private PostInfoVO postEntityToVO(Post entity, String username) {
        PostInfoVO vo = new PostInfoVO();

        vo.postId = entity.getPostId();
        vo.username = entity.getUsername();
        vo.title = entity.getTitle();
        vo.content = entity.getContent();
        vo.setTime(DateHelper.toTimeByTimeStamp(entity.getTime()));
        vo.supportNum = supportRepository.countByPostId(entity.getPostId());

        if (username != null) {
            vo.isSupported = supportRepository.findByUsernameAndPostId(username, entity.getPostId()) != null;
            vo.isCollected = collectionRepository.findByUsernameAndPostId(username, entity.getPostId()) != null;
        }

        return vo;
    }

    /**
     * Reply实体转vo
     *
     * @param entity entity
     * @return vo
     */
    private PostReplyVO replyEntityToVO(Reply entity, String username) {
        PostReplyVO vo = new PostReplyVO();
        vo.replyId = entity.getReplyId();
        vo.username = entity.getUsername();
        vo.content = entity.getContent();
        // 时间格式
        vo.time = DateHelper.toTimeByTimeStamp(entity.getTime());
        vo.supportNum = supportRepository.countByReplyId(entity.getReplyId());

        if (username != null) {
            vo.isSupported = supportRepository.findByUsernameAndReplyId(username, entity.getReplyId()) != null;
        }

        return vo;
    }

    @Override
    public List<PostInfoVO> getPosts(Pageable pageable, String username) {
        Page<Post> postsPage = postRepository.findAll(pageable);
        // 智能推荐
        if (username == null) {
            return postsPage.map(e -> postEntityToVO(e, null)).getContent();
        }

        List<Tag> favouriteTags = tagRepository.getFavouriteTags(username);
        // 无喜爱标签
        if (favouriteTags.size() == 0) {
            return postsPage.map(e -> postEntityToVO(e, username)).getContent();
        }
        List<Post> posts = new ArrayList<>();
        for (Tag tag : favouriteTags) {
            List<Post> postsWithSameTag = postRepository.getPostsByTag(tag.getName());
            for (Post post : postsWithSameTag) {
                if (!posts.contains(post)) {
                    posts.add(post);
                }
            }
        }
        // 推荐帖子数目不足
        if (posts.size() < pageable.getPageSize()) {
            for (Post post : postsPage) {
                if (!posts.contains(post)) {
                    posts.add(post);
                }
            }
        }

        return posts.stream().map(e -> postEntityToVO(e, username)).collect(Collectors.toList());
    }

    @Override
    public PostInfoVO getPostInfo(Long postId, String username) {
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new ResourceNotFoundException("未找到帖子");
        }
        return postEntityToVO(post, username);
    }

    @Override
    public Long addPost(PostAddVO vo) {
        // 参数检查
        if (vo.username == null || vo.title == null || vo.content == null) {
            throw new InvalidRequestException("发帖信息缺失");
        }

        // 判断用户是否存在
        if (!userDetector.isUserExists(vo.username)) {
            throw new ResourceConflictException("用户名不存在");
        }

        // 获取标签
        Set<Tag> tags = PostTagKeywords.getPostTags(vo.title, vo.content);

        // 持久化
        Post post = new Post();
        post.setUsername(vo.username);
        post.setTitle(vo.title);
        post.setContent(vo.content);
        post.setTime(Timestamp.valueOf(LocalDateTime.now()));
        // 添加标签
        post.setTags(tags);

        return postRepository.saveAndFlush(post).getPostId();
    }

    @Override
    public List<PostInfoVO> getPostsSharedByUser(String username) {
        // 判断用户是否存在
        if (!userDetector.isUserExists(username)) {
            throw new ResourceConflictException("用户名不存在");
        }

        List<Post> posts = postRepository.findByUsernameOrderByTimeDesc(username);
        return posts.stream()
                .map(e -> postEntityToVO(e, username))
                .collect(Collectors.toList());
    }

    @Override
    public void collectPost(String username, Long postId) {
        // 判断用户是否存在
        if (!userDetector.isUserExists(username)) {
            throw new ResourceConflictException("用户名不存在");
        }

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
                .map(c -> postEntityToVO(postRepository.findOne(c.getPostId()), username))
                .collect(Collectors.toList());
    }

    @Override
    public void praisePost(String username, Long id, boolean isReply) {
        // 判断用户是否存在
        if (!userDetector.isUserExists(username)) {
            throw new ResourceConflictException("用户名不存在");
        }

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
    public PostReplyVO getPostReplyInfo(Long replyId, String username) {
        Reply reply = replyRepository.findOne(replyId);
        if (reply == null) {
            throw new ResourceNotFoundException("未找到帖子回复");
        }
        return replyEntityToVO(reply, username);
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
        reply.setPostId(post.getPostId());
        reply.setUsername(vo.username);
        reply.setContent(vo.content);
        reply.setTime(Timestamp.valueOf(LocalDateTime.now()));
        return replyRepository.save(reply).getReplyId();
    }

    @Override
    public List<PostReplyVO> getPostReplies(Long postId, String username) {
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new ResourceNotFoundException("帖子不存在");
        }

        // 按点赞数逆序排序
        return post
                .getReplies()
                .stream()
                .map(e -> replyEntityToVO(e, username))
                .sorted((o1, o2) -> o2.supportNum - o1.supportNum)
                .collect(Collectors.toList());
    }

}
