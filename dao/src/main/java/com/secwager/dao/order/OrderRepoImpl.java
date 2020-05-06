package com.secwager.dao.order;

import com.secwager.proto.Market.Order;
import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;

public class OrderRepoImpl implements OrderRepo {

  private final QueryRunner queryRunner;

  public OrderRepoImpl(QueryRunner queryRunner) {
    this.queryRunner = queryRunner;
  }

  @Override
  public void insertOrder(Order order, String userId) {

  }

  @Override
  public void deleteOrder(Order order) {
    try {
      queryRunner.execute("update orders set order_status = 'SOFT_DELETE' where order_id = ?",
          order.getOrderId());
    } catch (SQLException e) {

    }
  }

}