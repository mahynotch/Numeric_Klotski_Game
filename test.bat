@echo off
if "%OS%"=="Windows_NT" setlocal enabledelayedexpansion
set "bastPath=%~dp0"
set allFile=
for /r %%i in (./test/*.in) do (
    set "var=%%i"
    set "allFile=!allFile! !var:%bastPath%=!"
)
set /p input=��������Ҫ��ȡ������(���������ļ�%allFile%, ֻ����������)
javac -encoding utf-8 -d ./ ./src/*.java
java Main < ./test/%input%.in
for %%i in (.\*.class) do (del %%i)
pause