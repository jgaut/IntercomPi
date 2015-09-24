#!/bin/sh
sudo kill $(ps aux |grep -v grep|grep java|awk -F' ' '{print $2}')
