package com.wclp.springserver.controller;

import com.alibaba.fastjson2.JSON;
import com.wclp.springserver.pojo.Device;
import com.wclp.springserver.pojo.Page;
import com.wclp.springserver.pojo.User;
import com.wclp.springserver.service.DeviceService;
import com.wclp.springserver.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Controller
public class PageController {
    @Resource
    UserService userServiceImpl;

    @Resource
    DeviceService deviceServiceImpl;

    @RequestMapping("/")
    public String toIndex() {
        return "index";
    }

    @RequestMapping("/user_list")
    public String toUser_list(){
        return "view/user/user-list";
    }

    @RequestMapping("/CamList")
    public String CamList(){
        return "view/menu/cam-list";
    }

    @RequestMapping("/messaging")
    public String messaging(){
        return "view/nav/messaging";
    }

    @RequestMapping("/getTV")
    public ModelAndView getTV(@RequestParam("Key")String Key){
        ModelAndView mav = new ModelAndView("view/website/CCTV");
        String addr = deviceServiceImpl.getCamAddr(Key);
        mav.addObject("addr",addr);
        mav.addObject("Key",Key);
        return mav;
    }

    @RequestMapping("toManagementPage")
    public ModelAndView managementPage(String uid){
        ModelAndView mav = new ModelAndView("management");
        User user = userServiceImpl.selById(uid);
        mav.addObject("uid",uid);
        mav.addObject("nickname",user.getNickname());
        return mav;
    }

    @RequestMapping("/userInfo")
    public ModelAndView userInfo(@RequestParam("uid")String uid){
        System.out.println(uid);
        ModelAndView mav = new ModelAndView("view/nav/userInfo");
        User user = userServiceImpl.selById(uid);
        mav.addObject("user",user);
        System.out.println(mav);
        return mav;
    }

    @RequestMapping("/editPwd")
    public ModelAndView editPwd(@RequestParam("uid")String uid){
        ModelAndView mav = new ModelAndView("view/nav/editPwd");
        mav.addObject("uid",uid);
        System.out.println(mav);
        return mav;
    }

    @RequestMapping("/role_list")
    public String role_list(){
        return "view/role/role-list";
    }

    @RequestMapping("/role_ctrl")
    public ModelAndView role_ctrl(@RequestParam("productKey")String productKey){
        ModelAndView mav = new ModelAndView("view/role/role-ctrl");
        Device device = deviceServiceImpl.getDeviceInfo(productKey);
        mav.addObject("productKey",productKey);
        return mav;
    }
    @RequestMapping("/msg_list")
    public String msg_list(){
        return "view/menu/msg-list";
    }

    @RequestMapping("/getLog")
    @ResponseBody
    public String getLog(HttpServletRequest request) throws ServletException, IOException {
        String page = request.getParameter("page");//当前是第几页数
        String limit = request.getParameter("limit");//每页显示数据条数
        if(null==page || "".equals(page)){
            page="1";
        }
        if(null==limit || "".equals(limit)){
            limit="10";
        }
        Page<User> data = userServiceImpl.getLoginLog(Integer.parseInt(page), Integer.parseInt(limit));
        data.setCode(0);
        return JSON.toJSONString(data);
    }

    @RequestMapping("/website/index")
    public String website(){
        return "view/website/index";
    }

    @RequestMapping("/register")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/user_add")
    public String toUser_add(){
        return "view/user/user-add";
    }

    @RequestMapping("/todo")
    public String todo(){
        return "view/nav/todo";
    }

    @RequestMapping("/note")
    public String note(){
        return "view/nav/note";
    }

    @RequestMapping("/toDataHistoryPage")
    public ModelAndView toDataHistoryPage(@RequestParam("deviceKey")String deviceKey){
        System.out.println(deviceKey);
        ModelAndView mav = new ModelAndView("view/home/dataHistory");
        mav.addObject("deviceKey",deviceKey);
        return mav;
    }

    @RequestMapping("/logout")
    public String logout(){
        return "index";
    }

    @RequestMapping("/home")
    public String homePage(){
        return "view/home/index";
    }

    @RequestMapping("/dataHistoryList")
    public String dataHistoryList(){
        return "view/home/dataHistory";
    }

}
