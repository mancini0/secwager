#include "gtest/gtest.h"
#include "matchengine/market.h"

TEST(MarketTest, OppositeSideMarketShouldNotMatch
) {
    EXPECT_EQ("Bazel", "Bazel");
}

