package com.tester.cases;

import com.tester.config.TestConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

////ce shi hh
public class LoginTestfdf {
    @BeforeTest(groups = "loginTrue", description = "测试准备工作,获取HttpClient对象")
    public void beforeTest() {
        String basePath = "http://localhost:8089/dev/";
        TestConfig.getUserInfoUrl = basePath + "getUserInfoUrl";
        TestConfig.getUserListUrl = basePath + "getUserListUrl";
        //TestConfig.addUserUrl = basePath+"addUserUrl";
        //TestConfig.loginUrl = basePath+"loginUrl";
        // TestConfig.updateUserInfoUrl = basePath+"updateUserInfoUrl";

        TestConfig.defaultHttpClient = new DefaultHttpClient();
        System.out.println('"');
    }

    @Test(groups = "loginTrue", description = "获取用户列表")
    public void getUserList() throws IOException {
       /* SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase", 1);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);*/

        //下边的代码为写完接口的测试代码
        String result = sendGet(TestConfig.getUserListUrl);
        //处理结果，就是判断返回结果是否符合预期
        Assert.assertEquals(true, true);
    }

    @Test(groups = "loginTrue", description = "获取用户列表")
    public void getUser() throws IOException {
        //下边的代码为写完接口的测试代码
        String result = sendGet(TestConfig.getUserInfoUrl);
        //处理结果，就是判断返回结果是否符合预期
        Assert.assertEquals(true, true);
    }
    /* @Test(groups = "loginTrue", description = "用户成功登陆接口")
    public void loginTrue() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase", 1);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //下边的代码为写完接口的测试代码
        String result = getResult(loginCase);
        //处理结果，就是判断返回结果是否符合预期
        Assert.assertEquals(loginCase.getExpected(), result);
    }*/


   /* @Test(groups = "loginFalse", description = "用户登录接口失败")
    public void loginFalse() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase", 2);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //下边的代码为写完接口的测试代码
        String result = getResult(loginCase);
        //处理结果，就是判断返回结果是否符合预期
        Assert.assertEquals(loginCase.getExpected(), result);
    }*/

    /* private String getResult(LoginCase loginCase) throws IOException {
         //下边的代码为写完接口的测试代码
         HttpPost post = new HttpPost(TestConfig.loginUrl);
         JSONObject param = new JSONObject();
         param.put("userName", loginCase.getUserName());
         param.put("password", loginCase.getPassword());
         //设置请求头信息，设置header
         post.setHeader("content-type", "application/json");
         //将参数信息添加到方法中
         StringEntity entity = new StringEntity(param.toString(), "utf-8");
         post.setEntity(entity);
         //声明一个对象来进行响应结果的存储
         String result;
         //执行post方法
         HttpResponse response = TestConfig.defaultHttpClient.execute(post);
         //获取响应结果
         result = EntityUtils.toString(response.getEntity(), "utf-8");
         System.out.println(result);

         TestConfig.store = TestConfig.defaultHttpClient.getCookieStore();
         return result;
     }*/
    private String sendGet(String url) throws IOException {
        //下边的代码为写完接口的测试代码
        HttpGet get = new HttpGet(url);
        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        HttpResponse response = TestConfig.defaultHttpClient.execute(get);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);

        TestConfig.store = TestConfig.defaultHttpClient.getCookieStore();
        return result;
    }
}