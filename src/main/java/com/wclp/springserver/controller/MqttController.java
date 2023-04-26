package com.wclp.springserver.controller;

import com.google.gson.Gson;
import com.wclp.springserver.mqtt.ApiResult;
import com.wclp.springserver.mqtt.IMqttSender;
import com.wclp.springserver.pojo.Device;
import com.wclp.springserver.pojo.IotData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

@Controller
public class MqttController {

    @Resource
    IMqttSender mqttSender;

    @Resource
    Gson gson;

    //这个是外面配置文件里面的设置的接收主题之一
    private final static String SEND_TOPIC_PREFIX = "receive_iot_topic/";



    @PostMapping("/sendToTopic")
    public ApiResult sendToTopic(String topic, String payload) {
        /**
         * 想接收方方法消息-主题：receive_iot_topic/#,receive_chat_topic/#
         */
        mqttSender.sendToMqtt(topic,payload);
        System.out.println("发送成功=>" + "主题：" + topic + "  载荷:" + payload);
        return ApiResult.success(null, "发送成功");
    }


    /**
     * 127.0.0.1:8081/mqtt/control_command
     * post、json
     * {
     *   "createtime": "2022-04-17T07:02:23.707Z",
     *   "deviceid": "001设备",
     *   "humi": 30,
     *   "light": 55,
     *   "loraid": "r001",
     *   "temp": 100,
     *   "types": "esp32"
     * }
     */

    @PostMapping("/control_command")
    public ApiResult controlCommand(@RequestBody Device iotData) {
        String deviceId = iotData.getProductKey();
        // 前缀 + 设备号
        String topic = SEND_TOPIC_PREFIX + deviceId;
        String payload=gson.toJson(iotData);
        mqttSender.sendToMqtt( topic,payload);
        System.out.println("发送成功=>" + "主题：" + topic + "  载荷:" + payload);
        return ApiResult.success(null, "发送成功");
    }


}
