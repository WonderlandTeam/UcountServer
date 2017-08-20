package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Collection;
import cn.edu.nju.wonderland.ucountserver.entity.Post;
import cn.edu.nju.wonderland.ucountserver.entity.Support;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.CollectionRepository;
import cn.edu.nju.wonderland.ucountserver.repository.PostRepository;
import cn.edu.nju.wonderland.ucountserver.repository.ReplyRepository;
import cn.edu.nju.wonderland.ucountserver.repository.SupportRepository;
import cn.edu.nju.wonderland.ucountserver.service.PostService;
import cn.edu.nju.wonderland.ucountserver.vo.PostInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostReplyVO;
import cn.edu.nju.wonderland.ucountserver.vo.PostShareVO;
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
     * 转换Post实体为PostInfoVO
     * @param entity        entity
     * @return              vo
     */
    private PostInfoVO postEntityToVO(Post entity) {
        PostInfoVO vo = new PostInfoVO();
        vo.username = entity.getUsername();
        vo.title = entity.getTitle();
        vo.content = entity.getContent();
        vo.time = entity.getTime();
        vo.supportNum = supportRepository.countByPostId(entity.getPostId());
        return vo;
    }


    @Override
    public List<PostInfoVO> getPosts(Pageable pageable) {
        return null;
    }

    @Override
    public PostInfoVO getPostInfo(Long postId) {
        return null;
    }

    @Override
    public Long addPost(PostShareVO postShareVO) {
        return null;
    }

    @Override
    public List<PostInfoVO> getPostsSharedByUser(String username) {
        return null;
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
    public void replyPost(Long postId, PostReplyVO postReplyVO) {

    }

    @Override
    public List<PostReplyVO> getPostReplies(Long postId) {
        return null;
    }

}
