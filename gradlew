#!/usr/bin/env sh
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
APP_NAME="Gradle"
APP_BASE_NAME="gradle"

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  LS=`ls -ld "$PRG"`
  LINK=`expr "$LS" : '.*-> \(.*\)$'`
  if expr "$LINK" : '/.*' > /dev/null; then
    PRG="$LINK"
  else
    PRG=`dirname "$PRG"`/"$LINK"
  fi
done

APP_HOME=`dirname "$PRG"`
APP_HOME=`cd "$APP_HOME" && pwd`

# Add default JVM options if not already specified
if [ -z "$JAVA_OPTS" ]; then
  JAVA_OPTS="$DEFAULT_JVM_OPTS"
fi

# Determine the Java command to run.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/bin/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/bin/java"
    elif [ -x "$JAVA_HOME/bin/java" ] ; then
        JAVACMD="$JAVA_HOME/bin/java"
    fi
fi

if [ ! -x "$JAVACMD" ] ; then
  for CANDIDATE in `ls -r /usr/java/jdk* /usr/java/jre* /usr/lib/jvm/java* /usr/bin/java 2>/dev/null` ; do
    if [ -x "$CANDIDATE" ] ; then
      JAVACMD="$CANDIDATE"
      break
    fi
  done
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

# Determine the Gradle JAR file to use.
GRADLE_JAR_PATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

exec "$JAVACMD" $JAVA_OPTS -classpath "$GRADLE_JAR_PATH" org.gradle.wrapper.GradleWrapperMain "$@"
