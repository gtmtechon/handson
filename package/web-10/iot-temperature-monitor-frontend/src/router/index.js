import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../views/HomePage.vue';
import RegisterDevicePage from '../views/RegisterDevicePage.vue';
import EditDevicePage from '../views/EditDevicePage.vue';
import MonitorDevicePage from '../views/MonitorDevicePage.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
  },
  {
    path: '/devices/register',
    name: 'RegisterDevice',
    component: RegisterDevicePage,
  },
  {
    path: '/devices/edit/:deviceId',
    name: 'EditDevice',
    component: EditDevicePage,
    props: true, // URL 파라미터를 컴포넌트 props로 전달
  },
  {
    path: '/devices/monitor/:deviceId',
    name: 'MonitorDevice',
    component: MonitorDevicePage,
    props: true, // URL 파라미터를 컴포넌트 props로 전달
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;