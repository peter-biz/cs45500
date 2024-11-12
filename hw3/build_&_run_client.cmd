javac -g -Xlint -Xdiags:verbose  -cp .;renderer_7.jar  %1
java  -Dsun.java2d.uiScale=1.0   -cp .;renderer_7.jar  %~n1
pause
