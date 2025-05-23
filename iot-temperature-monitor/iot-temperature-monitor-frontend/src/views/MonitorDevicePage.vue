// iot-temperature-monitor-frontend/src/views/MonitorDevicePage.vue

<template>
  <div class="page-container">
    <h1>{{ deviceName }} 모니터링</h1>
    <p v-if="device">장비 ID: {{ device.deviceId }}</p>
    <p v-if="device">위치: {{ device.location }}</p>

    <div class="controls">
      <label for="timeRange">시간 범위:</label>
      <select id="timeRange" v-model="selectedTimeRange" @change="fetchTemperatures">
        <option value="60">최근 1시간</option>
        <option value="360">최근 6시간</option>
        <option value="1440">최근 24시간</option>
        <option value="10080">최근 7일</option>
      </select>
    </div>

    <div v-if="loading" class="loading-message">온도 데이터를 불러오는 중...</div>
    <div v-else-if="error" class="error-message">{{ error }}</div>
    <div v-else-if="temperatures.length === 0" class="no-data-message">
      선택된 시간 범위에 대한 온도 데이터가 없습니다.
    </div>
    <div v-else>
      <TemperatureChart :readings="temperatures" />

      <div class="readings-list-container">
        <h2>최근 온도 기록</h2>
        <table>
          <thead>
            <tr>
              <th>시간</th>
              <th>온도 (°C)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="reading in temperatures" :key="reading.id">
              <td>{{ new Date(reading.recordedAt).toLocaleString() }}</td>
              <td>{{ reading.temperature }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <router-link to="/" class="button secondary back-button">목록으로</router-link>
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
  data() {
    return {
      deviceId: null,
      device: null,
      deviceName: '장비', // 초기 장비 이름
      temperatures: [], // 중요: 이 배열이 초기에는 비어있어야 합니다.
      selectedTimeRange: 60, // 기본값: 최근 1시간 (분 단위)
      loading: true,
      error: null,
    };
  },
  created() {
    this.deviceId = this.$route.params.deviceId;
    this.fetchDeviceDetails();
    this.fetchTemperatures();
  },
  methods: {
    async fetchDeviceDetails() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_BACKEND_API_URL}/devices/${this.deviceId}`);
        this.device = response.data;
        this.deviceName = this.device.deviceName || '알 수 없는 장비';
      } catch (err) {
        console.error('Error fetching device details:', err);
        this.error = '장비 상세 정보를 불러오는 데 실패했습니다.';
      }
    },
    async fetchTemperatures() {
      this.loading = true;
      this.error = null;
      try {
        const temperatureResponse = await axios.get(
          `${process.env.VUE_APP_BACKEND_API_URL}/temperatures/device/${this.deviceId}/recent?limit=${this.selectedTimeRange}`
        );
        this.temperatures = temperatureResponse.data; // 데이터를 받아 temperatures 배열에 할당
      } catch (err) {
        console.error('Error fetching temperatures:', err);
        this.error = '온도 데이터를 불러오는 데 실패했습니다.';
        this.temperatures = []; // 오류 발생 시 배열을 비워 차트 오류 방지
      } finally {
        this.loading = false;
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
  color: #555;
  margin-bottom: 5px;
}

.controls {
  margin-top: 20px;
  margin-bottom: 20px;
}

.controls label {
  font-weight: bold;
  margin-right: 10px;
}

.controls select {
  padding: 8px 12px;
  border-radius: 5px;
  border: 1px solid #ddd;
  font-size: 1em;
}

.loading-message, .error-message, .no-data-message {
  margin-top: 20px;
  font-size: 1.1em;
  color: #555;
}

.error-message {
  color: red;
}

.readings-list-container {
  margin: 40px auto;
  max-width: 700px;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow-x: auto;
}

.readings-list-container h2 {
  color: #0056b3;
  margin-bottom: 15px;
}

.readings-list-container table {
  width: 100%;
  border-collapse: collapse;
}

.readings-list-container th,
.readings-list-container td {
  padding: 10px 15px;
  border: 1px solid #ddd;
  text-align: left;
}

.readings-list-container th {
  background-color: #f2f2f2;
  font-weight: bold;
  color: #333;
}

.readings-list-container tr:nth-child(even) {
  background-color: #f9f9f9;
}

.readings-list-container tr:hover {
  background-color: #f1f1f1;
}

.button.secondary.back-button {
  background-color: #6c757d;
  color: white;
  text-decoration: none;
  padding: 10px 20px;
  border-radius: 6px;
  margin-top: 20px;
  display: inline-block;
}

.button.secondary.back-button:hover {
  background-color: #5a6268;
}
</style>