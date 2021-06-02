package com.zhang.mina;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang.client.HttpServiceSender;
import com.zhang.entity.Sensor;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

import java.util.Date;

/**
 * IoHandler继承类
 */
public class MyIoHandler extends IoHandlerAdapter {

    //当一个新客户端连接后触发此方法
    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        System.out.println(ioSession.getRemoteAddress()+"已连接...");
    }

    //当连接打开后触发此方法，一般此方法与 sessionCreated 同时触发
    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        System.out.println("server端IP："+ioSession.getRemoteAddress());
    }

    //当连接被关闭时触发
    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        System.out.println(ioSession.getRemoteAddress()+"连接被断开...");
    }

    //当连接空闲时，触发此类方法
    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        System.out.println(ioSession.getRemoteAddress()+"  :  "+idleStatus.toString()+"连接空闲");
    }

    //抛出异常未被捕获触发此方法
    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        System.out.println(ioSession.getRemoteAddress()+"  : 抛出异常未被捕获  : "+throwable.toString());
    }

    //接收到客户端的请求触发此方法
    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        System.out.println("11-> Message has been Received!!!");

        //转换接收字符流
        IoBuffer ioBuffer = (IoBuffer) o;
        byte[] data = new byte[ioBuffer.limit()];

        //转换字符流
        ioBuffer.get(data);

        //拼接字符串
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            stringBuffer.append((char) data[i]);
        }

        //获取拼接的字符串
        String msg = stringBuffer.toString();
      //  System.out.println("message:"+msg);

        //加密
        byte[] root = new byte[5];
        root[0] = 0x12;
        root[1] = 0x24;
        root[2] = 0x46;
        root[3] = 0x56;
        root[4] = 0x76;

        String ckey = "1234qwqw1234qwqw";

//        byte[] key = ckey.getBytes();
//        byte[] a1 = Utils.Encrypt(data,key);
//
//        System.out.println("a1: "+new String(a1));
//
//        byte[] a2 = Utils.Decrypt(a1,key);
//
//        System.out.println("a2: "+new String(a2));

        //msg   id:1:msv
        if(Utils.isNotEmpty(msg)){//判断消息是否为空
            if(msg.contains(Constants.DEVICECODE)){//判断消息中是否存在 "id" 字段

                String[] id = msg.split(":");//通过 “:” 将msg消息分隔为两部分  id   1   msv
//
//                System.out.println("id[0] : "+id[0]);
//                System.out.println("id[1] : "+id[1]);
//                System.out.println("id[2] : "+id[2]);

                ioSession.setAttribute(Constants.DEVICECODE,id[1]);//将消息的 “id”  msv 保存到IoSession中:id  1
                ioSession.setAttribute("value",id[2]);
//                //打印iosession信息
//                System.out.println("iosession: "+ioSession);
                //更新DeviceList 集合中IoSession的状态
                if(DeviceList.nodeMaps.containsKey(id[1])){
                    for (String str :
                            DeviceList.nodeMaps.keySet()) {
                        System.out.println("str:" +str);

                        if (!DeviceList.nodeMaps.get(str).isConnected()){// 该传感器断开连接
                            DeviceList.nodeMaps.put(id[1],ioSession);// 将 key(id) value(IoSession)放入集合中
                            System.out.println(str+" offline!!!!");
                        }
                    }
                    
                }
                else {//向DeviceList 集合中添加 IoSession ---（id，IoSession）
                    DeviceList.nodeMaps.put(id[1],ioSession);  //加入集合  id(1)  IoSession
                }
            }
            else {
                ioSession.closeNow();
                return;
            }
        }



        //封装对象
        Sensor sensor = new Sensor(Integer.parseInt((String) ioSession.getAttribute("id")),
                Double.parseDouble((String) ioSession.getAttribute("value")),
                new Date());

        //创建json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(sensor);

        String url = "http://127.0.0.1:8088/getsensordata";

        //向web服务器发送数据json
        HttpServiceSender cl = new HttpServiceSender();
        String [] response =cl.postJson(url,json);

        System.out.println("json:" +json);
        System.out.println("++++++++++++++"+response[0]+"  "+response[1]);

        return;

    }

    //当信息已经传送给客户端后触发此方法
    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {
//
//        // TODO Auto-generated method stub
//        System.out.println("Data has been sent!!!" + o);
//        super.messageSent(ioSession, o);
//        System.out.println("执行了messageSent");
    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {

    }

    @Override
    public void event(IoSession ioSession, FilterEvent filterEvent) throws Exception {

    }
}
