java_library(
    name = "FakeClock",
    srcs = ["FakeClock.java"],
    testonly = 1,
    deps = [
        "@guava//jar",
    ],
)

java_test(
    name = "RateLimitedRequestHandlerTest",
    srcs = ["RateLimitedRequestHandlerTest.java"],
    deps = [
        "//src/java/com/caston/challenge/server:RateLimitedRequestHandler",
        "//src/java/com/caston/challenge/server:RateLimiter",
        "//src/java/com/caston/challenge/server:RequestHandler",
        "@mockito//jar",
    ],
)

java_test(
    name = "UserBasedRateLimiterTest",
    srcs = ["UserBasedRateLimiterTest.java"],
    deps = [
        ":FakeClock",
        "//src/java/com/caston/challenge/server:RateLimiter",
        "//src/java/com/caston/challenge/server:ReadOnlyRequestRegistry",
        "//src/java/com/caston/challenge/server:RequestHandler",
        "//src/java/com/caston/challenge/server:UserBasedRateLimiter",
        "//src/java/com/caston/challenge/server:UserExtractor",
        "@mockito//jar",
    ],
)
