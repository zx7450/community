package com.example.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zx
 * @date 2022/6/9 15:05
 */

public class Event {
    private String topic;//事件类型
    private int userId;//触发的人
    private int entityType;//实体类型
    private int entityId;//实体id
    private int entityUserId;//实体作者
    private Map<String, Object> data = new HashMap<>();//其他额外信息，以便未来扩展用

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;//返回当前对象以便可以一次set多个内容
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
