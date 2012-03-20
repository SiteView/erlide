# Auto-generated by EclipseNSIS Script Wizard
# 2010-2-9 14:55:04

!define STR_NAME "SiteViewECC"

# General Symbol Definitions

!define VERSION 9.1
!define FILEVERSION 9.1.2012.0106
!define COMPANY "dragonflow"
Name "SiteView ECC ${VERSION}"

!define REGKEY "SOFTWARE\${STR_NAME} ${VERSION}"

# MUI Symbol Definitions
!define MUI_ICON logo.ico
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_UNFINISHPAGE_NOAUTOCLOSE

;修改文件字符串所用的宏
!macro ReplaceConfig FileName StringOld StringNew
    Push '${StringOld}'     #itsm
    Push '${StringNew}'     #erlang hostname
    Push all                #replace all occurrences
    Push all                #replace all occurrences
    Push '${FileName}'      #file to replace in
    Call AdvReplaceInFile
!macroend
# Included files
!include Sections.nsh
!include MUI2.nsh
!include StrFunc.nsh
!include TextReplace.nsh
${StrRep}

# Reserved Files
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

# Variables
Var StartMenuGroup

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "$(LNG_License)"
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES

!define MUI_FINISHPAGE_SHOWREADME
;!define MUI_FINISHPAGE_SHOWREADME_FUNCTION Info
!define MUI_FINISHPAGE_SHOWREADME_TEXT "$(LNG_Open)"

!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH

#!define MUI_FINISHPAGE_RUN "$DESKTOP\SiteView ECC 9.0.url"

# Installer languages
!insertmacro MUI_LANGUAGE SimpChinese
;!insertmacro MUI_LANGUAGE English

# Installer attributes
OutFile "${STR_NAME}_nnm_${FILEVERSION}_setup.exe"
InstallDir "C:\${STR_NAME}\${VERSION}"
CRCCheck on
XPStyle on
;ShowInstDetails show
VIProductVersion "${FILEVERSION}"
VIAddVersionKey /LANG=${LANG_SIMPCHINESE} ProductName "${STR_NAME}"
VIAddVersionKey /LANG=${LANG_SIMPCHINESE} LegalCopyright ""
VIAddVersionKey /LANG=${LANG_SIMPCHINESE} ProductVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_SIMPCHINESE} FileVersion "${FILEVERSION}"
VIAddVersionKey /LANG=${LANG_SIMPCHINESE} FileDescription ""
InstallDirRegKey HKLM "${REGKEY}" Path
;ShowUninstDetails show

