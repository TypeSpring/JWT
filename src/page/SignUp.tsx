import React, { useState, FC, useCallback } from "react";
import axios from "axios";
import { request } from "../config/axios";
import { useNavigate } from "react-router";
const SignUp: FC = () => {
  const [email, setEmail] = useState<String>("");
  const [password, setPassword] = useState<String>("");
  const navigate = useNavigate();
  const hanldeSubmit = useCallback(
    (e) => {
      console.log("email : ", email);
      console.log("password : ", password);
      request
        .post("/auth/signup", {
          email: email,
          password: password,
        })
        .then((result) => {
          //회원가입에 성공하면 TokenTest로 이동함.
          console.log(result);
          console.log(result.status);
          if (result.status == 201) {
            navigate("/login");
          }
        });
      e.preventDefault();
    },
    [email, password]
  );
  return (
    <div className="SignUp">
      <div>회원가입!</div>
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
        <button>회원가입</button>
      </form>
    </div>
  );
};
export default SignUp;
