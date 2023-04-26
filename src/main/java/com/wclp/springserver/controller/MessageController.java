package com.wclp.springserver.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wclp.springserver.pojo.DeviceMsg;
import com.wclp.springserver.pojo.IotData;
import com.wclp.springserver.pojo.Page;
import com.wclp.springserver.pojo.User;
import com.wclp.springserver.service.MessageService;
import com.wclp.springserver.service.UserService;
import com.zhenzi.sms.ZhenziSmsClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


@Controller
public class MessageController {

    @Resource
    UserService userServiceImpl;

    @Resource
    MessageService messageServiceImpl;

    @ResponseBody
    @RequestMapping("/getMessageList")
    public String getMessageList(){
        List<DeviceMsg> list = new ArrayList<>();
        List<DeviceMsg> list1 = messageServiceImpl.getMessageList();
        List<DeviceMsg> list2 = messageServiceImpl.getMsgHtyList();
        list.addAll(list1);
        list.addAll(list2);
        String json = JSON.toJSONString(list);
        System.out.println(json);
        return json;
    }

    @ResponseBody
    @RequestMapping("/getDeletedMsgPage")
    public String getDeletedMsgPage(){
        List<DeviceMsg> list = messageServiceImpl.getDeletedMsgPage();
        Page<DeviceMsg> data = new Page<>();
        data.setCode(0);
        data.setData(list);
        String json = JSON.toJSONString(data);
        System.out.println(json);
        return json;
    }

    @ResponseBody
    @RequestMapping("/getUnreadMsgPage")
    public String getUnreadMsgPage(){
        List<DeviceMsg> list = messageServiceImpl.getMessageList();
        Page<DeviceMsg> data = new Page<>();
        data.setCode(0);
        data.setData(list);
        String json = JSON.toJSONString(data);
        System.out.println(json);
        return json;
    }

    @ResponseBody
    @RequestMapping("/getReadMsgPage")
    public String getReadMsgPage(){
        List<DeviceMsg> list = messageServiceImpl.getMsgHtyList();
        Page<DeviceMsg> data = new Page<>();
        data.setCode(0);
        data.setData(list);
        String json = JSON.toJSONString(data);
        System.out.println(json);
        return json;
    }

    @ResponseBody
    @RequestMapping("/messageCheck")
    public String messageCheck(HttpServletRequest request){
        String id = request.getParameter("id");
        if(messageServiceImpl.checkMessage(Integer.parseInt(id))){
            return "done";
        }else {
            return "checked";
        }
    }

    @ResponseBody
    @RequestMapping("/messageDel")
    public String messageDelete(HttpServletRequest request){
        String id = request.getParameter("id");
        System.out.println(id);
        if(messageServiceImpl.delMsgHty(Integer.parseInt(id))){
            return "done";
        }else {
            return "checked";
        }
    }

    @ResponseBody
    @RequestMapping("/messageRecover")
    public String messageRecover(HttpServletRequest request){
        String id = request.getParameter("id");
        System.out.println(id);

        if(messageServiceImpl.RecoverMsgHty(Integer.parseInt(id))){
            return "done";
        }else {
            return "checked";
        }
    }

}
