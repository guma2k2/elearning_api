package com.backend.elearning.domain.topic;

import com.backend.elearning.domain.common.PageableData;

import java.util.List;

public interface TopicService {
    PageableData<TopicVM> getPageableTopics(int pageNum, int pageSize);
    TopicVM create (TopicPostVM topicPostVM);
    TopicVM getTopicById (Integer topicId);
    List<TopicVM> getTopics();
    void update(TopicPostVM topicPostVM, Integer topicId);
    void delete(Integer topicId);
}
