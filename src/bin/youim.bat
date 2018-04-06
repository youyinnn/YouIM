@echo off
echo Launching YouIm....
cd ..
dir /B|find "you" > jar.txt
set /P jar=<jar.txt
del jar.txt
java -jar %jar%
cd bin