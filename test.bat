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
set /p algo=请输入想使用的算法模式AStar/BFS (默认使用AStar，BFS仅作为时长对比且无gui模式):
set /p input=请输入想要读取的输入文件名(现有输入文件%allFile%, 输入0选择自行输入, 默认自行输入):
if "%algo%"=="bfs" (
goto BFS
)
if "%algo%"=="BFS" (
goto BFS
)
set /p mode=请输入想要使用的模式，terminal/gui (默认terminal):
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
set /p again=是否再来一次?(Y/n):
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
set /p again=是否再来一次?(Y/n):
if "%again%"=="Y" (
goto start
)
if "%again%"=="y" (
goto start
)
pause
exit
