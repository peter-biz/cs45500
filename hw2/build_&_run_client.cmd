javac -g -Xlint -Xdiags:verbose  -cp .;renderer_4.jar  %1
java                             -cp .;renderer_4.jar  %~n1
pause
