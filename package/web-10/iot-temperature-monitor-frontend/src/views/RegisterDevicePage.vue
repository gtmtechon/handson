<template>
  <div class="page-container">
    <h1>새 IoT 장비 등록</h1>
    <DeviceForm @submit="registerDevice" :isSubmitting="isRegistering" />
    <p v-if="error" class="error-message">{{ error }}</p>
  </div>
</template>

<script>
import axios from 'axios';
import DeviceForm from '../components/DeviceForm.vue';

export default {
  name: 'RegisterDevicePage',
  components: {
    DeviceForm,
  },
  data() {
    return {
      error: null,
      isRegistering: false, // 등록 요청 진행 상태
    };
  },
  methods: {
    async registerDevice(deviceData) {
      this.error = null;
      this.isRegistering = true; // 등록 시작, 버튼 비활성화

      try {
        await axios.post(
          `${process.env.VUE_APP_BACKEND_API_URL}/devices`,
          deviceData,
          {
            timeout: 5000 // 5초 타임아웃 설정 (5000 milliseconds)
          }
        );
        alert('장비가 성공적으로 등록되었습니다.');
        this.$router.push('/'); // 등록 후 목록 페이지로 이동
      } catch (err) {
        console.error('Error registering device:', err);

        // Axios Timeout Error 처리
        if (axios.isCancel(err)) {
          this.error = '장비 등록 요청이 취소되었습니다.'; // 요청이 수동으로 취소된 경우
        } else if (err.code === 'ECONNABORTED' && err.message.includes('timeout')) {
          this.error = '서버 응답이 없습니다. 서버가 실행 중인지 확인해주세요.';
        } else if (err.response && err.response.data) {
          // 서버에서 에러 응답이 온 경우
          this.error = `장비 등록에 실패했습니다: ${err.response.data.message || err.response.data}`;
        } else {
          // 기타 네트워크 오류 등
          this.error = '장비 등록에 실패했습니다. 네트워크 연결을 확인하거나 서버를 확인해주세요.';
        }
      } finally {
        this.isRegistering = false; // 등록 완료 (성공 또는 실패), 버튼 활성화
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
.error-message {
  color: red;
  margin-top: 15px;
}
</style>