package com.wclp.springserver.service.impl;

import com.google.gson.Gson;
import com.wclp.springserver.mapper.CameraMapper;
import com.wclp.springserver.mapper.DeviceMapper;
import com.wclp.springserver.mapper.MessageMapper;
import com.wclp.springserver.mqtt.IMqttSender;
import com.wclp.springserver.pojo.Camera;
import com.wclp.springserver.pojo.Device;
import com.wclp.springserver.pojo.DeviceMsg;
import com.wclp.springserver.pojo.IotData;
import com.wclp.springserver.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * MQTT接收消息
 */

@Service
public class MqttCaseServiceImpl implements MessageHandler {

    @Resource
    DeviceMapper deviceMapper;

    @Resource
    CameraMapper cameraMapper;

    @Resource
    MessageMapper messageMapper;

    @Resource
    IMqttSender mqttSender;
    /**
     * MessageHeaders:
     * public static final String PREFIX = "mqtt_";
     * public static final String QOS = "mqtt_qos";
     * public static final String ID = "mqtt_id";
     * public static final String RECEIVED_QOS = "mqtt_receivedQos";
     * public static final String DUPLICATE = "mqtt_duplicate";
     * public static final String RETAINED = "mqtt_retained";
     * public static final String RECEIVED_RETAINED = "mqtt_receivedRetained";
     * public static final String TOPIC = "mqtt_topic";
     * public static final String RECEIVED_TOPIC = "mqtt_receivedTopic";
     * public static final String MESSAGE_EXPIRY_INTERVAL = "mqtt_messageExpiryInterval";
     * public static final String TOPIC_ALIAS = "mqtt_topicAlias";
     * public static final String RESPONSE_TOPIC = "mqtt_responseTopic";
     * public static final String CORRELATION_DATA = "mqtt_correlationData";
     */
    @Resource
    Gson gson;

    IotData iotData = new IotData();

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
        String payload = (String) message.getPayload();
        Device entity = gson.fromJson(payload, Device.class);
        Camera camera = gson.fromJson(payload, Camera.class);
        boolean flag = false;
        System.out.println("headers:" + topic + " 接收的数据:" + payload);
        assert topic != null;
        if (topic.contains("mqtt-report")) {
            if (entity!=null){
                //不是遗嘱数据
                if (!entity.getTypes().equals("will")) {
                    switch (entity.getTypes()) {
                        case "cam_":
                            System.out.println("来自esp32cam的cam_数据");
                            if (cameraMapper.checkCam(camera.getProductKey()) == null) {
                                //写入数据库 ...
                                camera.setStatus(1);
                                camera.setCreatetime(DateUtils.getCurrentDateStr());
                                cameraMapper.newCam(camera);
                            }else {
                                camera.setStatus(1);
                                cameraMapper.updateCamData(camera);
                            }
                            break;
                        case "insert":
                            System.out.println(entity.getProductKey());
                            if (deviceMapper.checkDevice(entity.getProductKey()) == null) {
                                System.out.println("来自esp8266的insert数据");
                                //写入数据库 ...
                                entity.setStatus(1);
                                entity.setTemp_value(20);
                                entity.setCreatetime(DateUtils.getCurrentDateStr());
                                deviceMapper.InsertDevice(entity);
                                iotData.setCreatetime(DateUtils.getCurrentDateStr());
                                iotData.setProductKey(entity.getProductKey());
                                iotData.setTemperature(entity.getTemperature());
                                iotData.setHumidity(entity.getHumidity());
                                iotData.setDeviceData(entity.getDeviceData());
                                if(deviceMapper.checkDate(iotData.getCreatetime()) == null) {
                                    deviceMapper.InsertData(iotData);
                                }else {
                                    deviceMapper.UpdateDeviceData(iotData);
                                }
                            }
                            String topic_I = entity.getProductKey() + "-insert";
                            String payload_I = "int"+deviceMapper.getValue(entity.getProductKey());
                            mqttSender.sendToMqtt(topic_I,payload_I);
                            System.out.println("发送成功=>" + "主题：" + topic_I + "  载荷:" + payload_I);
                            break;
                        case "update":
                            System.out.println("来自esp8266的update数据");
                            //写入数据库 ...
                            System.out.println(deviceMapper.checkDate(iotData.getCreatetime()));
                            entity.setCreatetime(DateUtils.getCurrentDateStr());
                            iotData.setCreatetime(DateUtils.getCurrentDateStr());
                            deviceMapper.UpdateDeviceInfo(entity);
                            iotData.setProductKey(entity.getProductKey());
                            iotData.setTemperature(entity.getTemperature());
                            iotData.setHumidity(entity.getHumidity());
                            iotData.setDeviceData(entity.getDeviceData());
                            if(deviceMapper.checkDate(iotData.getCreatetime()) == null) {
                                deviceMapper.InsertData(iotData);
                            }else {
                                deviceMapper.UpdateDeviceData(iotData);
                            }
                            break;
                        case "report":
                            System.out.println("来自esp8266的report数据");
                            //写入数据库 ...
                            entity.setCreatetime(DateUtils.getCurrentDateStr());
                            deviceMapper.UpdateDeviceInfo(entity);
                            DeviceMsg msg = new DeviceMsg();
                            List<DeviceMsg> m_list;
                            msg.setChecked(0);
                            msg.setCreatetime(entity.getCreatetime());
                            msg.setDeviceName(entity.getDeviceName());
                            msg.setMessage(entity.getMessage());
                            msg.setProductKey(entity.getProductKey());
                            m_list = messageMapper.getMessage(entity.getProductKey());
                            if( m_list.isEmpty() ){
                                messageMapper.NewMessage(msg);
                            }else{
                                for (DeviceMsg deviceMsg : m_list) {
                                    if (Objects.equals(deviceMsg.getMessage(), msg.getMessage())) {
                                        messageMapper.updateMessage(msg);
                                        break;
                                    } else {
                                        flag = true;
                                    }
                                }
                                if (flag){
                                    messageMapper.NewMessage(msg);
                                }
                            }
                    }
                }
            }else {
                System.out.println("序列化失败");
            }
        }else if(topic.contains("mqtt-will")){
            if(entity.getTypes().equals("will")){
                System.out.println("来自esp8266的will数据");
                if(entity.getMessage().equals("offline")){
                    entity.setStatus(0);
                }else if(entity.getMessage().equals("online")){
                    entity.setStatus(1);
                }
                deviceMapper.DeviceStatus(entity);
            }else if(camera.getTypes().equals("cam_will")){
                System.out.println("来自esp32cam的will数据");
                if(camera.getMessage().equals("offline")){
                    camera.setStatus(0);
                }else if(camera.getMessage().equals("online")){
                    camera.setStatus(1);
                }
                cameraMapper.CamStatus(camera);
            }
        }
    }
}

