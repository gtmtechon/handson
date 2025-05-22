import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import './assets/style.css'; // 전역 스타일 임포트

createApp(App).use(router).mount('#app');