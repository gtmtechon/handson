// iot-temperature-monitor-frontend/src/components/TemperatureChart.vue

<template>
  <div class="chart-container">
    <canvas ref="temperatureChart"></canvas>
  </div>
</template>

<script>
import {
  Chart,
  LineController,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend
} from 'chart.js';

Chart.register(
  LineController,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend
);

export default {
  name: 'TemperatureChart',
  props: {
    readings: {
      type: Array,
      required: true,
      default: () => [] // readings prop에 빈 배열을 기본값으로 설정 (가장 중요!)
    },
  },
  data() {
    return {
      chartInstance: null,
    };
  },
  watch: {
    readings: {
      handler: 'renderChart',
      deep: true,
    },
  },
  mounted() {
    this.renderChart();
  },
  methods: {
    renderChart() {
      // readings prop이 비어있거나 유효하지 않으면 차트를 그리지 않고 반환 (방어적 코드)
      if (!this.readings || this.readings.length === 0) {
        if (this.chartInstance) {
          this.chartInstance.destroy(); // 기존 차트가 있다면 파괴
          this.chartInstance = null; // 인스턴스 초기화
        }
        console.warn('TemperatureChart: No readings data to render chart.');
        return;
      }

      // 기존 차트 인스턴스가 있으면 파괴하여 메모리 누수 방지
      if (this.chartInstance) {
        this.chartInstance.destroy();
      }

      const ctx = this.$refs.temperatureChart.getContext('2d');

      // 데이터 가공
      const labels = this.readings.map(reading =>
        new Date(reading.recordedAt).toLocaleString()
      ).reverse();
      const temperatures = this.readings.map(reading => reading.temperature).reverse();

      this.chartInstance = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            {
              label: '온도 (°C)',
              data: temperatures,
              borderColor: 'rgb(75, 192, 192)',
              tension: 0.1,
              fill: false,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            x: {
              type: 'category',
              title: {
                display: true,
                text: '시간',
              },
            },
            y: {
              type: 'linear',
              title: {
                display: true,
                text: '온도 (°C)',
              },
              beginAtZero: false,
            },
          },
          plugins: {
            tooltip: {
              mode: 'index',
              intersect: false,
            },
            legend: {
              display: true,
              position: 'top',
            },
          },
        },
      });
    },
  },
  beforeUnmount() {
    if (this.chartInstance) {
      this.chartInstance.destroy();
    }
  },
};
</script>

<style scoped>
.chart-container {
  position: relative;
  width: 100%;
  height: 400px;
  margin: 20px 0;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>