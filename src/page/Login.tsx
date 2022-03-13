import React, { useState, FC, useCallback } from "react";
import { request } from "../config/axios";
import { useNavigate } from "react-router";

const Login: FC = () => {
  const [email, setEmail] = useState<String>("");
  const [password, setPassword] = useState<String>("");
  const navigate = useNavigate();
  const hanldeSubmit = useCallback(
    (e) => {
      console.log("email : ", email);
      console.log("password : ", password);
      request
        .post("/auth/signin", {
          email: email,
          password: password,
        })
        .then((result) => {
          //로그인에 성공하면 TokenTest로 이동함.
          console.log(result);
          console.log(result.status);
          if (result.status == 200) {
            window.localStorage.setItem("A_TOKEN", result.data.accessToken);
            window.localStorage.setItem("R_TOKEN", result.data.refreshToken);
            window.localStorage.setItem("GRANT_TYPE", result.data.grantType);
            window.localStorage.setItem(
              "A_TOKEN_EXPIRE",
              result.data.accessTokenExpiresIn
            );
            navigate("/tokenTest");
          }
        });
      e.preventDefault();
    },
    [email, password]
  );
  return (
    <div className="Login">
      <div>로그인!</div>
      <form onSubmit={(e) => hanldeSubmit(e)}>
        <div>
          <div>EMAIL : </div>
          <input
            type="email"
            required
            placeholder="이메일"
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div>
          <div>PASSWORD : </div>
          <input
            type="password"
            required
            placeholder="비밀번호"
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button>로그인</button>
      </form>
    </div>
  );
};
export default Login;
