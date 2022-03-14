# JWT

### JWT를 이용한 인증 처리 시스템

> ### 요청 흐름

1. 유저가 로그인을 합니다.
2. 유저에게 Token을 발급해 줍니다.
   1. 유저를 특정할 수 있는 `어떠한 정보`를 Security의 User 정보의 name으로 등록합니다.
   2. 이 정보를 기반으로 각 토큰의 유효기간, Key 및 암호화 알고리즘을 이용하여 AccessToken, RefreshToken을 생성합니다.
   3. 유저에게 AccessToken, RefreshToken, 만료시간을 내어줍니다.
3. 유저가 로그인 이외의 API를 사용할 때 이 토큰을 Header에 담아 보내줍니다. 
   * Authorization 필드에, `Bearer ${accessToken}`
4. AccessToken의 유효 시간이 지나지 않았다면 헤더에 담긴 토큰을 이용하여 **어느 유저의 요청인지** 서버는 알 수 있습니다.
5. 하지만, 유효 시간이 다 지났다면, 유저는 **토큰 재발급**을 해야합니다.
6. 토큰 재발급을 할 때, **accessToken과 refreshToken 을 같이** 서버에 전송합니다.
   1. 첫 번째로, 서버는 refreshToken의 유효성을 검사합니다.
   2. 두 번째로, accessToken을 복호화하여 어느 유저인지 파악합니다.
   3. 세 번째로, refreshToken의 저장소에서 해당 유저의 refreshToken을 가져옵니다.
   4. 네 번째로, 현재 유저가 보낸 refreshToken과 저장소에서 가져온 refreshToken이 일치하는지 확인합니다.
   5. 위의 모든 검증이 일치한다면, 현재 유저 정보를 기반으로 새로운 토큰을 만들어 발급합니다. (2번 과정을 진행합니다.)
7. 새로 토큰을 받은 유저는 해당 토큰으로 다시 요청을 하면 됩니다.
8. 클라이언트 측에서 accessToken 만료시 토큰 재발급 요청을 할 때, 리프레시 토큰이 만료되지가 않았다면, 위와 같이 새로운 토큰 세트를 발급받아 로그인을 하지 않고도 계속적인 요청이 가능하지만, 리프레시 토큰 마저 만료가 되었다면, 다시 1번 과정부터 진행해야 합니다.


이와 같은 이유로, accessToken은 30분 정도, refreshToken은 일주일 정도의 유효기간을 갖도록 설정해놨습니다. 

**끊기지 않는 요청을 하려면 accessToken의 만료시간을 길게 가져가면 되지 않느냐?** 라고 할 수 있습니다.
하지만, accessToken이 탈취당한다면, 공격자는 해당 토큰으로 무슨 짓을 해도 자유로울 것입니다.

accessToken과 refreshToken이 나눠져 있는 이유는, accessToken이 탈취당한다면 거의 모든 api를 외부 공격자에 의해 사용될 수도 있기 때문에 **보안상의 이유로 추가를 한 것입니다.** 물론 refreshToken 마저 탈취 당한다면, -_-...; 그렇지만 리프레시 토큰도 유효기간이 영원하진 않답니다.

이와 같은 이유로 accessToken의 시간은 짧게 가져가고, refreshToken을 일주일 정도 유지하였습니다.
일주일 이내에 로그인을 하지 않는다면 새로 로그인을 해서 토큰을 발급 받아야겠죠?
자주 사용한다면, 30분마다 토큰 재발급을 통해서 토큰을 계속적으로 리프레시 할 것입니다.


<br/>



> ### 실제로 확인해보자

#### 회원 가입
![](/images/2022-03-14-02-47-41.png)
- 헤더에는 Authorization 정보가 없고, /auth로 오는 요청은 Security에서 허용했기 때문에, 회원 가입 이후 member table에 저장되는 것을 확인할 수 있습니다.



#### 로그인

![](/images/2022-03-14-02-50-39.png)

- 로그인 시 DB에 저장된 member의 비밀번호와 일치하는지 판단 이후, Token을 만들어 발급하는데, RefreshToken도 새로 만들기 때문에 이때 생성한 RefreshToken을 저장소에 넣어둡니다.

![](/images/2022-03-14-02-51-58.png)

로그인 이후에 accessToken, refreshToken을 받은 client입니다. 로그인을 했으므로, 앞으로 client는 모든 요청을 보낼 때 이 token을 header의 Authorization에 담아 보내야 합니다.


#### GET API 호출 시
![](/images/2022-03-14-02-54-43.png)

- Get Api 호출 시 클라이언트는 Authorization에 token을 담아 보냅니다.
- 서버는 `**/auth/**`이외의 path는 모두 Security에서 검증을 시도합니다.
- 애초에 이 프로젝트는 간단한 예제 프로젝트로서, email, password만 존재했기 때문에, email을 key값으로 활용합니다. 즉, SecurityContextHolder에는 email 값이 담겨 있기 때문에 어떤 사람의 요청인지 알 수 있게 됩니다.
- 이렇게 특정한 유저 정보를 알 수 있게 되면 요청에 대해서 어떤 사람이 어떤 액션을 했는지 처리할 수 있게 됩니다.

#### POST API
- GetApi와 동일합니다.
![](/images/2022-03-14-02-57-43.png)


#### PUT API
![](/images/2022-03-14-03-00-33.png)

- GET/POST의 경우 preflight의 대상이 아니지만, PUT, DELETE의 경우에는 preflight의 대상이 되기 때문에 Option 요청이 먼저 날아오는 것을 확인할 수 있습니다.


#### DELETE API
- PUT과 동일합니다.
![](/images/2022-03-14-03-02-35.png)



### RefreshToken을 발급받는 과정

1. Client는 로그인 이후에 AccessToken을 이용하여 각 api들을 사용합니다.
2. 이 때 서버는 매번 헤더로 넘어오는 이 AccessToken의 유효성을 검사합니다.
3. 만일, AccessToken의 유효기간이 모두 지났다면, 토큰 만료가 되었다는 것을 미리 만들어 두었던 ExceptionHandling을 통해 401 Response를 내어줍니다.
4. Client는 AccessToken이 만료가 되었다는 것을 401 Response로 알아차리고, AccessToken과 RefreshToken을 함께 서버에 전송하여 토큰 재발급을 요청합니다.
5. 서버는 다음과 같은 검증을 거칩니다.
   1. RefreshToken 유효성 검사
   2. AccessToken에서 정보만 빼와서 해당 유효 정보로 Member 정보를 가져옵니다.
   3. Member를 구분할 수 있는 Keyword로 로그인 시 RefreshToken의 Id로 저장했으니, 저장소에 있는 RefreshToken을 가져옵니다.
   4. 저장소에 있는 RefreshToken과 Client가 토큰 재발급을 위해 보냈던 RefreshToken의 동등성을 검사합니다.
   5. 일치한다면, 새로운 토큰 세트(AccessToken, RefreshToken)를 발급합니다.
6. 위 과정을 통해 새로운 토큰을 발급받은 클라이언트는 이 토큰을 이용하여 기존의 요청을 재 전송합니다.




<br/>

#### 코드 레벨의 자세한 기술은 [블로그](https://blog.naver.com/adamdoha)에서 `JWT`를 검색해주세요..!
