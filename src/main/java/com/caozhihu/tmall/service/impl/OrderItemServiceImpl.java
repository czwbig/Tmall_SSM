package com.caozhihu.tmall.service.impl;

import com.caozhihu.tmall.mapper.OrderItemMapper;
import com.caozhihu.tmall.pojo.Order;
import com.caozhihu.tmall.pojo.OrderItem;
import com.caozhihu.tmall.pojo.OrderItemExample;
import com.caozhihu.tmall.pojo.Product;
import com.caozhihu.tmall.service.OrderItemService;
import com.caozhihu.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ProductService productService;

    @Override
    public void add(OrderItem oi) {
        orderItemMapper.insert(oi);
    }

    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem oi) {
        orderItemMapper.updateByPrimaryKeySelective(oi);
    }

    @Override
    public OrderItem get(int id) {
        OrderItem result = orderItemMapper.selectByPrimaryKey(id);
        setProduct(result);//为orderitem设置product属性，product属性是非数据库字段，多对一关系
        return result;
    }

    @Override
    public List list() {
        OrderItemExample example = new OrderItemExample();
        example.setOrderByClause("id desc");
        return orderItemMapper.selectByExample(example);
    }

    @Override
    public void fill(List<Order> orders) {
        for (Order order : orders) {
            fill(order);
        }
    }

    /**
     * 因为在订单管理界面，首先是遍历多个订单，然后遍历这个订单下的多个订单项。
     * 而由MybatisGenerator逆向工程所创建的一套自动生成代码，是不具备一对多关系的，需要自己去二次开发
     * <p>
     * order一共有4个非数据库字段orderItems; user; total; totalNumber;
     * 1. 根据订单id查询出其对应的所有订单项
     * 2. 通过setProduct为所有的订单项设置Product属性
     * 3. 遍历所有的订单项，然后计算出该订单的总金额和总数量
     * 4. 最后再把订单项设置在订单的orderItems属性上。
     * 设置user交给OrderServiceImpl的list方法
     *
     * @param order 需要填充的order对象
     */
    @Override
    public void fill(Order order) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andOidEqualTo(order.getId());
        example.setOrderByClause("id desc");
        List<OrderItem> ois = orderItemMapper.selectByExample(example);
        setProduct(ois);

        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi : ois) {
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
            totalNumber += oi.getNumber();
        }
        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(ois);
    }

    public void setProduct(List<OrderItem> ois) {
        for (OrderItem oi : ois) {
            setProduct(oi);
        }
    }

    public void setProduct(OrderItem oi) {
        Product p = (Product) productService.get(oi.getPid());
        oi.setProduct(p);

    }
}
