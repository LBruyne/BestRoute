package com.hinsliu.bestroute.utils.wechat;

import com.hinsliu.bestroute.model.output.WxUserMessageModel;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class WeChatUtil {

    private WeChatUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 解析来自微信端的xml数据
     * 
     * @param in
     * @return 微信模型
     * @throws IOException
     */
    public static WxUserMessageModel resolveXmlData(InputStream in) throws IOException {
        String xmlData = IOUtils.toString(in, "UTF-8");
        WxUserMessageModel wxXmlData = null;
        XStream xstream = new XStream();
        // 设置加载类的类加载器
        xstream.setClassLoader(WxUserMessageModel.class.getClassLoader());
        xstream.processAnnotations(WxUserMessageModel.class);
        xstream.alias("xml", WxUserMessageModel.class);
        // 安全措施，需要允许其反序列
        xstream.allowTypes(new Class[] { WxUserMessageModel.class });
        wxXmlData = (WxUserMessageModel) xstream.fromXML(xmlData);
        return wxXmlData;
    }

    /**
     * 生成返回信息
     * @param request
     * @param type
     * @param content
     * @return
     */
    public static WxUserMessageModel generateResponesModel(WxUserMessageModel request, String type, String content) {
        WxUserMessageModel response = new WxUserMessageModel();
        response.setToUserName(request.getFromUserName());
        response.setFromUserName(request.getToUserName());
        response.setCreateTime(new Date().getTime());
        response.setMsgType(type);
        response.setContent(content);
        return response;
    }

    /**
     * xml模型转xml字符串
     * @param model
     * @return
     */
    public static String toXML(WxUserMessageModel model) {
        XStream xstream = new XStream();
        xstream.processAnnotations(WxUserMessageModel.class);
        xstream.setClassLoader(WxUserMessageModel.class.getClassLoader());
        return xstream.toXML(model); // XStream的方法，直接将对象转换成 xml数据
    }
}
