package com.secwager.dao.order;

import com.secwager.proto.Market.Order;
import org.apache.commons.dbutils.QueryRunner;

public class OrderRepoImpl implements OrderRepo {

  private final QueryRunner queryRunner;

  public OrderRepoImpl(QueryRunner queryRunner) {
    this.queryRunner = queryRunner;
  }

  public void insertOrder(Order order) {

  }

}