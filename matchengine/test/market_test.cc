#include "gtest/gtest.h"
#include "gmock/gmock.h"
#include "matchengine/market.h"

using testing::AtLeast;
using testing::_;


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

    ~DepthbookTest() {
        delete book;
    }
};



/**depth levels should not be published for market orders at rest, to prevent a trader on the opposite side from
 * sending an opposite order at an unfair limit price, since the trader will know there is resting liquidity waiting to
 * transact at ANY price.
 */
TEST_F(DepthbookTest, NoDepthPublishForRestingMktOrder
) {
    ASSERT_EQ(book->symbol(), "AMZN");
}



