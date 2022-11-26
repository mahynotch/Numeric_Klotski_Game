@echo off

javac -encoding utf-8 -d ./ ./src/*.java
java Main < ./test/1.in
for %%i in (.\*.class) do (del %%i)
pause