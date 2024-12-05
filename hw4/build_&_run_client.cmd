javac -g -Xlint -Xdiags:verbose  -cp .;renderer_9.jar  %1
java  -Dsun.java2d.uiScale=1.0   -cp .;renderer_9.jar  %~n1
pause
