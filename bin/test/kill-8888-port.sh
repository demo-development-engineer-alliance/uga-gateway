#!/bin/zsh
lsof -ti:8888 | xargs kill -9
echo "Done..."