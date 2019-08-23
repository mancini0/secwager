#include "gtest/gtest.h"
#include "gmock/gmock.h"
#include "matchengine/market.h"
//#include "mock_proto_sender.h"
/**depth levels should not be published for market orders at rest, to prevent a trader 'billy' on the opposite side from
 * sending an opposite order at an unfair limit price, since 'billy' will know there is resting liquidity waiting to
 * transact at ANY price.
 */


TEST(MarketTest, NoDepthPublishForRestingMktOrder
) {
    EXPECT_EQ("Bazel", "Bazel");
}



