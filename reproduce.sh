#!/usr/bin/env bash
for i in {1..5}; do  curl -v -H 'X-Foo-Header: foos' -H 'X-B3-TraceId: 862ce441e37c8656' -H 'X-B3-SpanId: aa10045d627ffa1d' -d '{"msg":"ping"}' localhost:9000/pong; done
