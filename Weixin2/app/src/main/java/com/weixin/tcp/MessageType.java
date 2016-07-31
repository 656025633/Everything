package com.weixin.tcp;

/**
 * Created by zhao on 2016/7/24 0024.
 */
public interface  MessageType {


    byte  USER_LOGIN_REQUEST = 0;  //用户登录请求

    byte DATA_REQUEST_IM = 1;      //IM消息转发

    byte DATA_REQUEST_IM_NOW = 2;   //IM实时消息转发

    byte DATA_REQUEST_Oper= 3;       //现场记录

    byte  SEVER_PUSH_NOTICE = 4;     //服务转发客运命令

    byte  SEVER_PUSH_RAILWAY = 5;     //服务转发到发作业

    byte  USER_Phone_LOG =6;          //用户操作日志

    byte  USER_LOGINOUT_REQUEST = 7;  //用户登录退出请求

    short MIN_PACKAGE_LENGTH = 8;

}
