package(default_visibility = ["//visibility:public"])

java_library(
    name = "RateLimiter",
    srcs = ["RateLimiter.java"],
    deps = [
        "@jsr_305//jar",
    ],
)

java_library(
    name = "NullRateLimiter",
    srcs = ["NullRateLimiter.java"],
    deps = [
        ":RateLimiter",
    ],
)

java_library(
    name = "RequestHandler",
    srcs = ["RequestHandler.java"],
    deps = [
        "@jsr_305//jar",
    ],
)

java_library(
    name = "RateLimitedRequestHandler",
    srcs = ["RateLimitedRequestHandler.java"],
    deps = [
        ":RateLimiter",
        ":RequestHandler",
        "@guava//jar",
        "@jsr_305//jar",
    ],
)
