@echo off
if "%OS%"=="Windows_NT" setlocal enabledelayedexpansion
set "bastPath=%~dp0"
set allFile=
for /r %%i in (./test/*) do (
    set "var=%%i"
    set "allFile=!allFile! !var:%bastPath%=!"
)
set /p input=��������Ҫ��ȡ�������ļ���(���������ļ�%allFile%, ����0ѡ����������)
set /p mode=��ѡ����Ҫʹ�õ�ģʽ��terminal/gui:
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