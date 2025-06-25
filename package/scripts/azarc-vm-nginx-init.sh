#!/bin/bash

# 스크립트 실행 시 오류 발생하면 즉시 종료
set -e

echo "---"
echo "Nginx 자동 설치를 시작합니다."
echo "---"

# 패키지 목록 업데이트
echo "패키지 목록을 업데이트 중..."
sudo apt update -y
echo "패키지 목록 업데이트 완료."
echo "---"

# Nginx 설치
# DEBIAN_FRONTEND=noninteractive: apt가 사용자에게 질문하지 않도록 설정
# -y 옵션: 모든 프롬프트에 자동으로 'yes'로 응답
echo "Nginx를 설치 중..."
DEBIAN_FRONTEND=noninteractive sudo apt install -y nginx
echo "Nginx 설치 완료."
echo "---"

# Nginx 서비스 상태 확인
echo "Nginx 서비스 상태를 확인 중..."
sudo systemctl status nginx --no-pager
echo "---"

# Nginx 방화벽 설정 (선택 사항이지만 권장)
echo "UFW(방화벽)에서 Nginx 관련 포트(80, 443)를 허용 중..."
sudo ufw allow 'Nginx Full'
sudo ufw enable
sudo ufw status verbose | grep "Nginx Full"
echo "방화벽 설정 완료."
echo "---"

echo "Nginx 설치 및 설정이 성공적으로 완료되었습니다!"
echo "웹 브라우저에서 서버의 IP 주소로 접속하여 Nginx 기본 페이지를 확인할 수 있습니다."