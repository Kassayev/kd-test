package kz.kassayev.test.service;

import kz.kassayev.test.model.Order;
import org.springframework.stereotype.Service;

public interface MessageService {
    void sendOrderMessage(Order order);
}
