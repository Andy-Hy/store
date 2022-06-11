package com.cy.store.mapper;

import com.cy.store.entity.Order;
import com.cy.store.entity.OrderItem;

/** 订单持久层接口*/
public interface OrderMapper {


    /**
     * 插入订单数据
     * @param order  订单数据
     * @return 收影响的行数
     */
    Integer insertOrder(Order order);
    /**
     * 插入单项数据
     * @param orderItem
     * @return
     */
    Integer insertOrderItem(OrderItem orderItem);
}