# Installer SECTIONS
Section -Main SEC0000
    SetOutPath $INSTDIR
    SetOverwrite on
    File /r /x .svn logo.ico
    ;File /r /x .svn /x src /x doc /x *.doc /x test /x examples ..\additionmod
    
    File /r /x .svn /x src /x doc /x *.doc /x perferences ..\conf
    CreateDirectory $INSTDIR\conf\perferences
    
    File /r /x .svn /x src /x doc /x *.doc /x new_ecc_db /x db ..\contentstore
    CreateDirectory $INSTDIR\contentstore\db
    
    File /r  /x .svn /x src /x doc /x *.doc /x test /x *.erl /x *.txt ..\core
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\data
    CreateDirectory $INSTDIR\error_logs
    
    ;File /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\iconv\*        
 
    SetOutPath $INSTDIR    
    CreateDirectory $INSTDIR\logs
    
    File /r  /x .svn /x src /x doc /x docs /x *.doc /x *.zip /x *.bak /x *.erl /x *.c /x *.sql ..\modules
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\nitrogen 
 
    File /r  /x .svn /x src /x doc /x *.doc /x *.project /x *.java /x *.erl /x *.txt ..\java	
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\nnm_discovery
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\plugin
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\priv
    
    CreateDirectory $INSTDIR\scratch
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\scripts
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\scripts.remote
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\ssh
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\ssl_cert
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\template.os
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.applications
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.health
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.history
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.incident
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.mail
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.mib
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.nnm
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.perfmon
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.post
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.script
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.sets
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.sms
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.snmp
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.solutions
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.sound
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.syslog
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.wsdl
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.wsdl
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\templates.machine
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt /x 64 /x tables64.bat /x createuser64.bat /x createuser32.bat /x ejabberd-2.1.9-windows-installer.exe ..\thirdparty
    
    File /r  /x .svn /x src /x doc /x *.doc /x wmi /x *.ncb /x *.erl /x *.txt /x DataTransfer /x *.doc /x *.pdf /x *.bz2 /x *.c /x *.cs /x windows ..\tools
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\web
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\wwwroot
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\bin
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\binnew
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\datnew
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\lr_extnew
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\dat
	
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\sec
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\lr_ext

    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt ..\ecc8monitor
    
    File /r  /x .svn /x src /x doc /x *.doc /x *.erl /x *.txt /x *.c /x gs-1.5.13 /x wx-0.98.10 ..\erl
    File /r  /x .svn /x src /x doc /x *.doc /x *.java /x *.erl /x *.txt /x *.zip /x *.pdf /x *.dat /x *.log /x *.bak /x *.sql ..\ofbiz
    
    File /x .svn /x *.erl /x *.txt /x *.nsi /x setup /x ecc_service.exe /x compile.bat ..\*.*
    
    # File install\postinstall.bat
    WriteRegStr HKLM "${REGKEY}\Components" Main 1
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR

    ;安装mysql
    DetailPrint "Installing mysql"
    ExecWait '"$INSTDIR\thirdparty\32\mysql5.0_Setup.exe" /q'
    sleep 50000
	
    ;安装VC、VC 9.1运行库
    DetailPrint "Installing VC、VC 9.1"
    ExecWait '"$INSTDIR\thirdparty\vcredist_x86.exe" /q /T:$TEMP'
    ExecWait '"$INSTDIR\thirdparty\vcredist_x86_9.0.exe" /q /S'
    
    File "/oname=$SYSDIR\ssleay32.dll" ssleay32.dll
    File "/oname=$SYSDIR\libeay32.dll" libeay32.dll
    File "/oname=$SYSDIR\libssl32.dll" libssl32.dll
	
    ;安装dotnetfx
    DetailPrint "Installing dotnetfx"
    ExecWait '"$INSTDIR\thirdparty\32\dotnetfx.exe" /q'

    ;安装jre
    DetailPrint "Installing jre"
    ExecWait '"$INSTDIR\thirdparty\32\jre-6u21-windows-i586.exe" /quiet'

    ;安装ejabberd
    ;DetailPrint "Installing ejabberd"
    ;ExecWait '"$INSTDIR\thirdparty\ejabberd-2.1.9-windows-installer.exe" --unattendedmodeui minimal --adminpw admin  --installer-language zh_CN --mode unattended'      
	
    ;安装tomcat
    DetailPrint "Installing tomcat7"
    ReadRegStr $R0 HKLM "SOFTWARE\${STR_NAME} ${VERSION}" "Path"
    ExecWait '"$INSTDIR\thirdparty\apache-tomcat-7.0.0.exe" /quiet /S /D=$R0\tomcat7'
    ReadRegStr $R0 HKLM "SOFTWARE\Apache Software Foundation\Tomcat\7.0" "InstallPath"

    ;修改apache java options
    DetailPrint "Updating apache java options ……"
	
    StrCpy $R1 "-Duser.timezone=GMT+08"
    registry::Write "HKEY_LOCAL_MACHINE\SOFTWARE\Apache Software Foundation\Procrun 2.0\Tomcat7\Parameters\Java" "Options" "$R1" "REG_MULTI_SZ" $var
    StrCpy $R2 "400"
    registry::Write "HKEY_LOCAL_MACHINE\SOFTWARE\Apache Software Foundation\Procrun 2.0\Tomcat7\Parameters\Java" "JvmMx" "$R2" "REG_DWORD" $var
	
    ReadRegStr $R0 HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment" "Path"
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\1.6" "JavaHome"
    ${If} $R0 == ""
	WriteRegExpandStr HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment" "Path" "$R1\bin\;$INSTDIR\erl\bin\;"
    ${else}		
    	WriteRegExpandStr HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment" "Path" "$R0;$R1\bin\;$INSTDIR\erl\bin\;"
    ${EndIf}
    ;将ecc文件拷贝到tomcat的webapps文件中
    ReadRegStr $R0 HKLM "SOFTWARE\Apache Software Foundation\Tomcat\7.0" "InstallPath"

	SetOutPath "$R0\webapps\ecc"
	File /r  /x .svn /x .bak /x .rar "..\..\ecc\*"
	Delete "$R0\webapps\ROOT\index.html"
	SetOutPath "$R0\webapps\ROOT"
	File index.html	
	
    ;修改erl5.8.4\bin\erl.ini和erl5.8.4\erts-5.8.4\bin\erl.ini文件中的路径为 安装目录相对应的路径
    FileOpen $0 "$INSTDIR\erl\bin\erl.ini" w
    FileWrite $0 "[erlang]"
    FileWriteByte $0 "13"
    FileWriteByte $0 "10"
    ${StrRep} $1 "Bindir=$INSTDIR\erl\erts-5.8.4\bin" "\" "\\"
    FileWrite $0 "$1"
    FileWriteByte $0 "13"
    FileWriteByte $0 "10"
    FileWrite $0 "Progname=erl"
    FileWriteByte $0 "13"
    FileWriteByte $0 "10"
    ${StrRep} $1 "Rootdir=$INSTDIR\erl" "\" "\\"
    FileWrite $0 "$1"
    FileClose $0
    
    FileOpen $0 "$INSTDIR\erl\erts-5.8.4\bin\erl.ini" w
    FileWrite $0 "[erlang]"
    FileWriteByte $0 "13"
    FileWriteByte $0 "10"
    ${StrRep} $1 "Bindir=$INSTDIR\erl\erts-5.8.4\bin" "\" "\\"
    FileWrite $0 "$1"
    FileWriteByte $0 "13"
    FileWriteByte $0 "10"
    FileWrite $0 "Progname=erl"
    FileWriteByte $0 "13"
    FileWriteByte $0 "10"
    ${StrRep} $1 "Rootdir=$INSTDIR\erl\erts-5.8.4" "\" "\\"
    FileWrite $0 "$1"
    FileClose $0   

    ;替换安装机器名称
    File "/oname=$INSTDIR\tomcat7\webapps\ecc\WEB-INF\classes\config.properties" config.properties
    File "/oname=$INSTDIR\tomcat7\webapps\ecc\main\index.zul" index_y.zul
    File "/oname=$INSTDIR\ofbiz\framework\entity\config\entityengine.xml" entityengine.xml

    ;${textreplace::ReplaceInFile} $INSTDIR\conf\server.conf $INSTDIR\conf\server.conf "%MASTER_NODE%" $R1 "/S=0" $5
    ;修改ecc\WEB-INF\classes\config.properties配置
    DetailPrint "修改ecc\WEB-INF\classes\config.properties配置"
    ReadRegStr $R1 HKLM "SYSTEM\CurrentControlSet\Services\Tcpip\Parameters" "Hostname"
    ;!insertmacro ReplaceConfig  "$R0\webapps\ecc\WEB-INF\classes\config.properties" "itsm" $R1    
    ${textreplace::ReplaceInFile} $INSTDIR\tomcat7\webapps\ecc\WEB-INF\classes\config.properties $INSTDIR\tomcat7\webapps\ecc\WEB-INF\classes\config.properties "%NODE%" $R1 "/S=0"  $5
    ${textreplace::ReplaceInFile} $INSTDIR\templates.nnm\nnm.conf $INSTDIR\templates.nnm\nnm.conf "%MASTER_NODE%" $R1 "/S=0" $5
    
    ;插入utf-8
    ${textreplace::ReplaceInFile} $INSTDIR\tomcat7\conf\server.xml $INSTDIR\tomcat7\conf\server.xml " port=$\"8080$\"" " URIEncoding=$\"UTF-8$\" port=$\"8080$\"" "/S=0" $5

    ;DetailPrint "配置消息服务工具……"
    ;ExecWait '"$INSTDIR\thirdparty\createuser32.bat"'

    ReadRegStr $R0 HKLM "SOFTWARE\MySQL AB\MySQL Server 5.0" "Location"
    ${If} $R0 == ""
	MessageBox MB_OK "请先安装MySQL！"	
		Quit
    	;sleep 50000
    ${EndIf}
    ;SetOutPath "$R0"
    ;File /r  /x .svn /x .bak /x .rar  "..\thirdparty\my.ini"
    ReadRegStr $R1 HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment" "Path"
    ${If} $R1 == ""
	WriteRegExpandStr HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment" "Path" "$R0bin\;"	
    ${else}		
    	WriteRegExpandStr HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment" "Path" "$R1;$R0bin\;"
    ${EndIf}         
    	
	;add by qicheng.ai
	SendMessage ${HWND_BROADCAST} ${WM_WININICHANGE} 0 "STR:Environment"
		
    DetailPrint "Initial installation, start the service"
    ;创建用户
    DetailPrint "Create user……"
    nsExec::Exec 'net user __siteview_ecc__ /delete'
    nsExec::Exec 'net user __siteview_ecc__ SU20009!@ /add /passwordchg:no   /expires:never /times:all'
    sleep 1000
    nsExec::Exec 'NET LOCALGROUP ADMINISTRATORS __siteview_ecc__ /ADD'
    sleep 1000
    nsExec::Exec '"$INSTDIR\thirdparty\ntrights.exe" -u __siteview_ecc__ +r SeServiceLogonRight'
    sleep 1000
    DetailPrint "Create user end!"
	
    ;安装、启动服务，初始化
    ;安装服务
    DetailPrint "Installing ecc_service service……"
    nsExec::Exec '"$INSTDIR\MonitorContrl.exe" -install .\__siteview_ecc__ SU20009!@'
    sleep 1000
    DetailPrint "Installing ecc_ofbiz service……"
    nsExec::Exec '"$INSTDIR\ofbizService.exe" -install .\__siteview_ecc__ SU20009!@'
    sleep 1000
    DetailPrint "Installing ecc_nnm service……"
    nsExec::Exec '"$INSTDIR\EccNnmService.exe" -install .\__siteview_ecc__ SU20009!@'
    sleep 1000
	
    DetailPrint "Setting Tomcat7 start:auto……"
    nsExec::Exec 'sc config Tomcat7 start= auto'　
	
    DetailPrint "Stopping Tomcat7 service……"
    nsExec::Exec 'net stop Tomcat7'
	
    DetailPrint "Starting Tomcat7 service……"
    nsExec::Exec 'net start Tomcat7'
    sleep 1000
	
    DetailPrint "Setting EccNgService service……"
    nsExec::Exec '"$INSTDIR\thirdparty\sc.exe" config EccNgService obj= .\__siteview_ecc__ password= SU20009!@'
    sleep 1000	    
		
    DetailPrint "数据库文件导入……"
    nsExec::Exec 'net start mysql'
    sleep 1000
    ExecWait '"$INSTDIR\thirdparty\tables32.bat"'           	    
    
    ;启动自带服务
    ;设置依赖关系
    nsExec::Exec 'sc config SiteViewEccNnm depend= SiteViewECC'
    Sleep 2000
    nsExec::Exec 'net start SiteViewECC'
    Sleep 2000
    nsExec::Exec 'net start ofbizService'
    Sleep 2000
    nsExec::Exec 'net start SiteViewEccNnm'
    Sleep 2000

    ;在桌面建立Siteview Ecc 2010快捷方式
    CreateShortCut "$DESKTOP\SiteView ECC 9.1.lnk" "http://localhost:8080/ecc/" "" "$INSTDIR\logo.ico"
    
    ;在开始菜单建Siteview EccNG目录
    SetOutPath $SMPROGRAMS\$StartMenuGroup    
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(LNG_Start) $(^Name) $(LNG_Service).lnk" $INSTDIR\MonitorContrl.exe -start
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(LNG_Stop) $(^Name) $(LNG_Service).lnk" $INSTDIR\MonitorContrl.exe -stop

    DetailPrint "Stopping EccNgService service……"
    nsExec::Exec 'net stop SiteViewEccNnm'
    sleep 1000
    ;nsExec::Exec 'net stop ofbizService'
    ;sleep 1000
    nsExec::Exec 'net stop SiteViewECC'
    sleep 1000
	
    DetailPrint "Starting EccNgService service……"
    nsExec::Exec 'net start mysql'
    sleep 2000
    nsExec::Exec 'net start SiteViewECC'
    sleep 2000
    ;nsExec::Exec 'net start ofbizService'
    ;Sleep 2000
    nsExec::Exec 'net start SiteViewEccNnm'
    Sleep 2000
    
    
