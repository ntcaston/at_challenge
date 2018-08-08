package(default_visibility = ["//visibility:public"])

java_library(
    name = "RateLimiter",
    srcs = ["RateLimiter.java"],
    deps = [
        "@jsr_305//jar",
    ],
)

java_library(
    name = "UserExtractor",
    srcs = ["UserExtractor.java"],
    deps = [
        "@jsr_305//jar",
    ],
)

java_library(
    name = "InetSocketHostExtractor",
    srcs = ["InetSocketHostExtractor.java"],
    deps = [
        ":UserExtractor",
        "@jsr_305//jar",
    ],
)

java_library(
    name = "UserBasedRateLimiter",
    srcs = ["UserBasedRateLimiter.java"],
    deps = [
        ":RateLimiter",
        ":RequestHandler",
        ":ReadOnlyRequestRegistry",
        ":UserExtractor",
        "@jsr_305//jar",
        "@guava//jar",
    ],
)

java_library(
    name = "RequestRegistry",
    srcs = ["RequestRegistry.java"],
    deps = [
        ":ReadOnlyRequestRegistry",
        ":UserExtractor",
        "@jsr_305//jar",
        "@guava//jar",
    ],
)

java_library(
    name = "ReadOnlyRequestRegistry",
    srcs = ["ReadOnlyRequestRegistry.java"],
    deps = [
        "@jsr_305//jar",
    ],
)

java_library(
    name = "RegisteringRequestHandler",
    srcs = ["RegisteringRequestHandler.java"],
    deps = [
        ":RequestHandler",
        ":RequestRegistry",
        "@guava//jar",
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
