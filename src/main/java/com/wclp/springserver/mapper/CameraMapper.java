package com.wclp.springserver.mapper;

import com.wclp.springserver.pojo.Camera;
import com.wclp.springserver.pojo.Device;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraMapper {

    void newCam(Camera data);

    void CamStatus(Camera data);

    int TotalCam();

    String getCamAddr(@Param("Key")String Key);

    List<Camera> getCameraByPage(@Param("start") int start, @Param("rows")int rows);

    void updateCamData(Camera data);

    Camera checkCam(@Param("Key")String Key);

    List<Camera> getCameraList();

    void rmCameraByKey(@Param("Key")String Key);

}
