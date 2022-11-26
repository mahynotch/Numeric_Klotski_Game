@echo off

javac -encoding utf-8 -d ./ ./src/*.java
java Main < ./test/1.txt
for %%i in (.\*.class) do (del %%i)
pause