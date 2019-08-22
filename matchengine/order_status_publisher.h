#pragma once

#include "liquibook/depth_order_book.h"
#include "proto/matchengine.pb.h"
#include "proto_sender.h"

typedef std::shared_ptr<secwager::Order> OrderPtr;

class OrderStatusPublisher : public liquibook::book::OrderListener<OrderPtr> {

public:
    OrderStatusPublisher(ProtoSender *protoSender);

    void on_accept(const OrderPtr &order) override;

    void on_reject(const OrderPtr &order, const char *reason) override;

    void on_fill(const OrderPtr &order, const OrderPtr &matched_order, liquibook::book::Quantity fill_qty,
                 liquibook::book::Cost fill_cost) override;

    void on_cancel(const OrderPtr &order) override;

    void on_cancel_reject(const OrderPtr &order, const char *reason) override;

    void on_replace(const OrderPtr &order, const int32_t &size_delta,
                    liquibook::book::Price new_price) override;

    void on_replace_reject(const OrderPtr &order, const char *reason) override;

private:
    ProtoSender *protoSender;

};