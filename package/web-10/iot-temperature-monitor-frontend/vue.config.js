const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000, // 원하는 포트 번호로 변경 (예: 8081, 3000, 4000 등)
    // host: '0.0.0.0' // 특정 IP에서 접근 가능하게 하려면 주석 해제 (기본은 localhost)
  }
});
// const { defineConfig } = require('@vue/cli-service')