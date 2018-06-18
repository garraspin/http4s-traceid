# Reproduction of bug that causes trace ID sometimes not to be passed

## Reproduce
Start the server by running the `Main` class.

Then execute `./reproduce.sh`

Example log with the bug occurring:

```
2018-06-19 00:56:49,357 [undefined] INFO  [ServerChannelGroup] [blaze-nio1-acceptor] - Connection to /0:0:0:0:0:0:0:1:52239 accepted at Tue Jun 19 00:56:49 BST 2018.
2018-06-19 00:56:49,387 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,391 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,392 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,392 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,393 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,393 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,394 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,394 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,394 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,395 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,396 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,396 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,397 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,398 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,399 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,402 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,403 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,403 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,404 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,404 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,405 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,405 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,406 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,406 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,407 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,408 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,408 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,409 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,410 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,411 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,413 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,414 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,415 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,416 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,417 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,418 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,419 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,419 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,433 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,434 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,434 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,434 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,435 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,435 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,436 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,436 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,437 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,437 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,437 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,438 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,438 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,438 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,439 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,439 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,439 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,440 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,440 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,440 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,441 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,441 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,441 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,442 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,442 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,442 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,443 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,443 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,443 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,443 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,444 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,444 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,444 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,445 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,445 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,446 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,447 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,447 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,447 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,448 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,448 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,449 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,450 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,450 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,451 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,452 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,453 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,453 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,454 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,455 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,456 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,457 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,458 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,459 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,460 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,460 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,461 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,461 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,461 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,462 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,462 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,462 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - A call is about to be made
2018-06-19 00:56:49,470 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,472 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,474 [undefined] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,475 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,477 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,478 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,480 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,483 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,484 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,485 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,486 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,487 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,488 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,489 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,490 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,491 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,506 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,507 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,508 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,510 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,511 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,512 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,513 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,515 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,516 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,519 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,520 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,523 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,525 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,526 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,528 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,530 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,531 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,532 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,533 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,534 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,535 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,536 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,537 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,538 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,539 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,540 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,541 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,542 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,543 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,546 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,547 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,549 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,550 [8566442e2c162749] INFO  [Main$] [scala-execution-context-global-62] - The call was made and returned an error
2018-06-19 00:56:49,551 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,553 [undefined] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,554 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,555 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,556 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,557 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,572 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,573 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,574 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,575 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,576 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,577 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,578 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,579 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,580 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,581 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,582 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,584 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,586 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,588 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,589 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,591 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,593 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,594 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,595 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,597 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,598 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,599 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,600 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,601 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,602 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,603 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,604 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,605 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,606 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,607 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,609 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,610 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,612 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,614 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,616 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,617 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,621 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,622 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,623 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,624 [6e6d17b754f40ca4] INFO  [Main$] [scala-execution-context-global-61] - The call was made and returned an error
2018-06-19 00:56:49,625 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,626 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,639 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,640 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error
2018-06-19 00:56:49,641 [154b0388100b831d] INFO  [Main$] [scala-execution-context-global-64] - The call was made and returned an error```

where this line the bug: `2018-03-19 10:08:11,764 [undefined] INFO  [Main$] [scala-execution-context-global-46] - No TraceID when jumping from scala-execution-context-global-42 to scala-execution-context-global-46`
