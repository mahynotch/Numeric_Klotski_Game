@echo off
if "%OS%"=="Windows_NT" setlocal enabledelayedexpansion
set "bastPath=%~dp0"
set allFile=
for /r %%i in (./test/*) do (
    set "var=%%i"
    set "allFile=!allFile! !var:%bastPath%=!"
)
set /p input=请输入想要读取的输入文件名(现有输入文件%allFile%, 输入0选择自行输入)
set /p mode=请选择想要使用的模式，terminal/gui:
if "%input%"=="0" (
javac -encoding utf-8 -d ./ ./src/*.java -cp ./lib/algs4.jar
java Main %mode% -cp ./lib/algs4.jar
for %%i in (.\*.class) do (del %%i)
) else (
javac -encoding utf-8 -d ./ ./src/*.java -cp ./lib/algs4.jar
java Main %mode% -cp ./lib/algs4.jar < ./test/%input%
for %%i in (.\*.class) do (del %%i)
)
pause