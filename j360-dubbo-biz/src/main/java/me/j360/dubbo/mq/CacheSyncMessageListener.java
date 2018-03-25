package me.j360.dubbo.mq;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.cache.CacheSyncManager;
import me.j360.dubbo.modules.util.mapper.JsonMapper;
import me.j360.dubbo.mq.domain.CacheSyncMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 消息的异步被动接收者.
 * 
 *
 */
@Slf4j
public class CacheSyncMessageListener implements MessageListener {

    private static JsonMapper binder = JsonMapper.nonNullMapper();

    @Autowired
    private CacheSyncManager cacheSyncManager;

    /**
	 * MessageListener回调函数.
	 */
	@Override
	public void onMessage(Message message) {
		try {
			MapMessage mapMessage = (MapMessage) message;
			// 打印消息详情
            byte[] bytes = mapMessage.getBytes("json");
            String json = new String(bytes,"utf-8");
            CacheSyncMessage dto = binder.fromJson(json, CacheSyncMessage.class);
            cacheSyncManager.writeMQCount(dto);
            message.acknowledge();
        } catch (Exception e) {
			log.error("处理消息时发生异常: [{}]", message.toString(), e);
		}
	}
}
