package com.wclp.springserver.controller;

import com.alibaba.fastjson2.JSON;
import com.wclp.springserver.pojo.Page;
import com.wclp.springserver.pojo.User;
import com.wclp.springserver.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class UserController {

    @Resource
    UserService userServiceImpl;

    @ResponseBody
    @PostMapping("newLogin")
    public String newLogin(@RequestBody User user) {
        User rs = userServiceImpl.newLogin(user);
        System.out.println(rs);
        return JSON.toJSONString(rs);
    }

    @ResponseBody
    @RequestMapping("login.do")
    public String doLogin(@RequestBody User getUser) {
        System.out.println(getUser.getMethod_L());
        return userServiceImpl.doLogin(getUser);
    }

    @ResponseBody
    @PostMapping("changePwd")
    public String changePwd(HttpServletRequest request) {
        String uid = request.getParameter("uid");
        String pwd = request.getParameter("password");
        String pwd1 = request.getParameter("password1");
        String pwd2 = request.getParameter("password2");
        System.out.println(uid);
        return userServiceImpl.changePwd(uid, pwd, pwd1, pwd2);
    }

    @ResponseBody
    @PostMapping("logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("userInfo");
        return "done";
    }

    @ResponseBody
    @PostMapping("register")
    public String register(@RequestParam String phone, @RequestParam String password) {

        User user = new User();
        user.setPhone(phone);
        user.setMethod_L("Wechat");
        user.setPassword(password);
        String rs = userServiceImpl.register(user);
        System.out.println(rs);
        return rs;

    }

    /**
     * 获得sessionId
     */
    @RequestMapping("/getSessionId")
    @ResponseBody
    public Object getSessionId(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(5 * 60);//以秒为单位，即在没有活动5分钟后，session将失效
            return session.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送短信验证码
     */
    @RequestMapping("/sendSms")
    @ResponseBody
    public Object sendSms(@RequestParam String phone, HttpServletRequest request) {
        System.out.println(request.getParameter("phone"));
        if (userServiceImpl.checkPhone(phone).equals("ok")) {
            userServiceImpl.sendSms(request.getParameter("phone"));
        } else {
            return userServiceImpl.checkPhone(phone);
        }
        return userServiceImpl.checkPhone(phone);
    }

    /**
     * 注册
     */
    @RequestMapping("/register.do")
    @ResponseBody
    public Object doRegister(HttpServletRequest request) {
        String vercode = request.getParameter("vercode");
        String phone = request.getParameter("phone");
        String rs = userServiceImpl.verifyCode(vercode, phone);
        System.out.println(rs);
        if (rs.equals("success")) {
            //将用户信息存入数据库
            User newNser = new User();
            newNser.setPhone(phone);
            newNser.setMethod_L(request.getParameter("method_L"));
            newNser.setNickname(request.getParameter("nickname"));
            newNser.setPassword(request.getParameter("password"));
            String check = userServiceImpl.register(newNser);
            if (!Objects.equals(check, "0")) {
                return check;
            }
        } else {
            return rs;
        }
        return rs;
    }

    @RequestMapping("/addInfo")
    @ResponseBody
    public Object addInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        String uid = request.getParameter("uid");
        String phone = request.getParameter("phone");
        String nickname = request.getParameter("nickname");
        String note = request.getParameter("note");
        System.out.println(code);
        System.out.println(uid);
        String rs = "";
        User user = new User();
        if(code.equals("")){
            //将用户信息存入数据库
            user.setUid(uid);
            user.setNickname(nickname);
            user.setNote(note);
            userServiceImpl.updUserInfo(user);
            return "noUpdatePhone";
        }else {
            rs = userServiceImpl.verifyCode(code, phone);
        }
        if (rs.equals("success")) {
            //将用户信息存入数据库
            user.setUid(uid);
            user.setPhone(phone);
            user.setNickname(nickname);
            user.setNote(note);
            userServiceImpl.updUserInfo(user);
        }
        return rs;
    }

    @ResponseBody
    @RequestMapping("/updateUserInfo")
    public String updateUserInfo(@RequestBody User user) {
        System.out.println(user);
        //将用户信息存入数据库
        user.setPhone(null);
        if(userServiceImpl.updUserInfo(user).equals("done")){
            return "success";
        }
        return "fail";
    }

    //重写
    @ResponseBody
    @PostMapping("setUser")
    public String setUser(@RequestBody User user) {
        if (userServiceImpl.updUserInfo(user) != null) {
            System.out.println(user);
            return "done";
        } else return null;
    }


    @RequestMapping("/getUserList")
    @ResponseBody
    public String toUserList(HttpServletRequest request){
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        if(null==page || "".equals(page)){
            page="1";
        }
        if(null==rows || "".equals(rows)){
            rows="10";
        }
        Page<User> data = userServiceImpl.findUserByPage(Integer.parseInt(page), Integer.parseInt(rows));
        String json = JSON.toJSONString(data);
        System.out.println(json);
        return json;
    }

    @RequestMapping("/searchUser")
    @ResponseBody
    public String searchUser(HttpServletRequest request) {
        String nickname = request.getParameter("nickname");
        String phone = request.getParameter("phone");
        String page = request.getParameter("page");
        String row = request.getParameter("rows");
        System.out.println(nickname);
        System.out.println(phone);
        if(null==row || "".equals(row)){
            row="10";
        }
        if(null==page || "".equals(page)){
            page="1";
        }
        Page<User> data = userServiceImpl.searchUser(nickname, phone, page, row);
        String json = JSON.toJSONString(data);
        System.out.println(json);
        return json;
    }

}


