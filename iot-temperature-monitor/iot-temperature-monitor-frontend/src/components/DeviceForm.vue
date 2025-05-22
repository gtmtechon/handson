<template>
  <form @submit.prevent="handleSubmit" class="device-form">
    <div class="form-group">
      <label for="deviceId">장비 ID:</label>
      <input type="text" id="deviceId" v-model="device.deviceId" readonly required />
    </div>
    <div class="form-group">
      <label for="deviceName">장비 이름:</label>
      <input type="text" id="deviceName" v-model="device.deviceName" :readonly="isSubmitting" required />
    </div>
    <div class="form-group">
      <label for="location">위치:</label>
      <input type="text" id="location" v-model="device.location" :readonly="isSubmitting" />
    </div>
    <button type="submit" class="button primary" :disabled="isSubmitting">
      {{ isSubmitting ? (isEditMode ? '수정 중...' : '등록 중...') : (isEditMode ? '수정 완료' : '등록') }}
    </button>
    <router-link to="/" class="button secondary" :class="{ 'disabled-link': isSubmitting }">취소</router-link>
  </form>
</template>

<script>
export default {
  name: 'DeviceForm',
  props: {
    initialDevice: {
      type: Object,
      default: () => ({
        deviceId: '',
        deviceName: '',
        location: '',
      }),
    },
    isEditMode: {
      type: Boolean,
      default: false,
    },
    isSubmitting: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      // initialDevice prop을 받아와 device 데이터 초기화
      // watch 옵션을 사용하여 initialDevice 변경 시 device 업데이트
      device: { ...this.initialDevice },
    };
  },
  watch: {
    initialDevice: {
      handler(newVal) {
        // prop이 변경될 때마다 내부 device 상태 업데이트
        this.device = { ...newVal };
      },
      deep: true,
      immediate: true, // 컴포넌트 마운트 시 즉시 실행하여 초기값 설정
    },
  },
  methods: {
    handleSubmit() {
      if (!this.isSubmitting) {
        this.$emit('submit', this.device); // 부모 컴포넌트로 현재 device 상태 emit
      }
    },
  },
};
</script>

<style scoped>
/* 기존 스타일 유지 */
.device-form {
  background-color: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  margin: 40px auto;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
  color: #333;
}

input[type="text"] {
  width: calc(100% - 24px); /* Padding 고려 */
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1em;
}

input[type="text"]:focus {
  border-color: #007bff;
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
}

input[readonly] {
  background-color: #e9ecef; /* 읽기 전용 필드의 배경색 변경 */
  cursor: not-allowed; /* 마우스 커서 변경 */
}

.button {
  margin-right: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1em;
  text-decoration: none; /* for router-link */
  display: inline-block;
  text-align: center;
}

.button.primary {
  background-color: #007bff;
  color: white;
}

.button.primary:hover:not(:disabled) {
  background-color: #0056b3;
}

.button.secondary {
  background-color: #6c757d;
  color: white;
}

.button.secondary:hover:not(:disabled) {
  background-color: #5a6268;
}

.button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
  opacity: 0.7;
}

.disabled-link {
  pointer-events: none;
  opacity: 0.7;
  cursor: not-allowed;
}
</style>