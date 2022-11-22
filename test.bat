@echo off

for %%i in (.\src\*.class) do (%%i > list.txt
echo , > list.txt)
javac -encoding=utf-8 -sourcefiles 
java Main < ./test/1.txt
pause
for %%i in (.\src\*.class) do (del %%i)