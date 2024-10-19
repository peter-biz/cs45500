javac -g -Xlint -Xdiags:verbose  -cp .;..  %1
java  -Dsun.java2d.uiScale=1.0   -cp .;..  %~n1
pause
