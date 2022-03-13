import React, { FC, useState } from "react";
import { request } from "../config/axios";
import { useNavigate } from "react-router";

const TokenTest: FC = () => {
  const navigate = useNavigate();
  const handleLogout = () => {
    window.localStorage.removeItem("A_TOKEN");
    localStorage.removeItem("R_TOKEN");
    localStorage.removeItem("GRANT_TYPE");
    localStorage.removeItem("A_TOKEN_EXPIRE");
    navigate("/", { replace: true });
  };
  const getAxios = async () => {
    await request.get("/test").then((res) => {
<<<<<<< HEAD
      console.log(res);
=======
>>>>>>> 25e5cf5abee909f7389f2c2ab97cd44bb302776f
      setAxiosResult(res.data);
    });
  };
  const postAxios = () => {
    request.post("/test").then((res) => {
      setAxiosResult(res.data);
      console.log(res);
    });
  };

  const putAxios = () => {
    request.put("/test").then((res) => {
      setAxiosResult(res.data);
      console.log(res);
    });
  };

  const deleteAxios = () => {
    request.delete("/test").then((res) => {
      setAxiosResult(res.data);
      console.log(res);
    });
  };

  const [axiosResult, setAxiosResult] = useState<String>("");
  return (
    <div className="TokenTest">
      로그인에 성공하였습니다.
      <div className="TokenTest__result">{axiosResult}</div>
      <div className="TokenTest__btnTest">
        <div>버튼들 테스트해보기</div>
        <button onClick={getAxios}> GET </button>
        <button onClick={postAxios}> POST </button>
        <button onClick={putAxios}> PUT </button>
        <button onClick={deleteAxios}> DELETE </button>
      </div>
      <button onClick={handleLogout}>로그아웃하기</button>
    </div>
  );
};
export default TokenTest;
