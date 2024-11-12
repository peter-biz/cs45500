::
:: This command file assumes that you have created
:: the folder C:\ImageMagick-7.1.1-38-portable-Q8-x64
::
pushd  C:\ImageMagick-7.1.1-39-portable-Q16-x64
set PATH=%CD%;%PATH%
popd
convert -delay 3x100 -loop 0 Hw2_frame*.ppm animation.gif
pause
