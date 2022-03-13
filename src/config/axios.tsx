import axios, { AxiosInstance } from "axios";

export const request: AxiosInstance = axios.create({
  baseURL: `${process.env.REACT_APP_URL}/api/v1`, // 기본 서버 주소 입력
});

// 새로고침하기전까지 header 가 변경이 안되는 이슈
// => interceptor를 통해서 refresh
request.interceptors.response.use(
  function (response) {
    console.log("서버로 잘 보내짐");
    return response;
  },
  (error) => {
    console.log("토큰이 만료됨");

    axios
      .post(`${process.env.REACT_APP_URL}/api/v1/auth/reissue`, {
        accessToken: localStorage.getItem("A_TOKEN"),
        refreshToken: localStorage.getItem("R_TOKEN"),
      })
      .then((res) => {
        console.log("서버에서 받아온 토큰 넣기");
        localStorage.setItem("A_TOKEN", res.data.accessToken);
        localStorage.setItem("R_TOKEN", res.data.refreshToken);
      });

    return Promise.reject(error);
  }
);
request.interceptors.request.use(
  function (config) {
    console.log("매번 localStorage에서 A_TOKEN 다시 꺼내옴");
    config.headers.Authorization = `Bearer ${localStorage.getItem("A_TOKEN")}`;
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);