SectionEnd

Section -post SEC0001    
    SetOutPath $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe
    SetOutPath $SMPROGRAMS\$StartMenuGroup
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk" $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
    #Delete "$INSTDIR\postinstall.bat" 
    ;ExecShell "open" "http://localhost:8080/ecc/index.jsp"
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
    next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
    done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o -un.Main UNSEC0000
    ;卸载服务
    nsExec::Exec '"$INSTDIR\thirdparty\sc.exe" stop SiteViewEccNnm'
    sleep 1000	
    nsExec::Exec '"$INSTDIR\EccNnmService.exe" -uninstall'
    sleep 1000
    nsExec::Exec '"$INSTDIR\thirdparty\sc.exe" delete SiteViewEccNnm'
    sleep 1000
    nsExec::Exec '"$INSTDIR\thirdparty\sc.exe" stop ofbizService'
    sleep 1000	
    nsExec::Exec '"$INSTDIR\ofbizService.exe" -uninstall'
    sleep 1000
    nsExec::Exec '"$INSTDIR\thirdparty\sc.exe" delete ofbizService'
    sleep 1000
    nsExec::Exec '"$INSTDIR\thirdparty\sc.exe" stop SiteViewECC'
    sleep 1000	
    nsExec::Exec '"$INSTDIR\MonitorContrl.exe" -uninstall'
    sleep 1000
    nsExec::Exec '"$INSTDIR\thirdparty\sc.exe" delete SiteviewECC'
    sleep 1000
    
    ;删除桌面快捷方式
    ExecWait 'net user __siteview_ecc__ /delete'       
    Delete /REBOOTOK "$DESKTOP\SiteView ECC 9.1.lnk"
	
    ;删除开始菜单对应目录
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(LNG_Start) $(^Name) $(LNG_Service).lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(LNG_Stop) $(^Name) $(LNG_Service).lnk"
    RmDir /r /REBOOTOK $INSTDIR
    DeleteRegValue HKLM "${REGKEY}\Components" Main
