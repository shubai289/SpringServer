package com.wclp.springserver.pojo;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Camera {

    int id;

    @SerializedName("address")
    String address;
    @SerializedName("status")
    int status;
    @SerializedName("productKey")
    String productKey;
    @SerializedName("types")
    String types;//信息类别
    @SerializedName("message")
    String message;
    @SerializedName("createtime")
    String createtime;//创建时间
}
