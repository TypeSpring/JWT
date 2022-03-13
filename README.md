<<<<<<< Updated upstream
# JWT
=======
### JWT

### JWT를 이용한 로그인 구현

1. 로그인을 하면 서버로부터 응답값을 받아옵니다.
2. 로그인 성공시 1에서 받은 토큰을 로컬스토리지에 저장하고, TokenTest로 navigate 됩니다.
3. TokenTest 에서 **GET, POST, DELETE, PUT**을 테스트 해볼 수 있습니다.
4. 제대로 토큰이 갔다면 서버측에서 응답값으로 보내주고, 이를 화면에 띄워줍니다.
5. 로그아웃 버튼을 누르면 로컬스토리지에 저장한 토큰을 remove하고 다시 home화면으로 이동합니다.

### Refresh

1. server로 accessToken으로 요청한다.
2. 1의 accessToken이 만료되지 않았다면 server에서 정상적인 응답값이 들어온다.
3. 1의 accessToken이 만료되었다면, 서버측에서 만료되었다는 응답을 준다 (HTTP status code : **401**)
4. 이 때 axios interceptors(**response**)가 error를 감지하여 `${process.env.REACT_APP_URL}/api/v1/auth/reissue`로 accessToken 과 refreshToken을 보낸다.
5. 4에 대한 응답값으로 서버에서 새로운 accessToken과 refreshToken을 보내준다.
6. axios interceptor(**request**)가 새로 발급받은 토큰으로 다시 서버에 요청한다.
7. 6의 경우 만료기간 전에 요청했다면 만료되지 않은 토큰이므로 정상적인 응답값을 준다.

<img width="705" alt="스크린샷 2022-03-13 오후 7 40 53" src="https://user-images.githubusercontent.com/72402747/158057226-75ad9c8a-6d4b-4cc6-8ff8-0ceb857f41f0.png">
>>>>>>> Stashed changes
