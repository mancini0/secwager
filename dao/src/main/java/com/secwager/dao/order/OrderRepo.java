package com.secwager.dao.order;

import com.secwager.proto.Market.Order;

public interface OrderRepo {

  void insertOrder(Order order, String userId);

  void deleteOrder(Order order);

}