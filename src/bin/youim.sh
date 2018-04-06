#!/usr/bin/env bash
# author:youyinnn
# url: https://github.com/youyinnn/YouIM

arglen=$#

get_pid(){
    pname="`find .. -name 'you*.jar'`"
    pname=${pname:3}
    echo "$pname"
}

if [ $arglen -eq 0 ]
 then
    echo "show help"
else
    case "$1" in
        "getp")
            get_pid
            ;;
    esac
fi