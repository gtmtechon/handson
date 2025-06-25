#!/bin/bash
$date=$(date +%Y%m%d-%H%M%S)
ps -ef | grep -v grep >> /tmp/pidlist-$date.txt
exit 0
# 이 스크립트는 현재 실행 중인 프로세스의 PID 목록을 /tmp 디렉토리에
# 날짜와 시간을 포함한 파일로 저장합니다.
# grep -v grep 명령은 grep 명령 자체를 제외하여 PID 목록만을
# 출력합니다. 이 파일은 나중에 다른 스크립트나 작업에서 참조할 수 있습니다.
