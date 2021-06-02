package com.zhang.mina;

import org.apache.mina.core.session.IoSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceList {
    public static Map<String, IoSession> nodeMaps = new ConcurrentHashMap<>();
}
