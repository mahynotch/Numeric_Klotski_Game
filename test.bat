:start
@echo off
if "%OS%"=="Windows_NT" setlocal enabledelayedexpansion
set "bastPath=%~dp0"
set allFile=
for /r %%i in (./test/*) do (
    set "var=%%i"
    set "allFile=!allFile! !var:%bastPath%=!"
)
set mode=terminal
set algo=AStar
set input=0
set /p algo=��������ʹ�õ��㷨ģʽAStar/BFS (Ĭ��ʹ��AStar��BFS����Ϊʱ���Ա�����guiģʽ):
set /p input=��������Ҫ��ȡ�������ļ���(���������ļ�%allFile%, ����0ѡ����������, Ĭ����������):
if "%algo%"=="bfs" (
goto BFS
)
if "%algo%"=="BFS" (
goto BFS
)
set /p mode=��������Ҫʹ�õ�ģʽ��terminal/gui (Ĭ��terminal):
if "%input%"=="0" (
javac -encoding utf-8 -d ./ ./src/*.java -cp ./lib/algs4.jar
java Main %mode% -cp ./lib/algs4.jar
for %%i in (.\*.class) do (del %%i)
) else (
javac -encoding utf-8 -d ./ ./src/*.java -cp ./lib/algs4.jar
java Main %mode% -cp ./lib/algs4.jar < ./test/%input%
for %%i in (.\*.class) do (del %%i)
)
set again=Y
set /p again=�Ƿ�����һ��?(Y/n):
if "%again%"=="Y" (
goto start
)
if "%again%"=="y" (
goto start
)
pause
exit


:BFS
if "%input%"=="0" (
javac -encoding utf-8 -d ./ ./src/*.java -cp ./lib/algs4.jar
java BFS -cp ./lib/algs4.jar
for %%i in (.\*.class) do (del %%i)
) else (
javac -encoding utf-8 -d ./ ./src/*.java -cp ./lib/algs4.jar
java BFS -cp ./lib/algs4.jar < ./test/%input%
for %%i in (.\*.class) do (del %%i)
)
set again=Y
set /p again=�Ƿ�����һ��?(Y/n):
if "%again%"=="Y" (
goto start
)
if "%again%"=="y" (
goto start
)
pause
exit
