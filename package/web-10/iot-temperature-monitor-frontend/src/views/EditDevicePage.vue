<template>
    <div class="page-container">
      <h1>장비 수정: {{ device ? device.deviceName : '로딩 중...' }}</h1>
      <DeviceForm v-if="device" :initialDevice="device" :isEditMode="true" @submit="updateDevice" />
      <div v-else-if="loading" class="loading-message">장비 정보를 불러오는 중...</div>
      <div v-else-if="error" class="error-message">{{ error }}</div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import DeviceForm from '../components/DeviceForm.vue';
  
  export default {
    name: 'EditDevicePage',
    components: {
      DeviceForm,
    },
    props: ['deviceId'], // 라우터에서 전달받은 deviceId
    data() {
      return {
        device: null,
        loading: true,
        error: null,
      };
    },
    mounted() {
      this.fetchDevice();
    },
    methods: {
      async fetchDevice() {
        this.loading = true;
        this.error = null;
        try {
          const response = await axios.get(`${process.env.VUE_APP_BACKEND_API_URL}/devices/${this.deviceId}`);
          this.device = response.data;
        } catch (err) {
          console.error('Error fetching device for edit:', err);
          this.error = '장비 정보를 불러오는 데 실패했습니다.';
          this.device = null; // 에러 발생 시 폼 숨김
        } finally {
          this.loading = false;
        }
      },
      async updateDevice(deviceData) {
        this.error = null;
        try {
          await axios.put(`${process.env.VUE_APP_BACKEND_API_URL}/devices/${this.deviceId}`, deviceData);
          alert('장비가 성공적으로 수정되었습니다.');
          this.$router.push('/'); // 수정 후 목록 페이지로 이동
        } catch (err) {
          console.error('Error updating device:', err);
          if (err.response && err.response.data) {
            this.error = `장비 수정에 실패했습니다: ${err.response.data.message || err.response.data}`;
          } else {
            this.error = '장비 수정에 실패했습니다. 서버를 확인해주세요.';
          }
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .page-container {
    padding: 20px;
    text-align: center;
  }
  h1 {
    color: #0056b3;
  }
  .loading-message, .error-message {
    margin-top: 20px;
    font-size: 1.1em;
  }
  .error-message {
    color: red;
  }
  </style>