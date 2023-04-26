package com.wclp.springserver.controller;


import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;
import com.wclp.springserver.mqtt.IMqttSender;
import com.wclp.springserver.pojo.Camera;
import com.wclp.springserver.pojo.Device;
import com.wclp.springserver.pojo.IotData;
import com.wclp.springserver.pojo.Page;
import com.wclp.springserver.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@Controller
public class DeviceController {

    @Resource
    DeviceService deviceServiceImpl;

    @Resource
    IMqttSender mqttSender;

    @Resource
    Gson gson;

    @ResponseBody
    @RequestMapping("/DeviceList")
    public String DeviceList(){
        //Map<String, Object> map = new HashMap<>();
        List<Device> list = deviceServiceImpl.getAllDevice();
        //map.put("deviceList", list);
        //System.out.println(map);
        String json = JSON.toJSONString(list);
        System.out.println(json);
        return json;
    }

    @ResponseBody
    @RequestMapping("/getDevicePage")
    public String getDevicePage(HttpServletRequest request){
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        if(null==page || "".equals(page)){
            page="1";
        }
        if(null==rows || "".equals(rows)){
            rows="10";
        }
        Page<Device> data = deviceServiceImpl.getDevicePage(Integer.parseInt(page), Integer.parseInt(rows));
        //map.put("deviceList", list);
        //System.out.println(map);
        String json = JSON.toJSONString(data);
        System.out.println(json);
        return json;
    }

    @ResponseBody
    @RequestMapping("/getCameraPage")
    public String getCameraPage(HttpServletRequest request){
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        if(null==page || "".equals(page)){
            page="1";
        }
        if(null==rows || "".equals(rows)){
            rows="10";
        }
        Page<Camera> data = deviceServiceImpl.getCameraPage(Integer.parseInt(page), Integer.parseInt(rows));
        //map.put("deviceList", list);
        //System.out.println(map);
        String json = JSON.toJSONString(data);
        System.out.println(json);
        return json;
    }

    @ResponseBody
    @RequestMapping("/CameraList")
    public String CameraList(){

        List<Camera> list = deviceServiceImpl.getCameraList();
        //System.out.println(map);
        String json = JSON.toJSONString(list);
        System.out.println(json);
        return json;
    }

    @ResponseBody
    @RequestMapping("/rmDevice")
    public String rmDevice(HttpServletRequest request){

        deviceServiceImpl.rmDevice(request.getParameter("productKey"));
        List<Device> list = deviceServiceImpl.getAllDevice();
        return JSON.toJSONString(list);
    }

    @ResponseBody
    @RequestMapping(value = "/turn_left")
    public String turn_left(HttpServletRequest request){
        String payload = request.getParameter("payload");
        String topic = request.getParameter("topic");
        System.out.println(payload);
        mqttSender.sendToMqtt(topic,payload);
        return "turn_left";
    }

    @ResponseBody
    @RequestMapping(value = "/turn_right")
    public String turn_right(HttpServletRequest request){
        String payload = request.getParameter("payload");
        String topic = request.getParameter("topic");
        System.out.println(payload);
        mqttSender.sendToMqtt(topic,payload);
        return "turn_right";
    }

    @ResponseBody
    @RequestMapping(value = "Refresh")
    public String Refresh(HttpServletRequest request){
        String productKey = request.getParameter("productKey");
        String payload = request.getParameter("payload");
        String topic = request.getParameter("topic");
        Device info = deviceServiceImpl.getDeviceInfo(productKey);
        mqttSender.sendToMqtt(topic,payload);
        return JSON.toJSONString(info);
    }

    @RequestMapping(value = "doRefresh")
    public ModelAndView doRefresh(HttpServletRequest request){
        String productKey = request.getParameter("productKey");
        String payload = request.getParameter("payload");
        String topic = request.getParameter("topic");
        Device device = deviceServiceImpl.getDeviceInfo(productKey);
        ModelAndView mav = new ModelAndView("view/role/role-ctrl");
        mqttSender.sendToMqtt(topic,payload);
        mav.addObject("device",device);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "Switch_lightStatus")
    public String Switch_Status(HttpServletRequest request){
        String topic = request.getParameter("topic");
        String payload = request.getParameter("payload");
        mqttSender.sendToMqtt(topic,payload);
        return "done";
    }

    @ResponseBody
    @RequestMapping(value = "changeDeviceName")
    public String changeDeviceName(HttpServletRequest request){
        String topic = request.getParameter("topic");
        String payload = request.getParameter("payload");
        mqttSender.sendToMqtt(topic,payload);
        return "done";
    }

    @ResponseBody
    @RequestMapping("/getDataPage")
    public String getDataPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("page");//当前是第几页数
        String limit = request.getParameter("limit");//每页显示数据条数
        String deviceKey = request.getParameter("deviceKey");
        Page<IotData> data = deviceServiceImpl.getDataPage(page,limit, deviceKey);
        String json = JSON.toJSONString(data);
        System.out.println(json);
        return json;
    }
}
