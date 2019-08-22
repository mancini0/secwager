#include "order_status_publisher.h"


OrderStatusPublisher::OrderStatusPublisher(ProtoSender *protoSender) : protoSender(protoSender) {}


void OrderStatusPublisher::on_accept(const OrderPtr &order) {
    order->set_state(secwager::Order_State_ACCEPTED);
    order->set_qty_on_market(order->order_qty());
    order->set_fill_cost(0);
    order->set_qty_filled(0);
    protoSender->send(*order, order->order_id());
};

void OrderStatusPublisher::on_reject(const OrderPtr &order, const char *reason) {

    order->set_state(secwager::Order_State_REJECTED);
    order->set_rejected_reason(reason);
    protoSender->send(*order, order->order_id());

};


void
OrderStatusPublisher::on_fill(const OrderPtr &order, const OrderPtr &matched_order, liquibook::book::Quantity fill_qty,
                              liquibook::book::Cost fill_cost) {

    order->set_qty_filled(order->qty_filled() + fill_qty);
    order->set_qty_on_market(order->qty_on_market() - fill_qty);
    order->set_fill_cost(order->fill_cost() + fill_cost);
    order->qty_on_market() > 0 ? order->set_state(secwager::Order_State_PARTIAL_FILLED)
                               : order->set_state(secwager::Order_State_FILLED);
    order->add_matched_against(matched_order->order_id());
    protoSender->send(*order, order->order_id());

    matched_order->set_qty_filled(matched_order->qty_filled() + fill_qty);
    matched_order->set_qty_on_market(matched_order->qty_on_market() - fill_qty);
    matched_order->set_fill_cost(matched_order->fill_cost() + fill_cost);
    matched_order->qty_on_market() > 0 ? matched_order->set_state(secwager::Order_State_PARTIAL_FILLED)
                                       : matched_order->set_state(secwager::Order_State_FILLED);
    matched_order->add_matched_against(order->order_id());
    protoSender->send(*matched_order, matched_order->order_id());
};

void OrderStatusPublisher::on_cancel(const OrderPtr &order) {
    order->set_qty_on_market(0);
    order->set_state(secwager::Order_State_CANCELLED);
    protoSender->send(*order, order->order_id());
};

//TODO
void OrderStatusPublisher::on_cancel_reject(const OrderPtr &order, const char *reason) {};

//TODO
void OrderStatusPublisher::on_replace(const OrderPtr &order, const int32_t &size_delta,
                                      liquibook::book::Price new_price) {};

//TODO
void OrderStatusPublisher::on_replace_reject(const OrderPtr &order, const char *reason) {};
