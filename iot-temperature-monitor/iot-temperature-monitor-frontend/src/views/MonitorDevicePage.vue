<template>
  <div class="page-container">
    <h1>{{ device ? device.deviceName : '로딩 중...' }} 온도 모니터링</h1>
    <p v-if="device">장비 ID: {{ device.deviceId }}</p>
    <p v-if="device">위치: {{ device.location }}</p>

    <div class="controls">
      <label for="timeRange">조회 기간:</label>
      <select id="timeRange" v-model="selectedTimeRange" @change="fetchTemperatures">
        <option value="60">최근 1시간 (60분)</option>
        <option value="180">최근 3시간 (180분)</option>
        <option value="360">최근 6시간 (360분)</option>
        <option value="1440">최근 24시간 (1440분)</option>
      </select>
      <button @click="fetchTemperatures" class="button primary">온도 데이터 조회</button>
      <button @click="startAutoRefresh" :disabled="autoRefreshIntervalId !== null" class="button secondary">자동 새로고침 시작</button>
      <button @click="stopAutoRefresh" :disabled="autoRefreshIntervalId === null" class="button danger">자동 새로고침 중지</button>
      <span v-if="autoRefreshIntervalId !== null" class="refresh-status">
        자동 새로고침 중... ({{ refreshInterval / 1000 }}초 간격)
      </span>
    </div>

    <div v-if="loading" class="loading-message">온도 데이터를 불러오는 중...</div>
    <div v-else-if="error" class="error-message">{{ error }}</div>
    <div v-else>
      <TemperatureChart :chartData="chartData" />
    </div>

    <router-link to="/" class="button secondary" style="margin-top: 20px;">장비 목록으로 돌아가기</router-link>
  </div>
</template>

<script>
import axios from 'axios';
import TemperatureChart from '../components/TemperatureChart.vue';

export default {
  name: 'MonitorDevicePage',
  components: {
    TemperatureChart,
  },
  props: ['deviceId'], // 라우터에서 전달받은 deviceId
  data() {
    return {
      device: null,
      loading: true,
      error: null,
      chartData: { // Chart.js 데이터 구조
        labels: [],
        datasets: [{
          label: '온도 (°C)',
          data: [],
          borderColor: 'rgb(75, 192, 192)',
          tension: 0.1,
          fill: false,
        }],
      },
      selectedTimeRange: 60, // 기본 조회 기간 60분
      autoRefreshIntervalId: null,
      refreshInterval: 5000, // 5초 간격으로 자동 새로고침
    };
  },
  async mounted() {
    await this.fetchDeviceAndInitialData();
  },
  beforeUnmount() {
    this.stopAutoRefresh(); // 컴포넌트가 파괴되기 전에 자동 새로고침 중지
  },
  methods: {
    async fetchDeviceAndInitialData() {
      this.loading = true;
      this.error = null;
      try {
        const deviceResponse = await axios.get(`${process.env.VUE_APP_BACKEND_API_URL}/devices/${this.deviceId}`);
        this.device = deviceResponse.data;

        // 초기 온도 데이터 불러오기
        await this.fetchTemperatures();

      } catch (err) {
        console.error('Error fetching device or initial temperatures:', err);
        this.error = '장비 정보 또는 초기 온도 데이터를 불러오는 데 실패했습니다.';
      } finally {
        this.loading = false;
      }
    },
    async fetchTemperatures() {
      // 조회 버튼 클릭 또는 자동 새로고침 시 호출
      this.loading = true; // 데이터 로딩 중 표시
      this.error = null;
      try {
        // 백엔드에서 `limit`은 분 단위로 전달됨
        const temperatureResponse = await axios.get(`${process.env.VUE_APP_BACKEND_API_URL}/temperatures/device/${this.deviceId}/recent?limit=${this.selectedTimeRange}`);
        
        // 데이터는 최신 순으로 오므로, 차트에는 오래된 순으로 표시하기 위해 역순으로 처리
        const sortedTemperatures = temperatureResponse.data.sort((a, b) => new Date(a.recordedAt) - new Date(b.recordedAt));
        
        this.chartData.labels = sortedTemperatures.map(data => new Date(data.recordedAt).toLocaleTimeString());
        this.chartData.datasets[0].data = sortedTemperatures.map(data => data.temperature);

        // Vue의 반응형 시스템이 배열 변경을 감지하도록 수동으로 트리거 (불필요할 수 있지만 안전하게)
        this.chartData = { ...this.chartData };
        
      } catch (err) {
        console.error('Error fetching temperatures:', err);
        this.error = '온도 데이터를 불러오는 데 실패했습니다.';
      } finally {
        this.loading = false;
      }
    },
    startAutoRefresh() {
      if (this.autoRefreshIntervalId === null) {
        this.autoRefreshIntervalId = setInterval(() => {
          this.fetchTemperatures();
        }, this.refreshInterval);
        console.log(`자동 새로고침 시작: ${this.refreshInterval / 1000}초 간격`);
      }
    },
    stopAutoRefresh() {
      if (this.autoRefreshIntervalId !== null) {
        clearInterval(this.autoRefreshIntervalId);
        this.autoRefreshIntervalId = null;
        console.log('자동 새로고침 중지');
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
  margin-bottom: 10px;
}
p {
  margin-bottom: 5px;
}
.controls {
  margin-top: 20px;
  margin-bottom: 30px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  gap: 15px;
}
.controls label {
  font-weight: bold;
}
.controls select {
  padding: 8px 12px;
  border-radius: 6px;
  border: 1px solid #ddd;
  font-size: 1em;
}
.controls .button {
  margin: 0; /* 기존 margin 겹침 방지 */
}
.refresh-status {
  font-size: 0.9em;
  color: #555;
  margin-left: 10px;
}
.loading-message, .error-message {
  margin-top: 20px;
  font-size: 1.1em;
}
.error-message {
  color: red;
}
</style>