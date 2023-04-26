package com.wclp.springserver.service.impl;

import com.wclp.springserver.mapper.MessageMapper;
import com.wclp.springserver.pojo.DeviceMsg;
import com.wclp.springserver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"MessageServiceImpl"})
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Override
    public List<DeviceMsg> getMessageList() {
        return messageMapper.getMessageList();
    }

    @Override
    public List<DeviceMsg> getMsgHtyList() {
        return messageMapper.getMsgHtyList();
    }

    @Override
    public List<DeviceMsg> getDeletedMsgPage(){
        return messageMapper.getDeletedMsgPage();
    }

    @Override
    public boolean checkMessage(int id) {
        DeviceMsg msg = messageMapper.getMessagebyId(id);
        msg.setChecked(1);
        messageMapper.delMessage(id);
        return messageMapper.MsgToHty(msg);
    }

    @Override
    public boolean delMsgHty(int id) {
        return messageMapper.HideMessage(id, 2);
    }

    @Override
    public boolean RecoverMsgHty(int id) {
        return messageMapper.RecoverMsgHty(id, 1);
    }
}
