# Reproduction of bug that causes trace ID sometimes not to be passed

## Reproduce
Start the server by running the `Main` class.

Then execute `./reproduce.sh`

Example log with the bug occurring:

```
2018-03-20 14:05:18,819 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /0:0:0:0:0:0:0:1:53078 accepted at Tue Mar 20 14:05:18 CET 2018.
2018-03-20 14:05:19,393 [862ce441e37c8656] INFO  [Api] [scala-execution-context-global-95] - Got Ping(ping)
2018-03-20 14:05:19,413 [862ce441e37c8656] INFO  [HttpClient] [scala-execution-context-global-95] - POST http://localhost:9000/pang {
  "msg" : "ping"
}
2018-03-20 14:05:19,443 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /127.0.0.1:53079 accepted at Tue Mar 20 14:05:19 CET 2018.
2018-03-20 14:05:19,493 [862ce441e37c8656] INFO  [Api] [scala-execution-context-global-115] - Pang HS: localhost:9000,application/json,application/json,862ce441e37c8656,0,e68459eb418f726c,foos,33b36db3bc6b0f72,http4s-blaze/0.18.0,14
2018-03-20 14:05:19,575 [undefined] INFO  [Api] [scala-execution-context-global-115] - Got Pang(pang)
2018-03-20 14:05:19,582 [undefined] INFO  [HttpClient] [scala-execution-context-global-115] - POST http://localhost:9000/peng {
  "msg" : "ping"
}
2018-03-20 14:05:19,592 [ebf4284c245a230c] INFO  [Api] [scala-execution-context-global-38] - Peng HS: localhost:9000,application/json,application/json,http4s-blaze/0.18.0,14
2018-03-20 14:05:19,599 [undefined] INFO  [Api] [scala-execution-context-global-38] - Got Peng(peng)
2018-03-20 14:05:19,601 [undefined] INFO  [Api] [scala-execution-context-global-38] - Responding Pong(ping pang peng)

```
