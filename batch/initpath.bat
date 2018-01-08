@echo off

rem initpath.bat

echo Setting environnement variables for J2SDK1.4.0

set jdkhome=c:\j2sdk1.4.0
set path=c:\winnt\system32;c:\winnt;%jdkhome%\bin

set basehome=c:\data\usr\hr\java\marray
set classeshome=%basehome%\classes
set sourceshome=%basehome%\src
set classpath=.;%classeshome%

set compiler=javac

rem compilation option (-g, -O, ...)
set option=

set interpreter=java -mx128m

set application=Application
