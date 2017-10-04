package com.thinkgem.jeesite.api.service;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.api.entity.handler.TextMessage;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.config.WechatConfig;
import com.thinkgem.jeesite.mapper.AccessTokenMapper;
import com.thinkgem.jeesite.modules.manager.users.entity.Users;
import com.thinkgem.jeesite.modules.manager.users.service.UsersService;
import com.thinkgem.jeesite.util.HttpUtil;
import com.thinkgem.jeesite.util.XMLUtil;
import com.thinkgem.jeesite.vo.AccessToken;
import com.thinkgem.jeesite.vo.AccessTokenExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 微信公众号需要的service
 * Created by Administrator on 2017/10/4.
 */
@Service
public class WechatApiService {

    @Autowired
    private AccessTokenMapper accessTokenMapper;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private UsersService usersService;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public AccessToken getWechatToken() {
        Date now = new Date();
        AccessTokenExample example = new AccessTokenExample();
        AccessTokenExample.Criteria query = example.createCriteria();
        query.andInTimeLessThan(now).andInOutTimeGreaterThan(now);
        List<AccessToken> list = accessTokenMapper.selectByExample(example);

        if (list.isEmpty()) {
            String result = HttpUtil.sendPostRequest(wechatConfig.token_url, "grant_type=client_credential&appid=" + wechatConfig.app_id + "&secret=" + wechatConfig.app_sercet, true);
            AccessToken token = JSONObject.parseObject(result, AccessToken.class);
            token.setInTime(now);
            token.setInOutTime(getTwoHoursDate(now));
            token.setTokenType(0);
            accessTokenMapper.insert(token);
            return token;
        } else
            return list.get(0);

    }

    /**
     * 微信消息处理
     * @param request
     * @return
     */
    public String processRequest(HttpServletRequest request) {
        Map<String, String> map = XMLUtil.xmlToMap(request);
        logger.info(JSONObject.toJSONString(map));
        // 发送方帐号（一个OpenID）
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        // 消息类型
        String msgType = map.get("MsgType");
        //事件类型
        String event = map.get("Event");
        // 默认回复一个"success"
        String responseMessage = "success";
        //关注取关
        if(StringUtils.isNotBlank(event)){
            logger.info("关注。。。。");
            //关注
            if (event.equalsIgnoreCase("subscribe")) {
                Users user = usersService.weChatRegister(fromUserName);
                // 对消息进行处理
                TextMessage textMessage = new TextMessage();
                textMessage.setMsgType(XMLUtil.MESSAGE_TEXT);
                textMessage.setToUserName(fromUserName);
                textMessage.setFromUserName(toUserName);
                textMessage.setCreateTime(System.currentTimeMillis());
                textMessage.setContent("关注红包已发送,红包有效时间为2天!");
                responseMessage = XMLUtil.textMessageToXml(textMessage);
            } else if (event.equalsIgnoreCase("unsubscribe")) {  //取消关注
                logger.info("取关。。。。");
                usersService.weChatCancel(fromUserName);
            }
        }else{
            logger.info("文本消息");
            TextMessage textMessage = new TextMessage();
            textMessage.setMsgType(XMLUtil.MESSAGE_TEXT);
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(System.currentTimeMillis());
            textMessage.setContent("!!!!");
            responseMessage = XMLUtil.textMessageToXml(textMessage);
        }


        logger.info(responseMessage);
        return responseMessage;

    }


    private Date getTwoHoursDate(Date now) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(now);
        ca.add(Calendar.HOUR_OF_DAY, 2);
        return ca.getTime();
    }
}
