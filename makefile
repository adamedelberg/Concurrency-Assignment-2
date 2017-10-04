{\rtf1\ansi\ansicpg1252\cocoartf1347\cocoasubrtf570
{\fonttbl\f0\fmodern\fcharset0 Courier;}
{\colortbl;\red255\green255\blue255;\red0\green0\blue0;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\deftab720
\pard\pardeftab720

\f0\fs24 \cf2 \expnd0\expndtw0\kerning0
\outl0\strokewidth0 \strokec2 JFLAGS = -g\
JC = javac\
.SUFFIXES: .java .class\
.java.class:\
        $(JC) $(JFLAGS) $*.java\
\
CLASSES = \\\
        Driver.java \\\
        Parallel.java \\\
        Serial.java \\\
        Performance.java \
\
default: classes\
\
classes: $(CLASSES:.java=.class)\
\
clean:\
        $(RM) *.class\
}