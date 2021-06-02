package com.zhang.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 配置Socket
 */

@Configuration
public class MinaConfig {
    //拦截器
    @Bean
    public IoHandler ioHandlerG(){
        return new MyIoHandler();
    }

    //端口号
    @Bean
    public InetSocketAddress inetSocketAddress(){
        return new InetSocketAddress(8089);
    }

    @Bean
    public IoAcceptor ioAcceptorG() throws IOException {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();

//        //增加编码过滤器,统一编码UTF-8
//        acceptor.getFilterChain().addLast("codec",
//                new ProtocolCodecFilter(new TextLineCodecFactory()));

        //设置服务端逻辑处理器
        acceptor.setHandler(ioHandlerG());

        //设置读缓存大小
        acceptor.getSessionConfig().setReadBufferSize(2048);

        //设置指定类型的空闲时间，空闲时间超过这个值将触发 sessionIdle方法
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,120);

        //绑定端口号
        acceptor.bind(inetSocketAddress());

        return acceptor;

    }
}
