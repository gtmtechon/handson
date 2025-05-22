<template>
    <div class="page-container">
      <h1>등록된 IoT 장비</h1>
      <router-link to="/devices/register" class="button primary">새 장비 등록</router-link>
  
      <div v-if="loading" class="loading-message">장비 목록을 불러오는 중...</div>
      <div v-else-if="error" class="error-message">{{ error }}</div>
      <table v-else-if="devices.length > 0">
        <thead>
          <tr>
            <th>장비 ID</th>
            <th>장비 이름</th>
            <th>위치</th>
            <th>등록일</th>
            <th>액션</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="device in devices" :key="device.deviceId">
            <td>{{ device.deviceId }}</td>
            <td>{{ device.deviceName }}</td>
            <td>{{ device.location }}</td>
            <td>{{ new Date(device.registeredAt).toLocaleString() }}</td>
            <td class="actions">
              <router-link :to="`/devices/edit/${device.deviceId}`" class="button">수정</router-link>
              <button @click="confirmDelete(device.deviceId)" class="button danger">삭제</button>
              <router-link :to="`/devices/monitor/${device.deviceId}`" class="button primary">모니터링</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="no-data-message">등록된 장비가 없습니다.</div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name: 'HomePage',
    data() {
      return {
        devices: [],
        loading: true,
        error: null,
      };
    },
    mounted() {
      this.fetchDevices();
    },
    methods: {
      async fetchDevices() {
        this.loading = true;
        this.error = null;
        try {
          const response = await axios.get(`${process.env.VUE_APP_BACKEND_API_URL}/devices`);
          this.devices = response.data;
        } catch (err) {
          console.error('Error fetching devices:', err);
          this.error = '장비를 불러오는 데 실패했습니다.';
        } finally {
          this.loading = false;
        }
      },
      async confirmDelete(deviceId) {
        if (confirm('정말로 이 장비를 삭제하시겠습니까? 관련 온도 데이터도 삭제됩니다.')) {
          try {
            await axios.delete(`${process.env.VUE_APP_BACKEND_API_URL}/devices/${deviceId}`);
            this.fetchDevices(); // 삭제 후 목록 새로고침
          } catch (err) {
            console.error('Error deleting device:', err);
            alert('장비 삭제에 실패했습니다.');
          }
        }
      },
    },
  };
  </script>
  
  <style scoped>
  /* Scoped styles for HomePage.vue */
  .page-container {
    padding: 20px;
  }
  
  h1 {
    color: #0056b3;
    margin-bottom: 20px;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
  }
  
  th, td {
    border: 1px solid #ddd;
    padding: 12px;
    text-align: left;
  }
  
  th {
    background-color: #007bff;
    color: white;
  }
  
  tr:nth-child(even) {
    background-color: #f2f2f2;
  }
  
  .actions .button {
    margin-right: 8px;
  }
  
  .loading-message, .error-message, .no-data-message {
    text-align: center;
    padding: 20px;
    font-size: 1.1em;
  }
  .error-message {
    color: red;
  }
  </style>