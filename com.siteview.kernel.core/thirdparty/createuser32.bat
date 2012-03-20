@echo off

FOR /F "skip=2 tokens=3-6" %%a IN ('@reg query "HKEY_LOCAL_MACHINE\SOFTWARE\Ericsson\Erlang\ErlSrv\1.1\ejabberd" /v WorkDir') do set ejbml=%%a %%b

FOR /F "skip=2 tokens=3-6" %%a IN ('@reg query "HKEY_LOCAL_MACHINE\SYSTEM\ControlSet001\Services\Tcpip\Parameters" /v Hostname') do set host=%%a

set path=%path%;%ejbml%;
set path

FOR /F "skip=2 tokens=3-6" %%a IN ('@reg query "HKEY_LOCAL_MACHINE\SOFTWARE\SiteViewECC 9.1" /v Path') do set mysoft=%%a

echo %mysoft%\thirdparty\
cd %mysoft%\thirdparty\

call ejabberdctl start
call ejabberdctl status
call ejabberdctl register erlangnode %host% 3ren
call ejabberdctl register debugger1 %host% 3ren
call ejabberdctl register debugger2 %host% 3ren 
call ejabberdctl register debugger3 %host% 3ren
call ejabberdctl register logger1 %host% 3ren
call ejabberdctl register logger2 %host% 3ren
call ejabberdctl register logger3 %host% 3ren
call ejabberdctl register topo1 %host% 3ren
call ejabberdctl register topo2 %host% 3ren

pause
cls

exit
