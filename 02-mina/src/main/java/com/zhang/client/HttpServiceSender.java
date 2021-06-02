package com.zhang.client;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServiceSender {
    protected final Logger LOGGER =  LoggerFactory.getLogger(HttpServiceSender.class);

    public static String[] get(String url) throws IOException {
        String[] response = new String[2];
        response[0] = "-1";

        //创建页面接收
        CloseableHttpClient client = HttpClientBuilder.create().build();

        //发送get请求
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse resp = client.execute(get);//resp:响应页面

        if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){//发送请求成功，并得到响应
            //解析数据
            //读取服务器返回来的json字符串数据
            String data = EntityUtils.toString(resp.getEntity(), Charsets.UTF_8);
            response[1] = data;
            System.out.println("data" +data);
        }else {
            System.out.println("请求页面错误");
        }


        response[0] = String.valueOf(resp.getStatusLine().getStatusCode());
        return response;
    }

    public static String[] postJson(String url, String json) throws IOException {
        //创建返回字符串
        String[] response = new String[2];
        response[0] = "-1";

        //创建页面发送对象
        CloseableHttpClient client = HttpClientBuilder.create().build();

        //post请求
        HttpPost post = new HttpPost(url);
        //添加请求头
        post.setHeader("Content-Type", "application/json;charset=UTF-8");

        //创建请求体对象
        StringEntity entity = new StringEntity(json);//解决请求页面编码格式问题
        entity.setContentType("application/json");//解决请求页面中文乱码

        //添加请求体
        post.setEntity(entity);

        CloseableHttpResponse resp = client.execute(post);// resp: 响应页面对象


        if(resp.getStatusLine().getStatusCode() == 200){//请求成功，并获得响应信息
            //解析数据
            HttpEntity respEntity = resp.getEntity();//获取响应页面，体
            String data = EntityUtils.toString(respEntity); //获得信息
//            System.out.println("post data:" +data);
            response[1] = data;
        }
        response[0] = String.valueOf(resp.getStatusLine().getStatusCode());
        return response;
    }


//    public static void main(String[] args) throws Exception {
//        HttpServiceSender cl = new HttpServiceSender();
//        String url = "http://127.0.0.1:8088/postjson";
//        String [] a=cl.postJson(url);
//        System.out.println("++++++++++++++"+a[0]+"  "+a[1]);
//    }

}
