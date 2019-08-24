#include "gtest/gtest.h"
#include "gmock/gmock.h"
#include "matchengine/market.h"

using testing::AtLeast;
using testing::_;

//TODO use this Mock when I test my actual marketplace.
class MockProtoSender : public ProtoSender {
public:
    MOCK_METHOD(void, send, (const google::protobuf::MessageLite &msg, const std::string &address), (override));
};


class DepthbookTest : public testing::Test {
    typedef std::shared_ptr<secwager::Order> OrderPtr;
    typedef liquibook::book::DepthOrderBook<OrderPtr> DepthBook;

protected:
    DepthBook *book;

    DepthbookTest() {
        book = new DepthBook("AMZN");
    }

    virtual ~DepthbookTest() {
        delete book;
    }
};



/**depth levels should not be published for market orders at rest, to prevent a trader on the opposite side from
 * sending an opposite order at an unfair limit price, since the trader will know there is resting liquidity waiting to
 * transact at ANY price.
 */
TEST_F(DepthbookTest, NoDepthPublishForRestingMktOrder
) {
    auto order = std::make_shared<secwager::Order>();
    order->set_order_qty(100);
    order->set_is_limit(false);
    order->set_is_buy(true);
    order->set_order_id("buy_amzn_at_mkt");
    book->add(order);
    ASSERT_EQ(book->bids().size(), 1);
    ASSERT_EQ(book->depth().bids()->aggregate_qty(), 0);
}

/**
 * If there is no current liquidity in the market, two NON-LIMIT (market) orders on opposite sides should not
 * fill against each other.
 */
TEST_F(DepthbookTest, NoMarketAgainstMarketExecutions) {
    auto buyMkt = std::make_shared<secwager::Order>();
    auto sellMkt = std::make_shared<secwager::Order>();
    buyMkt->set_order_qty(100);
    buyMkt->set_is_limit(false);
    buyMkt->set_is_buy(true);
    buyMkt->set_order_id("buy_amzn_at_mkt");
    book->add(buyMkt);

    sellMkt->set_order_qty(250);
    sellMkt->set_is_limit(false);
    sellMkt->set_is_buy(false);
    sellMkt->set_order_id("sell_amzn_at_mkt");
    book->add(sellMkt);

    ASSERT_EQ(book->bids().size(), 1);
    ASSERT_EQ(book->asks().size(), 1);
    ASSERT_EQ(book->depth().bids()->aggregate_qty(), 0);
    ASSERT_EQ(book->depth().asks()->aggregate_qty(), 0);
}

/** A resting market order should execute against an incoming limit order on the opposite side.
 *  If there remains unfilled liquidity associated with the limit order,
 *  the depth book should publish that information.
 * **/

TEST_F(DepthbookTest, RestingMarketExecutionAgainstIncomingLimitLargerIncomingQty) {
    auto buyMkt = std::make_shared<secwager::Order>();
    auto sellLimit = std::make_shared<secwager::Order>();
    buyMkt->set_order_qty(100);
    buyMkt->set_is_limit(false);
    buyMkt->set_is_buy(true);
    buyMkt->set_order_id("buy_amzn_at_mkt");
    book->add(buyMkt);

    sellLimit->set_order_qty(105);
    sellLimit->set_price(1200);
    sellLimit->set_is_limit(true);
    sellLimit->set_is_buy(false);
    sellLimit->set_order_id("sell_105_amzn_1200_bucks");
    book->add(sellLimit);

    ASSERT_EQ(book->bids().size(), 0); //no remaining buy-side liquidity, buyMkt should've been filled completely
    ASSERT_EQ(book->asks().size(), 1); //a limit order for 5 shares @ 1200 should remain unfilled
    ASSERT_EQ(book->depth().bids()->aggregate_qty(), 0);
    ASSERT_EQ(book->depth().asks()->aggregate_qty(),
              5); //the depth book should publish the unfilled 5@1200 level, since it was a limit order.
}


/** A resting market order should execute against an incoming limit order on the opposite side.
 *  If there remains unfilled liquidity associated with the market order,
 *  the depth book should NOT publish that information.
 * **/

TEST_F(DepthbookTest, RestingMarketExecutionAgainstIncomingLimitLargerRestingQty) {
    auto buyMkt = std::make_shared<secwager::Order>();
    auto sellLimit = std::make_shared<secwager::Order>();
    buyMkt->set_order_qty(200);
    buyMkt->set_is_limit(false);
    buyMkt->set_is_buy(true);
    buyMkt->set_order_id("buy_amzn_at_mkt");
    book->add(buyMkt);

    sellLimit->set_order_qty(105);
    sellLimit->set_price(1200);
    sellLimit->set_is_limit(true);
    sellLimit->set_is_buy(false);
    sellLimit->set_order_id("sell_30_amzn_1200_bucks");
    book->add(sellLimit);

    ASSERT_EQ(book->bids().size(), 1); //remaining buy-side liquidity, 95@mkt
    ASSERT_EQ(book->asks().size(), 0); //limit order completely filled
    ASSERT_EQ(book->depth().bids()->aggregate_qty(), 0);
    ASSERT_EQ(book->depth().asks()->aggregate_qty(), 0);//the depth book should not publish the resting mkt order depth.
}
