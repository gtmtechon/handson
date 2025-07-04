## package install
`$` sudo apt update  
`$` sudo apt upgrade -y  
`$` sudo ufw allow 8080/tcp  
`$` sudo apt install nginx -y   


## https를 위한 TLS인증서 생성
`$` sudo mkdir -p /etc/nginx/ssl

### private key
`$` sudo openssl genrsa -out /etc/nginx/ssl/nginx.key 2048



### csr,Certificate Signing Request; 인증서에 포함정보, 인증기관(CA)이 인증서를 서명하거나, 자체 서명 인증서를 만들 때 사용
`$` sudo openssl req -new -key /etc/nginx/ssl/nginx.key -out /etc/nginx/ssl/nginx.csr

Country Name (2 letter code) [AU]:KR  
State or Province Name (full name) [Some-State]:Seoul  
Locality Name (eg, city) []:Seoul  
Organization Name (eg, company) [Internet Widgits Pty Ltd]:MyCompany  
Organizational Unit Name (eg, section) []:IT  
Common Name (e.g. server FQDN or YOUR name) []:iotmonproxy.koreacentral.cloudapp.azure.com  <-- **  이 부분이 중요! Nginx가 서비스할 도메인 이름을 입력합니다.**
Email Address []:admin@mycompany.com  

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:  <-- 그냥 Enter (비워둠)
An optional company name []: <-- 그냥 Enter (비워둠)



### self signed certification
`$` sudo openssl x509 -req -days 365 -in /etc/nginx/ssl/nginx.csr -signkey /etc/nginx/ssl/nginx.key -out /etc/nginx/ssl/nginx.crt



## configuration
### nginx TLS 설정 파일

아래 링크에서 예시 설정 파일을 참고하세요:  
[nginx-tls.conf](https://raw.githubusercontent.com/gtmtechon/handson/refs/heads/main/docs/nginx/nginx-tls.conf)


## reload & test  
`$` sudo nginx -t  
`$` sudo systemctl restart nginx
(or sudo systemctl reload nginx)  
`$` sudo systemctl status nginx  

`$` curl http://localhost:8080/api/devices/selectAll  

### 설정 검증 및 재시작  
`$` sudo nginx -t  
`$` sudo systemctl reload nginx  
