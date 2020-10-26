package kz.kassayev.test.listener;

import kz.kassayev.test.model.Order;
import kz.kassayev.test.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class OrderMessageListener {
    private OrderRepository orderRepository;

    private static final Logger log = LogManager.getLogger(OrderMessageListener.class);

    public OrderMessageListener(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public void receiveMessage(Map<String, Order> message){
        log.info("Received <" + message + ">");
        Order order = message.get("object");
        Order savedOrder = orderRepository.save(order);
        log.info("Added new note in table Order id:" + savedOrder.getId());

    }
}
