package cn.edu.nju.wonderland.ucountserver.util;

import cn.edu.nju.wonderland.ucountserver.entity.Tag;

import java.util.*;

/**
 * 帖子标签、关键词
 */
public class PostTagKeywords {

    private static final Map<Tag, Set<String>> TAG_KEYWORDS_MAP;

    static {
        TAG_KEYWORDS_MAP = new HashMap<>();

        TAG_KEYWORDS_MAP.put(new Tag("股票"), new HashSet<String>(){{
            add("股票");
            add("炒股");
            add("中小板");
            add("创业板");
            add("上证指数");
            add("深圳成指");
        }});

        TAG_KEYWORDS_MAP.put(new Tag("理财"), new HashSet<String>(){{
            add("理财");
            add("投资");
        }});

    }

    /**
     * 获取帖子标签
     * @param title     帖子标题
     * @param content   帖子内容
     * @return          帖子标签集合
     */
    public static Set<Tag> getPostTags(String title, String content) {
        Set<Tag> tags = new HashSet<>();
        TAG_KEYWORDS_MAP.forEach((k, v) -> v.forEach(w -> {
            if (title.contains(w) || content.contains(w)) {
                tags.add(k);
            }
        }));
        return tags;
    }

}
