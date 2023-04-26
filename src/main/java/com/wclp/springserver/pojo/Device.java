package com.wclp.springserver.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device implements Serializable {

    int id;

    int temp_value;
    @SerializedName("address")
    String address;
    @SerializedName("status")
    int status;
    @SerializedName("deviceName")
    String deviceName;
    @SerializedName("productKey")
    String productKey;
    @SerializedName("types")
    String types;//信息类别
    @SerializedName("temperature")
    float temperature;
    @SerializedName("humidity")
    float humidity;
    @SerializedName("message")
    String message;
    // float pm25;
    @SerializedName("deviceData")
    float deviceData;
    @SerializedName("ledStatus")
    String ledStatus;
    @SerializedName("createtime")
    String createtime;//创建时间

}
