package com.example.community.dao;

import com.example.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author zx
 * @date 2022/5/18 0:01
 */
@Mapper
public interface LoginTicketMapper {//也可以用注解声明方法对应的sql，此处为示例

    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select * ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    //    @Update({//也可以实现动态sql如下
//            "<script>",
//            "update login_ticket set status=#{status} where ticket=#{ticket}",
//            "<if test=\"ticket!=null\">",
//            "and 1=1",
//            "</if>",
//            "</script>"
//    })
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}",
    })
    int updateStatus(String ticket, int status);
}
