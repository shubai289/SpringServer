package com.wclp.springserver.service;

import com.wclp.springserver.pojo.Page;
import com.wclp.springserver.pojo.User;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    /**
     * 登录功能
     */
    String login(User user);
    String doLogin(User user);
    User newLogin(User user);
    /**
     * 账户注册功能
     */
    String register(User user);
    /**
     * 更新用户信息
     */
    String updUserInfo(User user);
    //User setUser(User user);
    /**
     * 获取用户信息
     */
    Page<User> searchUser(String nickname, String phone, String page, String rows);
    User selById(String uid);
    User getUserbyId(User user);
    String checkUser(User user);
    User checkPwd(String uid);
    Page<User> getLoginLog(int page, int rows);
    /**
     * 获取用户列表
     */

    Page<User> findUserByPage(int page, int rows);
    /**
     * 获取用户密码
     */
    String changePwd(String uid, String pwd, String pwd1, String pwd2);
    /**
     * 短信验证码
     */
    String checkPhone(String phone);
    String sendSms(String phone);
    String verifyCode(String code, String phone);
}
