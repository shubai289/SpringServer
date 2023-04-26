package com.wclp.springserver.service;

import com.wclp.springserver.pojo.DeviceMsg;

import java.util.List;

public interface MessageService {
    List<DeviceMsg> getMessageList();

    boolean checkMessage(int id);

    List<DeviceMsg> getMsgHtyList();

    boolean delMsgHty(int id);

    List<DeviceMsg> getDeletedMsgPage();

    boolean RecoverMsgHty(int id);
}
