import React from "react";
import "./App.css";
import Home from "./page/Home";
import Login from "./page/Login";
import { Route, Routes, BrowserRouter } from "react-router-dom";
import TokenTest from "./page/TokenTest";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/tokenTest" element={<TokenTest />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
