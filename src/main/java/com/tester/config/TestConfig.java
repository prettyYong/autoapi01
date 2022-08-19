package com.tester.config;

import lombok.Data;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;


@Data
public class TestConfig {
    //登陆接口uri
    //public static String loginUrl;
    public static String getUserInfoUrl;
    public static String getUserListUrl;
    //public static String addUserUrl;
    //public static String updateUserInfoUrl;
    //声明http客户端
    public static DefaultHttpClient defaultHttpClient;
    //用来存储cookies信息的变量
    public static CookieStore store;
}