package(default_visibility = ["//visibility:public"])

java_library(
    name = "RateLimiter",
    srcs = ["RateLimiter.java"],
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
)

java_library(
    name = "Server",
    srcs = ["Server.java"],
    deps = [
        ":RateLimiter",
        ":RequestHandler",
        "@guava//jar",
    ],
)
