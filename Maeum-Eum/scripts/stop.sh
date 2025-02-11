#!/usr/bin/env bash

PROJECT_ROOT=/home/ec2-user/Maeum-Eum
JAR="$PROJECT_ROOT/application.jar"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

NOW=$(date +%c)

# 현재 구동 중인 애플리케이션 pid 확인
PID=$(pgrep -f $JAR | head -n 1)

# 프로세스가 켜져 있으면 종료
if [ -z "$PID" ]; then
  echo "$NOW > 현재 실행중인 애플리케이션이 없습니다" >> $DEPLOY_LOG
else
  echo "$NOW > 실행중인 $CURRENT_PID 애플리케이션 종료 " >> $DEPLOY_LOG
  # -15 : 정상 종료
  sudo kill -15 "$PID"
fi