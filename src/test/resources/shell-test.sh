#!/bin/bash

sleep 1
echo 'step 10'
echo 'Hello'
sleep 5
echo 'step 70'
echo 'World'
(>&2 echo "some error")
echo 'done!'