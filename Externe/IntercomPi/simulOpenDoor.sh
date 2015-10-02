#!/bin/sh
/usr/local/bin/gpio mode 7 OUT ; /usr/local/bin/gpio write 7 1 ; sleep 2s ; /usr/local/bin/gpio write 7 0 ;