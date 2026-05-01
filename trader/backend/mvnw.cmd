@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM Begin all REM://
@echo off
@REM set title of command window
title %0
@REM enable echoing by setting MAVEN_BATCH_ECHO to 'on'
@if "%MAVEN_BATCH_ECHO%"=="on" echo %MAVEN_BATCH_ECHO%

@REM set %HOME% to equivalent of $HOME
if "%HOME%"=="" (set "HOME=%HOMEDRIVE%%HOMEPATH%")

set ERROR_CODE=0

@REM To isolate internal variables from possible post scripts, we use another setlocal
@setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%"=="" goto OkJHome

echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto init

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:init

@REM Find the project base dir, i.e. the directory that contains the folder ".mvn".
@REM Fallback to current working directory if not found.

set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir

set EXEC_DIR=%CD%
set WDIR=%EXEC_DIR%
:findBaseDir
IF EXIST "%WDIR%"\.mvn goto baseDirFound
if "%WDIR%"=="%WDIR:~0,2%" goto baseDirNotFound
set "WDIR=%WDIR:~0,-1%"
set "WDIR=%WDIR%\.."
goto findBaseDir

:baseDirFound
set MAVEN_PROJECTBASEDIR=%WDIR%
goto endDetectBaseDir

:baseDirNotFound
set MAVEN_PROJECTBASEDIR=%EXEC_DIR%
goto endDetectBaseDir

:endDetectBaseDir

IF NOT EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" goto MWrapperNotFound

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"

"%JAVA_HOME%\bin\java.exe" ^
  %MAVEN_OPTS% ^
  %MAVEN_DEBUG_OPTS% ^
  -classpath %WRAPPER_JAR% ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  org.apache.maven.wrapper.MavenWrapperMain %*
if ERRORLEVEL 1 goto error
goto end

:MWrapperNotFound
@REM Fallback: Use the wrapper properties to download Maven directly
set WRAPPER_PROPERTIES="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties"
if not exist %WRAPPER_PROPERTIES% (
    echo Error: Could not find maven-wrapper.properties >&2
    goto error
)

@REM Use PowerShell to download and run Maven
for /f "tokens=2 delims==" %%a in ('findstr "distributionUrl" %WRAPPER_PROPERTIES%') do set DOWNLOAD_URL=%%a
set "DOWNLOAD_URL=%DOWNLOAD_URL: =%"

set "MAVEN_HOME=%HOME%\.m2\wrapper\dists"
if not exist "%MAVEN_HOME%" mkdir "%MAVEN_HOME%"

echo Downloading Maven from %DOWNLOAD_URL%...
powershell -Command "& { $url='%DOWNLOAD_URL%'; $out='%MAVEN_HOME%\maven.zip'; (New-Object Net.WebClient).DownloadFile($url, $out); Expand-Archive -Path $out -DestinationPath '%MAVEN_HOME%' -Force; Remove-Item $out }"

@REM Find the extracted Maven directory
for /d %%i in ("%MAVEN_HOME%\apache-maven-*") do set "MAVEN_EXTRACTED=%%i"

"%MAVEN_EXTRACTED%\bin\mvn.cmd" %*
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

cmd /C exit /B %ERROR_CODE%