SectionEnd

Section -un.post UNSEC0001
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk"
    Delete /REBOOTOK $INSTDIR\uninstall.exe
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegKey /IfEmpty HKLM "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
    RmDir /REBOOTOK $SMPROGRAMS\$StartMenuGroup
    RmDir /REBOOTOK $INSTDIR
    ;MessageBox MB_YESNO|MB_ICONQUESTION "Do you wish to reboot the system?" IDNO +2
    ;Reboot
    ReadRegStr $R0 HKLM "SOFTWARE\Apache Software Foundation\Tomcat\7.0" "InstallPath"
    ExecWait '"$R0\Uninstall.exe"/quiet /S'
SectionEnd

#Installer functions
Function .onInit
    InitPluginsDir
    StrCpy $StartMenuGroup "$(^Name)"
    Push $R1
    File /oname=$PLUGINSDIR\spltmp.BMP logo.bmp
    advsplash::show 1000 600 400 -1 $PLUGINSDIR\spltmp
    Pop $R1
    Pop $R1
    !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

# Uninstaller functions
Function un.onInit
	FindProcDLL::FindProc "epmd.exe" 
	Pop $R0 
	IntCmp $R0 1 0 no_run 
	;MessageBox MB_OKCANCEL "delete?" IDOK OK IDCANCEL Cancel
	;OK:
	KillProcDLL::KillProc "epmd.exe"
	Sleep 1000
	;Goto no_run
	;Cancel:
	no_run:
	
    	ReadRegStr $INSTDIR HKLM "${REGKEY}" Path
    	StrCpy $StartMenuGroup "$(^Name)"
    	!insertmacro SELECT_UNSECTION Main ${UNSEC0000}
