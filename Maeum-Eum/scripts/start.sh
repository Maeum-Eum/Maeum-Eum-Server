#!/usr/bin/env bash

PROJECT_ROOT=/home/ec2-user/Maeum-Eum
JAR="$PROJECT_ROOT/application.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

NOW=$(date +%c)

# build 파일 복사
echo "[$NOW] > $JAR 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/Maeum-Eum-0.0.1-SNAPSHOT.jar $JAR

# jar 파일 실행
echo "[$NOW] > $JAR 파일 실행" >> $DEPLOY_LOG
nohup sudo java -jar $JAR > $APP_LOG 2> $ERROR_LOG &

PID=$(pgrep -f $JAR)
echo "$NOW > 현재 실행된 프로세스 아이디 $PID 입니다." >> $DEPLOY_LOG