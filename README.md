# Rate-limited Server

### Overview
Small project for an extensible server which supports rate limiting incoming requests.

### Coding conventions
* Arguments passed to methods and constructors are assumed to be non-nullable unless annotated as @Nullable
* Java code adheres to the [Google style guide](https://google.github.io/styleguide/javaguide.html) with the exception of an 80-character column limit

### Building
This project uses [bazel](https://bazel.build/) for managing the build. The demo can be built and run via:
```
bazel build src/java/com/caston/challenge/demo:DemoMain && ./bazel-bin/src/java/com/caston/challenge/demo/DemoMain
```
This will start a server at http://localhost:8000/demo.

### Structure
The key interfaces/classes are `RequestHandler`, `RateLimiter` and `RequestRegistry`. The design is such to allow for composition of `RequestHandlers` which may perform tasks (such as rate-limiting, but can also do logging or other operations) before passing on to delegate `RequestHandlers`.

None of the key interfaces/classes have an opinion about the protocol (e.g. HTTP) which is instead left to concrete implementations.
