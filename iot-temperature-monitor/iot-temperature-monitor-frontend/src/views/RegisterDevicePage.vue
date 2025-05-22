<template>
  <div class="page-container">
    <h1>새 IoT 장비 등록</h1>
    <DeviceForm
      @submit="registerDevice"
      :isSubmitting="isRegistering"
      :initialDevice="newDeviceData"
    />
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
      newDeviceData: { // DeviceForm으로 전달할 초기 장비 데이터
        deviceId: '', // 여기에 자동 생성된 ID가 들어갈 것입니다.
        deviceName: '',
        location: '',
      },
    };
  },
  mounted() {
    // 페이지 로드 시 새로운 deviceId 자동 생성
    this.generateNewDeviceId();
  },
  methods: {
    generateNewDeviceId() {
      // 랜덤 16진수 16자리 문자열 생성
      // 이 방법은 완전히 고유함을 보장하지 않지만, 간단한 예시에는 적합합니다.
      // 실제 환경에서는 UUID 등을 사용하는 것이 더 좋습니다.
      const uuid = ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
      );
      // UUID는 일반적으로 하이픈을 포함하므로, 16자리 헥사 문자열로 변환 (필요시)
      // 여기서는 하이픈 없이 32자리의 헥사 문자열이 생성됩니다.
      // 요구사항에 맞춰 16자리로 자르거나 원하는 형태로 변환할 수 있습니다.
      // 현재는 UUID (32자리 헥사) 전체를 deviceId로 사용합니다.
      // 만약 정확히 '16자리 헥사'를 원한다면 다음 줄을 수정하세요.
      // 예: uuid.substring(0, 16);
      this.newDeviceData.deviceId = uuid.replace(/-/g, '').substring(0, 16); // 하이픈 제거 후 16자리로 자르기
      console.log('Generated new Device ID:', this.newDeviceData.deviceId);
    },
    async registerDevice(deviceData) {
      this.error = null;
      this.isRegistering = true; // 등록 시작, 버튼 비활성화

      // DeviceForm에서 받은 deviceData는 이미 생성된 deviceId를 포함합니다.
      // 하지만 혹시 모를 경우를 대비해 여기서 다시 한번 확인/할당 가능
      if (!deviceData.deviceId) {
        deviceData.deviceId = this.newDeviceData.deviceId;
      }

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
          // 서버에서 에러 응답이 온 경우 (예: 409 Conflict)
          this.error = `장비 등록에 실패했습니다: ${err.response.data.message || (typeof err.response.data === 'string' ? err.response.data : JSON.stringify(err.response.data))}`;
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