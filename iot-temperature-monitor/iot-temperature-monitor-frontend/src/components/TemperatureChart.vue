<template>
    <div class="chart-container">
      <canvas ref="chartCanvas"></canvas>
    </div>
  </template>
  
  <script>
  import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale } from 'chart.js';
  
  // Chart.js 모듈 등록
  ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale);
  
  export default {
    name: 'TemperatureChart',
    props: {
      chartData: {
        type: Object,
        required: true,
      },
    },
    data() {
      return {
        chartInstance: null,
      };
    },
    watch: {
      chartData: {
        handler(newVal) {
          if (this.chartInstance) {
            this.chartInstance.data = newVal;
            this.chartInstance.update();
          } else {
            this.renderChart();
          }
        },
        deep: true, // chartData 객체 내부의 변경도 감지
        immediate: true, // 컴포넌트 마운트 시 즉시 실행
      },
    },
    mounted() {
      this.renderChart();
    },
    beforeUnmount() {
      if (this.chartInstance) {
        this.chartInstance.destroy(); // 컴포넌트 파괴 시 차트 인스턴스 정리
      }
    },
    methods: {
      renderChart() {
        if (this.chartInstance) {
          this.chartInstance.destroy();
        }
        this.chartInstance = new ChartJS(this.$refs.chartCanvas, {
          type: 'line',
          data: this.chartData,
          options: {
            responsive: true,
            maintainAspectRatio: false, // 컨테이너 크기에 맞춰 조절
            scales: {
              x: {
                title: {
                  display: true,
                  text: '시간',
                },
              },
              y: {
                title: {
                  display: true,
                  text: '온도 (°C)',
                },
                beginAtZero: false, // 온도가 0부터 시작할 필요는 없음
              },
            },
            plugins: {
              legend: {
                display: true,
                position: 'top',
              },
              tooltip: {
                mode: 'index',
                intersect: false,
              },
            },
          },
        });
      },
    },
  };
  </script>
  
  <style scoped>
  .chart-container {
    width: 90%; /* 화면 너비에 맞게 조절 */
    max-width: 800px; /* 최대 너비 설정 */
    height: 400px; /* 높이 고정 또는 반응형으로 조절 */
    margin: 30px auto;
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  </style>