FunctionEnd

Function Info
	;ExecShell "open" "$DESKTOP\SiteView ECC 9.1.lnk"	
Functionend

Function .onGUIEnd
        ExecShell "open" "$DESKTOP\SiteView ECC 9.1.lnk" 
  	;ExecShell "open" "$DESKTOP\SiteView ECC 9.0.PRODUCT_WEB_SITE"
FunctionEnd

# 替换文件中的字符串
Function AdvReplaceInFile
    Exch $0  ;file to replace in
    Exch
    Exch $1  ;number to replace after
    Exch
    Exch 2
    Exch $2  ;replace and onwards
    Exch 2
    Exch 3
    Exch $3  ;replace with
    Exch 3
    Exch 4
    Exch $4  ;to replace
    Exch 4
    Push $5  ;minus count
    Push $6  ;universal
    Push $7  ;end string
    Push $8  ;left string
    Push $9  ;right string
    Push $R0 ;file1
    Push $R1 ;file2
    Push $R2 ;read
    Push $R3 ;universal
    Push $R4 ;count (onwards)
    Push $R5 ;count (after)
    Push $R6 ;temp file name
    GetTempFileName $R6
    FileOpen $R1 $0 r   ;file to search in
    FileOpen $R0 $R6 w  ;temp file
    StrLen $R3 $4
    StrCpy $R4 -1
    StrCpy $R5 -1
    loop_read:
    ClearErrors
    FileRead $R1 $R2 ;read line
    IfErrors exit
    StrCpy $5 0
    StrCpy $7 $R2
    loop_filter:
    IntOp $5 $5 - 1
    StrCpy $6 $7 $R3 $5 ;search
    StrCmp $6 "" file_write2
    StrCmp $6 $4 0 loop_filter
    StrCpy $8 $7 $5     ;left part
    IntOp $6 $5 + $R3
    IntCmp $6 0 is0 not0
    is0:
    StrCpy $9 ""
    Goto done
    not0:
    StrCpy $9 $7 "" $6  ;right part
    done:
    StrCpy $7 $8$3$9    ;re-join
    IntOp $R4 $R4 + 1
    StrCmp $2 all file_write1
    StrCmp $R4 $2 0 file_write2
    IntOp $R4 $R4 - 1
    IntOp $R5 $R5 + 1
    StrCmp $1 all file_write1
    StrCmp $R5 $1 0 file_write1
    IntOp $R5 $R5 - 1
    Goto file_write2
    file_write1:
    FileWrite $R0 $7  ;write modified line
    Goto loop_read
    file_write2:
    FileWrite $R0 $R2 ;write unmodified line
    Goto loop_read
    exit:
    FileClose $R0
    FileClose $R1
    SetDetailsPrint none
    Delete $0
    Rename $R6 $0
    Delete $R6
    SetDetailsPrint both
    Pop $R6
    Pop $R5
    Pop $R4
    Pop $R3
    Pop $R2
    Pop $R1
    Pop $R0
    Pop $9
    Pop $8
    Pop $7
    Pop $6
    Pop $5
    Pop $0
    Pop $1
    Pop $2
    Pop $3
    Pop $4
