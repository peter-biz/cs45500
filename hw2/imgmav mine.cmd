::
:: This command file assumes that you have created
:: the folder C:\ImageMagick-7.1.1-39-portable-Q8-x64
::
pushd  C:\ImageMagick-7.1.1-39-portable-Q8-x64
set PATH=%CD%;%PATH%
popd
magick.exe -delay 1x30 -loop 0  Hw2_frame*.ppm  myhw2animation.gif
pause
