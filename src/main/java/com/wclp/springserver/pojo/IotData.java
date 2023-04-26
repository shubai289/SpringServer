package com.wclp.springserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Data
@Component
public class IotData implements Serializable {

    int id;

    String productKey;

    float temperature;

    float humidity;

    float deviceData;

    String createtime;//创建时间

}
