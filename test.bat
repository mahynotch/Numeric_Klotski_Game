@echo off

javac -encoding utf-8  --source-path ./src/Board.java ./src/Piece.java ./src/Main.java ./src/Coordinate.java -d ./
java Main < ./test/1.txt
for %%i in (.\*.class) do (del %%i)
pause