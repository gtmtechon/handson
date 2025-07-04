`$` sudo apt update  
`$` sudo apt upgrade -y  

`$` sudo ufw allow 8080/tcp  

`$` sudo apt install nginx -y  
`$` sudo mv /etc/nginx/sites-available/default /etc/nginx/sites-available/default.bak  
`$` $sudo rm /etc/nginx/sites-enabled/default  


## configuration
`$` sudo ln -s /etc/nginx/sites-available/my_proxy.conf /etc/nginx/sites-enabled/  
 include /etc/nginx/sites-enabled/*;  # 이 라인을 주석 처리  
include /etc/nginx/sites-enabled/my_proxy.conf; # 우리가 만든 파일만 명시적으로 include  

`$` sudo nginx -t  
`$` sudo systemctl restart nginx  
`$` sudo systemctl status nginx  

## test  
$curl http://localhost:8080/api/devices/selectAll  

### 설정 검증 및 재시작  
`$` sudo nginx -t  
`$` sudo systemctl reload nginx  
