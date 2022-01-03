package com.hinsliu.bestroute.model.output;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * Wechat user message
 */
@Data
@XStreamAlias("xml")
public class WxUserMessageModel {

    @XStreamAlias("ToUserName")
    private String toUserName;

    @XStreamAlias("FromUserName")
    private String fromUserName;

    @XStreamAlias("CreateTime")
    private Long createTime;

    @XStreamAlias("MsgType")
    private String msgType;

    @XStreamAlias("Content")
    private String content;

    @XStreamAlias("MsgId")
    private String msgId;

    @XStreamAlias("Title")
    private String title;

    @XStreamAlias("Description")
    private String description;

    @XStreamAlias("Url")
    private String url;

    @XStreamAlias("Event")
    private String event;

    @XStreamAlias("EventKey")
    private String eventkey;
}
