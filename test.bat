@echo off
if "%OS%"=="Windows_NT" setlocal enabledelayedexpansion
set "bastPath=%~dp0"
set allFile=
for /r %%i in (./src/*.java) do (
    set "var=%%i"
    set "allFile=!allFile! ./src/!var:%bastPath%=!"
)
echo %allFile%
javac -encoding utf-8  --source-path %allFile% -d ./
java Main terminal < ./test/1.txt
for %%i in (.\*.class) do (del %%i)
pause