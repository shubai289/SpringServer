package com.wclp.springserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    int id;
    String Uid;
    String phone;
    String password;
    String nickname;
    String Check;
    String method_L;
    String loginDate;
    String result_L;
    String note;
}
