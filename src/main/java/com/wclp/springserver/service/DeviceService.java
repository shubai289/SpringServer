package com.wclp.springserver.service;

import com.wclp.springserver.pojo.Camera;
import com.wclp.springserver.pojo.Device;
import com.wclp.springserver.pojo.IotData;
import com.wclp.springserver.pojo.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceService {

    /**
     * 获取设备信息
     */
    Device getDeviceInfo(String Key);
    String getCamAddr(String key);
    /**
     * 获取设备列表
     */
    List<Device> getAllDevice();
    List<Camera> getCameraList();
    Page<Device> getDevicePage(int page, int rows);
    Page<Camera> getCameraPage(int page, int rows);
    Page<IotData> getDataPage(String page, String rows,String deviceKey);
    /**
     * LED操作
     */
    String Switch_Status(String lightStatus, String Key);
    /**
     * 移除设备
     */
    void rmDevice(String Key);

}
