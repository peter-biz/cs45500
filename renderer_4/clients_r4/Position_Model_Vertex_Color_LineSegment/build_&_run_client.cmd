javac -g -Xlint -Xdiags:verbose  -cp .;../..  %1
java                             -cp .;../..  %~n1
pause
