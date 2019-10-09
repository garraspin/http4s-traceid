#!/usr/bin/env bash
#for i in {1..20}; do curl -H 'X-B3-TraceId: 862ce441e37c8656' -H 'X-B3-SpanId: aa10045d627ffa1d' localhost:9000/foo; done
for i in {1..5}; do curl localhost:9000/foo; done
#for i in {1..5}; do ./curl.sh; done
