java_library(
    name = "DemoHttpHandler",
    srcs = ["DemoHttpHandler.java"],
    deps = [
        "//src/java/com/caston/challenge/server:NullRateLimiter",
        "//src/java/com/caston/challenge/server:RateLimitedRequestHandler",
        "//src/java/com/caston/challenge/server:RequestHandler",
    ],
)

# Main entry point for demo.
java_binary(
    name = "DemoMain",
    srcs = ["DemoMain.java"],
    deps = [
      "DemoHttpHandler",
    ],
)
