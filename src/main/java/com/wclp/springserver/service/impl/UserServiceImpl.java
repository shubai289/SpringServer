package com.wclp.springserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.wclp.springserver.mapper.UserMapper;
import com.wclp.springserver.pojo.Page;
import com.wclp.springserver.pojo.User;
import com.wclp.springserver.service.UserService;
import com.wclp.springserver.utils.DateUtils;
import com.wclp.springserver.utils.IdUtils;
import com.zhenzi.sms.ZhenziSmsClient;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.*;

@Service
@CacheConfig(cacheNames = {"UserServiceImpl"})
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public User newLogin(User user) {
        User getUser = userMapper.selBynickName(user.getNickname());
        if (getUser != null){
            getUser.setUid(user.getUid());
            getUser.setLoginDate(DateUtils.getCurrentDateStr());
            getUser.setResult_L("登录成功");
            userMapper.recordLogin_Log(getUser);
            return getUser;
        }
        else {
            String UID = IdUtils.getId();
            while(true){
                User u1 = userMapper.selById(UID);
                if(u1 != null) {
                    UID = IdUtils.getId();
                }else {
                    user.setCheck("toPwdPage");
                    user.setUid(UID);
                    break;
                }
            }
            user.setLoginDate(DateUtils.getCurrentDateStr());
            user.setResult_L("用户注册");
            userMapper.recordLogin_Log(user);
            userMapper.InsertUsers(user);
        }
        return user;
    }

    @Override
    public String doLogin(User user) {
        User getUser = userMapper.selBynickName(user.getNickname());
        if (getUser == null){
            getUser = userMapper.selByPhone(user.getNickname());
            if (getUser == null){
                return "800";
            }else {
                return Get(user, getUser);
            }
        }else{
            return Get(user, getUser);
        }
    }

    private String Get(User user, User getUser) {
        if(user.getPassword().equals(getUser.getPassword())){
            user.setUid(getUser.getUid());
            user.setLoginDate(DateUtils.getCurrentDateStr());
            user.setResult_L("登录成功");
            userMapper.recordLogin_Log(user);
            return user.getUid();
        }else {
            user.setUid(getUser.getUid());
            user.setLoginDate(DateUtils.getCurrentDateStr());
            user.setResult_L("密码错误");
            userMapper.recordLogin_Log(user);
            return "808";
        }
    }

    @Override
    public String checkUser(User user){
        if (userMapper.selByPhone(user.getPhone()) == null) {
            return "!Phone";
        }else if(userMapper.selBynickName(user.getNickname()) == null){
            return "!Nickname";
        }else return null;
    }

    @Override
    public User checkPwd(String uid) {
        return userMapper.selById(uid);
    }

    @Override
    public String login(User user) {
        User getUser = userMapper.selByPhone(user.getPhone());
        System.out.println(getUser);
        if (getUser != null) {
            if (getUser.getPassword().equals(user.getPassword())) {
                getUser.setLoginDate(DateUtils.getCurrentDateStr());
                getUser.setResult_L("登录成功");
                userMapper.recordLogin_Log(getUser);
                return "done";
            } else {
                getUser.setLoginDate(DateUtils.getCurrentDateStr());
                getUser.setResult_L("密码错误");
                userMapper.recordLogin_Log(getUser);
                return "pwdError";
            }
        } else {
            return "phoneError";
        }
    }

    @Override
    public Page<User> getLoginLog(int page, int rows) {
        Page<User> pageBean = new Page<>();
        if (page <= 1) {
            page = 1;
        }
        int start;
        start = (page - 1) * 10;
        int totalCount = userMapper.TotalLogCount();
        //计算总页数
        int totalPage = totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1;
        pageBean.setTotalPage(totalPage);
        List<User> data = userMapper.getLoginLogPage(start, rows);
        pageBean.setCount(totalCount);
        pageBean.setRows(rows);
        pageBean.setData(data);
        return pageBean;
    }

    @Override
    public String updUserInfo(User user) {
        if(userMapper.selById(user.getUid()) != null){
            userMapper.updUserInfo(user);
            return "done";
        }
        return null;
    }

    @Override
    public Page<User> searchUser(String nickname, String phone, String page, String rows) {
        Page<User> pageBean = new Page<>();
        int pg = Integer.parseInt(page);
        if (pg <= 1) {
            pg = 1;
        }
        int r = Integer.parseInt(rows);
        int start;
        start = (pg - 1) * 10;
        List<User> data = userMapper.searchUser(nickname, phone , pg, r);

        int totalCount = userMapper.findTotalCount();
        //计算总页数
        //int totalPage = totalCount % r == 0 ? totalCount / r : totalCount / r + 1;
        //pageBean.setTotalPage(totalPage);
        pageBean.setData(data);
        pageBean.setCount(totalCount);
        //pageBean.setCurrentPage(page);
        pageBean.setRows(r);
        return pageBean;
    }

    @Override
    public User getUserbyId(User user) {
        User tuser = userMapper.selById(user.getUid());
        if (tuser != null) {
           return tuser;
        }
        tuser = userMapper.selByPhone(user.getPhone());
        return tuser;
    }

    @Override
    public String register(User user) {
        //判断手机号是否已存在
        User u = userMapper.selByPhone(user.getPhone());
        if (u != null) {
            return "exist";
        }
        //生成新Uid，判断Uid是否已存在
        String ID = IdUtils.getId();
        while(true){
            User u1 = userMapper.selById(ID);
            if(u1 != null) {
                ID = IdUtils.getId();
            }else {
                user.setUid(ID);
                break;
            }
        }
        user.setLoginDate(DateUtils.getCurrentDateStr());
        user.setResult_L("用户注册");
        userMapper.recordLogin_Log(user);
        userMapper.InsertUsers(user);
        return ID;
    }

    @Override
    public Page<User> findUserByPage(int page, int rows) {
        Page<User> pageBean = new Page<>();
        if (page <= 1) {
            page = 1;
        }
        int start;
        start = (page - 1) * 10;
        int totalCount = userMapper.findTotalCount();
        //计算总页数
        int totalPage = totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1;
        pageBean.setTotalPage(totalPage);
        List<User> data = userMapper.findUserByPage(start, rows);
        pageBean.setCode(0);
        pageBean.setCount(totalCount);
        pageBean.setRows(rows);
        pageBean.setData(data);
        return pageBean;
    }

    @Override
    public String changePwd(String uid,String pwd, String pwd1, String pwd2) {
        User user = userMapper.selById(uid);
        if (Objects.equals(pwd, "") && user.getPassword() == null){
            if(pwd1.equals(pwd2)) {
                user.setPassword(pwd1);
                userMapper.changePwd(user);
                return "done";
            }
        }else if (Objects.equals(pwd, "") && user.getPassword() != null){
            return "nullPwd";
        }else if(user.getPassword() != null && !Objects.equals(pwd, "")){
            if (user.getPassword().equals(pwd)){
                if(pwd1.equals(pwd2)) {
                    user.setPassword(pwd1);
                    userMapper.changePwd(user);
                    return "done";
                }else {
                    return "re_pwdErr";
                }
            }else {
                return "old_pwdErr";
            }
        }
        return "";
    }

    @Override
    public User selById(String uid){
        return userMapper.selById(uid);
    }

    @Override
    public String checkPhone(String phone){
        if(Objects.equals(phone, "")){
            return "none";
        }else if(phone == null){
            return "none";
        }else if(phone.length() != 11){
            return "wrongNum";
        }else{
            if(userMapper.selByPhone(phone) == null){
                return "ok";
            }else {
                return "exist";
            }
        }
    }

    @Override
    public String sendSms(String phone) {
        Map<String, Object> params = new HashMap<>();
        Map<Object, Object> map = new HashMap<>();
        try {
            JSONObject json = null;
            //生成6位验证码
            String verifyCode = String.valueOf(new Random().nextInt(8999) + 1000);
            System.out.println(verifyCode);
            //发送短信
            ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com", "112958","f4211e7d-1385-4cef-9e7e-10c39cf37457");
            params.put("number", phone);
            params.put("templateId","11344");
            String[] templateParams = new String[2];
            templateParams[0] = verifyCode;
            templateParams[1] = "5分钟";
            params.put("templateParams", templateParams);
            String result = client.send(params);
            json = JSONObject.parseObject(result);
            if(json.getIntValue("code") != 0)//发送短信失败
                return "fail";
            map.put("verifyCode", verifyCode);
            map.put("createTime", Long.toString(System.currentTimeMillis()));
            System.out.println(map);
            // 将认证码存入Redis
            stringRedisTemplate.opsForHash().putAll(phone ,map);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String verifyCode(String code, String phone){
        Long time;
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(phone);
        System.out.println(map);
        if(map.get("verifyCode") != null){
            if(!map.get("verifyCode").equals(code)){
                return "errCode";
            }else {
                time = Long.valueOf(String.valueOf(map.get("createTime")));
            }
        }else {
            return "noCode";
        }
        System.out.println(time);

        if((System.currentTimeMillis() - time) > 1000 * 60 * 5){
            return "timeOut";
        }
        //这里省略
        return "success";
    }
}
