package com.example.community.entity;

import java.util.List;

/**
 * @author zx
 * @date 2022/6/17 16:27
 */
public class SearchResult {
    private List<DiscussPost> list;
    private long total;

    public SearchResult(List<DiscussPost> list, long total) {
        this.list = list;
        this.total = total;
    }

    public List<DiscussPost> getList() {
        return list;
    }

    public void setList(List<DiscussPost> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
