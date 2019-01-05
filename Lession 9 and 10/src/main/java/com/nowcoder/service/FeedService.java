package com.nowcoder.service;

import com.nowcoder.dao.FeedDAO;
import com.nowcoder.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nowcoder on 2016/8/12.
 */
@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    // 拉模式的feed：取feed,实际上是取一些列的feed流
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    // 加feed
    public boolean addFeed(Feed feed) {
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    // 推模式feed:查feed
    public Feed getById(int id) {
        return feedDAO.getFeedById(id);
    }
}
