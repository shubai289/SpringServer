package com.wclp.springserver.service.impl;

import com.wclp.springserver.mapper.CameraMapper;
import com.wclp.springserver.mapper.DeviceMapper;
import com.wclp.springserver.pojo.*;
import com.wclp.springserver.service.DeviceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"DeviceServiceImpl"})
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceMapper deviceMapper;

    @Resource
    CameraMapper cameraMapper;

    @Override
    public Device getDeviceInfo(String Key) {
        return deviceMapper.getDeviceInfo(Key);
    }

    @Override
    public Page<Device> getDevicePage(int page, int rows) {
        Page<Device> pageBean = new Page<>();
        if (page <= 1) {
            page = 1;
        }
        int start;
        start = (page - 1) * 10;
        int totalCount = deviceMapper.findTotalCount();
        //int totalPage = totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1;
        //pageBean.setTotalPage(totalPage);
        List<Device> data = deviceMapper.getDeviceByPage(start, rows);
        pageBean.setCode(0);
        pageBean.setCount(totalCount);
        //pageBean.setRows(rows);
        pageBean.setData(data);
        return pageBean;
    }

    @Override
    public Page<Camera> getCameraPage(int page, int rows) {
        Page<Camera> pageBean = new Page<>();
        if (page <= 1) {
            page = 1;
        }
        int start;
        start = (page - 1) * 10;
        int totalCount = cameraMapper.TotalCam();
        //int totalPage = totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1;
        //pageBean.setTotalPage(totalPage);
        List<Camera> data = cameraMapper.getCameraByPage(start, rows);
        pageBean.setCode(0);
        pageBean.setCount(totalCount);
        //pageBean.setRows(rows);
        pageBean.setData(data);
        return pageBean;
    }

    @Override
    public Page<IotData> getDataPage(String page, String rows,String deviceKey) {
        Page<IotData> pageBean = new Page<>();
        if(null==page || "".equals(page)){
            page="1";
        }
        if(null==rows || "".equals(rows)){
            rows="10";
        }
        int pg = Integer.parseInt(page);
        int limit = Integer.parseInt(rows);
        if (pg <= 1) {
            pg = 1;
        }
        int start;
        start = (pg - 1) * 10;
        int totalCount = deviceMapper.CountData(deviceKey);
        //int totalPage = totalCount % limit == 0 ? totalCount / limit : totalCount / limit + 1;
        //pageBean.setTotalPage(totalPage);
        List<IotData> data = deviceMapper.getDataPage(start, limit,deviceKey);
        pageBean.setCode(0);
        pageBean.setCount(totalCount);
        //pageBean.setRows(rows);
        pageBean.setData(data);
        return pageBean;
    }

    @Override
    public List<Device> getAllDevice() {
        return deviceMapper.getAllDevice();
    }

    @Override
    public List<Camera> getCameraList() {
        return cameraMapper.getCameraList();
    }

    @Override
    public String getCamAddr(String Key){
        return cameraMapper.getCamAddr(Key);
    }

    @Override
    public String Switch_Status(String lightStatus, String Key) {
        if(lightStatus.equals("on")){
            deviceMapper.Switch_Status("off", Key);
            return "off";
        }else if (lightStatus.equals("off")){
            deviceMapper.Switch_Status("on", Key);
            return "on";
        }else {
            return "error";
        }
    }

    @Override
    public void rmDevice(String Key) {
        Device device = deviceMapper.checkDevice(Key);
        if(device != null){
            if (device.getStatus() == 0) {
                deviceMapper.rmDeviceByKey(Key);
                System.out.println("Device_removed");
            }else if (device.getStatus() == 1){
                System.out.println("unable to delete online_Device");
            }
        }else {
            Camera camera = cameraMapper.checkCam(Key);
            if(camera != null){
                if (camera.getStatus() == 0){
                    cameraMapper.rmCameraByKey(Key);
                }else if (camera.getStatus() == 1){
                    System.out.println("unable to delete online_Device");
                }
            }
        }
    }
}
