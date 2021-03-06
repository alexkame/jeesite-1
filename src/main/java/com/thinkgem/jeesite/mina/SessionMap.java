package com.thinkgem.jeesite.mina;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;

import javax.mail.Session;


/**
 * @author whl
 * @Description: 单例工具类，保存所有mina客户端连接
 * @date 2014-9-29 上午10:09:15
 */
public class SessionMap {

    private final static Log log = LogFactory.getLog(SessionMap.class);

    private static SessionMap sessionMap = null;

    private static Map<String, IoSession> map = new HashMap<String, IoSession>();


    //构造私有化 单例
    private SessionMap() {
    }


    /**
     * @Description: 获取唯一实例
     * @author whl
     * @date 2014-9-29 下午1:29:33
     */
    public static SessionMap newInstance() {
        log.debug("SessionMap单例获取---");
        if (sessionMap == null) {
            sessionMap = new SessionMap();
        }
        return sessionMap;
    }

    public static void  removeSession(IoSession ioSession) {
        for (String key : map.keySet()) {
            //map.keySet()返回的是所有key的值
            IoSession saveIosession = map.get(key);//得到每个key多对用value的值
            if (saveIosession == ioSession) {
                map.remove(key);
                log.info("删除iossession： " + key);
            }
        }
    }


    /**
     * @Description: 保存session会话
     * @author whl
     * @date 2014-9-29 下午1:31:05
     */
    public void addSession(String key, IoSession session) {
        log.info("保存会话到SessionMap单例---key=" + key);
        this.map.put(key, session);
    }

    /**
     * @Description: 根据key查找缓存的session
     * @author whl
     * @date 2014-9-29 下午1:31:55
     */
    public static IoSession getSession(String key) {
        log.debug("获取会话从SessionMap单例---key=" + key);
        return map.get(key);
    }

    public static IoSession removeSession(String key) {
        log.debug("获取会话从SessionMap单例---key=" + key);
        return map.remove(key);
    }

    /**
     * @Description: 发送消息到客户端
     * @author whl
     * @date 2014-9-29 下午1:57:51
     */
    public static void sendMessage(String[] keys, Object message) {
        for (String key : keys) {
            IoSession session = getSession(key);

            log.info("服务通知客户端session: " + session.getId() + ",消息: " + message);
            if (session == null) {
                log.info("mina,session消息异常");
                return;
            }
            session.write(message);

        }
    }


    public static void sendMessage(String key, Object message) {
        IoSession session = getSession(key);
        log.info("服务通知客户端session: " + session.getId() + ",消息: " + message);
        if (session == null) {
            log.info("mina,session消息异常");
            return;
        }
        session.write(message + "@@@");

    }

}
