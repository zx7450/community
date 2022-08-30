package com.example.community.actuator;

import com.example.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zx
 * @date 2022/8/30 23:25
 */
@Component
@Endpoint(id = "database")
public class DataBAseEndPoint {
    private static final Logger logger = LoggerFactory.getLogger(DataBAseEndPoint.class);

    @Autowired
    private DataSource dataSource;

    @ReadOperation//只能通过get请求访问
    public String chenkConnection() {
        try (
                Connection connection = dataSource.getConnection();
        ) {
            return CommunityUtil.getJSONString(0, "获取连接成功!");
        } catch (SQLException e) {
            logger.error("获取连接失败!" + e.getMessage());
            return CommunityUtil.getJSONString(1, "获取连接失败!");
        }
    }
}