FunctionEnd

function pcap_install
    IfFileExists "$SYSDIR\drivers\npf.sys" 0 not_exists
        push "$SYSDIR\drivers\npf.sys"
        call getversion
        pop $R0
        IntCmp $R0 41 0 lt41 0
                  goto end
             lt41:
                  Messagebox MB_OK "$(PCAP_TIPINFO)"
                  SetErrorLevel 2
        
        goto end
	not_exists:
            call pcap_copyfiles
	end:
functionend

function pcap_copyfiles
    	File "/oname=$SYSDIR\wpcap.dll" wpcap.dll
    	File "/oname=$SYSDIR\Packet.dll" Packet.dll
    	File "/oname=$SYSDIR\drivers\npf.sys" npf.sys
functionend

function getversion
	pop $0
	GetDllversion $0 $R0 $R1
	IntOp $R2 $R0 / 0x00010000
	IntOp $R3 $R0 & 0x0000FFFF
	IntOp $R4 $R1 / 0x00010000
	IntOp $R5 $R1 & 0x0000FFFF
	IntOp $R2 $R2 * 10
	IntOp $R6 $R2 + $R3
	push $R6
functionend

# Installer Language Strings
# TODO Update the Language Strings with the appropriate translations.

LangString ^UninstallLink ${LANG_SIMPCHINESE} "卸载 $(^Name)"
LangString ^UninstallLink ${LANG_ENGLISH} "Uninstall $(^Name)"

LicenseLangString LNG_License ${LANG_ENGLISH} "siteviewlicense_english.rtf"
LicenseLangString LNG_License ${LANG_SIMPCHINESE} "siteviewlicense.rtf"

LangString LNG_Start ${LANG_ENGLISH} "Start"
LangString LNG_Start ${LANG_SIMPCHINESE} "启动"

LangString LNG_Stop ${LANG_ENGLISH} "Stop"
LangString LNG_Stop ${LANG_SIMPCHINESE} "停止"

LangString LNG_Service ${LANG_ENGLISH} "Service"
LangString LNG_Service ${LANG_SIMPCHINESE} "服务"

LangString LNG_Open ${LANG_ENGLISH} "Open page SiteView ECC 9.1"
LangString LNG_Open ${LANG_SIMPCHINESE} "打开网页“SiteView ECC 9.1”"

LangString PCAP_TIPINFO ${LANG_ENGLISH} "you have installed low version winpcap,please uninstall it first."
LangString PCAP_TIPINFO ${LANG_SIMPCHINESE} "您已经安装了低版本的winpcap，请先卸载。"
