
package me.j360.dubbo.mq;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.modules.util.mapper.JsonMapper;
import me.j360.dubbo.mq.domain.CacheSyncMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.io.UnsupportedEncodingException;


/**
 * 缓存同步模块的发送者
 *
 *
 *
 */

@Slf4j
public class CacheSyncProducer {

    private static JsonMapper binder = JsonMapper.defaultMapper();

    @Setter
    private JmsTemplate jmsTemplate;
    @Setter
    private Destination cacheQueue;

    public void send(final CacheSyncMessage cacheSyncMessage) {
        final String beanString = binder.toJson(cacheSyncMessage);
        try {
            sendMesssage(beanString, cacheQueue);
        }catch (Exception e){
            log.error("消息发送异常:{}",cacheSyncMessage.toString(),e);
        }
    }

    /**
     * 通用的调用方法
     */
    private void sendMesssage(final String beanString, Destination destination) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                try {
                    message.setBytes("json", beanString.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error("消息编码失败:{}", beanString, e);
                }
                return message;
            }
        });
    }
    
}
