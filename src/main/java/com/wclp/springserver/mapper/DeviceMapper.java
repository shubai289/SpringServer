package com.wclp.springserver.mapper;


import com.wclp.springserver.pojo.Device;
import com.wclp.springserver.pojo.IotData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMapper {
    Device getDeviceInfo(@Param("Key")String Key);

    void UpdateDeviceInfo(Device data);

    void InsertDevice(Device data);

    void DeviceStatus(Device data);

    List<Device> getDeviceByPage(@Param("start") int start, @Param("rows")int rows);

    List<IotData> getDataPage(@Param("start") int start, @Param("rows")int rows,@Param("productKey")String productKey);

    void rmDeviceByKey(@Param("Key")String Key);

    String getValue(@Param("Key")String Key);

    Device checkDevice(@Param("Key")String Key);

    void InsertData(IotData iotdata);

    List<Device> getAllDevice();

    void Switch_Status(@Param("lightStatus")String lightStatus, @Param("Key")String Key);

    int findTotalCount();

    int CountData(@Param("productKey")String productKey);

    String checkDate(String Key);

    void UpdateDeviceData(IotData iotdata);

}
