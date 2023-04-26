package com.wclp.springserver.mapper;


import com.wclp.springserver.pojo.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserMapper {

    User selById(@Param("uid")String uid);

    void updUserInfo(User user);

    void InsertUsers(User user);

    User selByPhone(@Param("phone") String phone);

    User selBynickName(@Param("nickname") String nickname);

    void recordLogin_Log(User user);

    List<User> getLoginLogPage(@Param("start") int start, @Param("rows")int rows);

    List<User> findUserByPage(@Param("start") int start, @Param("rows")int rows);

    List<User> searchUser(@Param("nickname") String nickname,@Param("phone") String phone ,@Param("start") int start, @Param("rows")int rows);

    int findTotalCount();

    int TotalLogCount();

    void changePwd(User user);
}