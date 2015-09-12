#!/bin/sh
kill $(ps aux |grep IntercomPi|grep java|awk -F' ' '{print $2}')