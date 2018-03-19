# Reproduction of bug that causes trace ID sometimes not to be passed

## Reproduce
Start the server by running the `Main` class.

Then execute `./reproduce.sh`

Example log with the bug occurring:

```
2018-03-19 10:08:11,652 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /0:0:0:0:0:0:0:1:61114 accepted at Mon Mar 19 10:08:11 CET 2018.
2018-03-19 10:08:11,656 [862ce441e37c8656] INFO  [Main$] [scala-execution-context-global-42] - No TraceID when jumping from scala-execution-context-global-44 to scala-execution-context-global-42
2018-03-19 10:08:11,693 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /0:0:0:0:0:0:0:1:61115 accepted at Mon Mar 19 10:08:11 CET 2018.
2018-03-19 10:08:11,697 [862ce441e37c8656] INFO  [Main$] [scala-execution-context-global-44] - No TraceID when jumping from scala-execution-context-global-42 to scala-execution-context-global-44
2018-03-19 10:08:11,730 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /0:0:0:0:0:0:0:1:61116 accepted at Mon Mar 19 10:08:11 CET 2018.
2018-03-19 10:08:11,734 [862ce441e37c8656] INFO  [Main$] [scala-execution-context-global-42] - No TraceID when jumping from scala-execution-context-global-44 to scala-execution-context-global-42
2018-03-19 10:08:11,759 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /0:0:0:0:0:0:0:1:61117 accepted at Mon Mar 19 10:08:11 CET 2018.
2018-03-19 10:08:11,764 [undefined] INFO  [Main$] [scala-execution-context-global-46] - No TraceID when jumping from scala-execution-context-global-42 to scala-execution-context-global-46
2018-03-19 10:08:11,786 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /0:0:0:0:0:0:0:1:61118 accepted at Mon Mar 19 10:08:11 CET 2018.
2018-03-19 10:08:11,791 [862ce441e37c8656] INFO  [Main$] [scala-execution-context-global-44] - No TraceID when jumping from scala-execution-context-global-46 to scala-execution-context-global-44
2018-03-19 10:08:11,809 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /0:0:0:0:0:0:0:1:61119 accepted at Mon Mar 19 10:08:11 CET 2018.
2018-03-19 10:08:11,813 [862ce441e37c8656] INFO  [Main$] [scala-execution-context-global-46] - No TraceID when jumping from scala-execution-context-global-44 to scala-execution-context-global-46
2018-03-19 10:08:11,832 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /0:0:0:0:0:0:0:1:61120 accepted at Mon Mar 19 10:08:11 CET 2018.
2018-03-19 10:08:11,836 [862ce441e37c8656] INFO  [Main$] [scala-execution-context-global-42] - No TraceID when jumping from scala-execution-context-global-46 to scala-execution-context-global-42
```

where this line the bug: `2018-03-19 10:08:11,764 [undefined] INFO  [Main$] [scala-execution-context-global-46] - No TraceID when jumping from scala-execution-context-global-42 to scala-execution-context-global-46`
