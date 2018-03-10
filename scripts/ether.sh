#!/bin/bash

base=$HOME/ws/trust/scripts

curl -X POST -H "Content-Type: application/json" --data @${base}/$1 localhost:8080/rpc/ether | json_pp
