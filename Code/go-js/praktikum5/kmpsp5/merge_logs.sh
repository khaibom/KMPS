#!/bin/bash
# Merge server and client logs, ensuring each file ends with newline, then sort
(for f in logs/server_*.log logs/client_*.log; do
    [ -f "$f" ] && cat "$f" && echo ""
done) | grep -v '^$' | sort > logs/merged_chronological.log

