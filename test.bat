@echo off
if "%OS%"=="Windows_NT" setlocal enabledelayedexpansion
set "bastPath=%~dp0"
set allFile=
for /r %%i in (./test/*.in) do (
    set "var=%%i"
    set "allFile=!allFile! !var:%bastPath%=!"
)
set /p input=��������Ҫ��ȡ������(���������ļ�%allFile%, ֻ����������)
javac -encoding utf-8 -d ./ ./src/*.java -cp ./lib/algs4.jar
java Main terminal -cp ./lib/algs4.jar  < ./test/%input%.in
for %%i in (.\*.class) do (del %%i)
pause