import axios, { AxiosInstance } from "axios";

export const request: AxiosInstance = axios.create({
  baseURL: `${process.env.REACT_APP_URL}/api/v1`, // 기본 서버 주소 입력
});

// 새로고침하기전까지 header 가 변경이 안되는 이슈
// => interceptor를 통해서 refresh

request.interceptors.request.use(
  function (config) {
    console.log("request => config ====================================");
    console.log(config);
    console.log("request => config ====================================");
    config.headers.Authorization = `Bearer ${localStorage.getItem("A_TOKEN")}`;
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);
