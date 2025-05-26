// app.js
const http = require('http'); // HTTP 모듈을 가져옵니다.
const fs = require('fs');     // 파일 시스템 모듈을 가져옵니다.
const path = require('path');   // 경로 모듈을 가져옵니다.

// Azure App Service는 PORT 환경 변수를 통해 포트를 제공합니다.
// 환경 변수가 없으면 기본값으로 8080을 사용합니다.
const port = process.env.PORT || 8080;
const hostname = '0.0.0.0'; // 모든 네트워크 인터페이스에서 요청을 수신하도록 설정

const server = http.createServer((req, res) => {
  if (req.url === '/') {
    const filePath = path.join(__dirname, 'index.html');

    fs.readFile(filePath, (err, data) => {
      if (err) {
        console.error('Error reading index.html:', err); // 에러 로깅
        res.statusCode = 500;
        res.setHeader('Content-Type', 'text/plain');
        res.end('Internal Server Error');
        return;
      }

      res.statusCode = 200;
      res.setHeader('Content-Type', 'text/html');
      res.end(data);
    });
  } else {
    res.statusCode = 404;
    res.setHeader('Content-Type', 'text/plain');
    res.end('404 Not Found');
  }
});

server.listen(port, hostname, () => {
  console.log(`Server running on http://${hostname}:${port}/`);
});