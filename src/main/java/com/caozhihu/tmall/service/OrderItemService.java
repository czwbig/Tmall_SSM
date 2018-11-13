package com.caozhihu.tmall.service;

import com.caozhihu.tmall.pojo.Order;
import com.caozhihu.tmall.pojo.OrderItem;

import java.util.List;

public interface OrderItemService {
    void add(OrderItem oi);

    void delete(int id);

    void update(OrderItem oi);

    OrderItem get(int id);

    List list();

    void fill(List<Order> orders);

    void fill(Order order);
}
