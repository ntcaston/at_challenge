java_library(
    name = "DemoHttpHandler",
    srcs = ["DemoHttpHandler.java"],
    deps = [
        "//src/java/com/caston/challenge/server:InetSocketHostExtractor",
        "//src/java/com/caston/challenge/server:NullRateLimiter",
        "//src/java/com/caston/challenge/server:RateLimitedRequestHandler",
        "//src/java/com/caston/challenge/server:RegisteringRequestHandler",
        "//src/java/com/caston/challenge/server:RequestHandler",
        "//src/java/com/caston/challenge/server:RequestRegistry",
        "//src/java/com/caston/challenge/server:UserBasedRateLimiter",
        "//src/java/com/caston/challenge/server:UserExtractor",
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
