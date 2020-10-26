package kz.kassayev.test.service;

import kz.kassayev.test.TestApplication;
import kz.kassayev.test.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendOrderMessage(Order order) {
        Map<String, Order> actionmap = new HashMap<>();
        actionmap.put("object", order);
        rabbitTemplate.convertAndSend(TestApplication.SFG_MESSAGE_QUEUE, actionmap);

    }
}
