@REM
@REM Copyright 2010 the original author or authors.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM      http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

@if "%DEBUG%" == "" @set DEBUG=0

@if "%DEBUG%" == "1" @echo on

@set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

@REM Determine the Java command to run.
@if defined JAVA_HOME goto findJavaFromJavaHome

@set JAVA_CMD=java
@for %%i in (java.exe) do @if "%%~$PATH:i" NEQ "" @set JAVA_CMD=%%~$PATH:i

@if not "%JAVA_CMD%" == "java" goto runJava

:findJavaFromJavaHome
@if exist "%JAVA_HOME%\jre\bin\java.exe" @set JAVA_CMD="%JAVA_HOME%\jre\bin\java.exe"&goto runJava
@if exist "%JAVA_HOME%\bin\java.exe" @set JAVA_CMD="%JAVA_HOME%\bin\java.exe"&goto runJava
@echo.
@echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
@echo.
@echo Please set the JAVA_HOME variable in your environment to match the location of your Java installation.
@goto fail

:runJava
@set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"
@if not defined JAVA_OPTS @set JAVA_OPTS=%DEFAULT_JVM_OPTS%
"%JAVA_CMD%" %JAVA_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
@if %ERRORLEVEL% NEQ 0 goto fail
@goto end

:fail
@echo.
@echo BUILD FAILED
@goto end

:end
@exit /b %ERRORLEVEL%
