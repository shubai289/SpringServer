package com.wclp.springserver.mapper;

import com.wclp.springserver.pojo.DeviceMsg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {

    boolean HideMessage(@Param("id")int id, int checked);

    boolean RecoverMsgHty(@Param("id")int id, int checked);

    boolean delMessage(@Param("id")int id);

    boolean delMsgHty(@Param("id")int id);

    DeviceMsg getMessagebyId(@Param("id")int id);

    boolean MsgToHty(DeviceMsg msg);

    void NewMessage(DeviceMsg msg);

    List<DeviceMsg> getMessage(@Param("Key")String Key);

    void updateMessage(DeviceMsg msg);

    List<DeviceMsg> getMessageList();

    List<DeviceMsg> getMsgHtyList();

    List<DeviceMsg> getDeletedMsgPage();


}
