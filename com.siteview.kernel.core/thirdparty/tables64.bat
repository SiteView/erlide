@echo off

FOR /F "skip=2 tokens=3-6" %%a IN ('@reg query "HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\MySQL AB\MySQL Server 5.0" /v Location') do set mysqlml=%%a %%b %%c %%d

set path=%path%;%mysqlml%bin\;
set path

FOR /F "skip=2 tokens=3-6" %%a IN ('@reg query "HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\SiteViewECC 9.1" /v Path') do set mysoft=%%a

echo %mysoft%\thirdparty\
cd %mysoft%\thirdparty\

mysql --user=root --password=system --execute="SHOW DATABASES;"

mysql --user=root --password=system --execute="source ./ofbiz.sql"

mysql --user=root --password=system --execute="source ./ofbizolap.sql"

mysql --user=root --password=system --execute="source ./ofbiztenant.sql"

mysql --user=root --password=system --execute="SHOW DATABASES;"

pause
cls

exit
