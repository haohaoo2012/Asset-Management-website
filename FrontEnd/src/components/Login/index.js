import React, { Component, useEffect, useState } from "react";
import "./Login.css";
import { useCookies } from "react-cookie";
import { Button, Form, FormGroup, Label, Input } from "reactstrap";
import { Modal, ModalHeader, ModalBody, ModalFooter } from "reactstrap";
import axios from "axios";
import { post } from "../../httpHelper";
import { withRouter, useHistory, Redirect } from "react-router-dom";
import * as URL from "../../constants/URL";
import logo from "../../images/Logo_lk.png";

const Login = (props) => {
  const [cookies, setCookie] = useCookies(["user"]);
  const [isDisabled, setIsDisabled] = useState(true);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isFailed, setIsFailed] = useState(false);
  const [role, setRole] = useState("");
  let history = useHistory();

  useEffect(() => {
    if (username.length > 0 && password.length > 0) setIsDisabled(false);
    else setIsDisabled(true);

    if(cookies.user){
      if(cookies.user.role === "ROLE_ADMIN"){
        setRole("ROLE_ADMIN");
      }
      else if(cookies.user.role === "ROLE_USER"){
        setRole("ROLE_USER");
      }
    }
    console.log(role);
  }, [username, password]);

  const handleLogin = (e) => {
    e.preventDefault();
    let url = "signin";
    let body = {
      username,
      password,
    };
    post(url, body)
      .then((response) => {
        if (response.status === 200) {
          if (response.data.successCode === "USER_LOGIN_SUCCESS") {
            setCookie("user", JSON.stringify(response.data.data), {
              expires: new Date(new Date().valueOf() + 1000 * 3600 * 24),
              path: "/",
            });
            localStorage.setItem("jwtToken", response.data.data.token);
            localStorage.setItem("locationId", response.data.data.idLocation);
            if (response.data.data.role === "ROLE_ADMIN") {
              history.push("/admin");
            }
            if (response.data.data.role === "ROLE_USER") {
              history.push("/user");
            }
          }
        }
      })
      .catch((err) => {
        console.log(err);
        setIsFailed(true);
      });
  };

  return (
    <div>
      {
        role === 'ROLE_ADMIN' &&
        history.push("/admin")
      }
      {
        role === 'ROLE_USER' &&
        history.push("/user")
      }
      <div className="header">
        <span className="logo">
          <img src={logo} alt="logo company" width="50px" />
        </span>
        <span className="title">
          <b>Online Asset Management</b>
        </span>
      </div>
      <div id="Login" className="Login">
        <div id="Welcome_to_Online_Asset_Manage">
          <p>Welcome to Online Asset Management</p>
        </div>
        <div id="login-form">
          <form onSubmit={handleLogin}>
            <div className="login-data">
              <label htmlFor="username">
                Username
                <span style={{ color: "red", fontSize: "20px" }}>*</span>
              </label>
              <input
                type="text"
                name="username"
                value={username}
                onChange={({ target }) => setUsername(target.value)}
              />
            </div>
            <div className="login-data">
              <label htmlFor="password">
                Password
                <span style={{ color: "red", fontSize: "20px" }}>*</span>
              </label>
              <input
                type="password"
                name="password"
                value={password}
                onChange={({ target }) => setPassword(target.value)}
              />
            </div>
            <Button
              className="login-btn"
              disabled={isDisabled}
            >
              Login
            </Button>
          </form>
        </div>
        <Modal isOpen={isFailed}>
          <ModalHeader>Notice</ModalHeader>
          <ModalBody>
            <p style={{ color: "red" }}>
              Username or Password is incorrect. Please try again.
            </p>
          </ModalBody>
          <ModalFooter>
            <Button color="primary" onClick={() => setIsFailed(false)}>
              Close
            </Button>
          </ModalFooter>
        </Modal>
      </div>
    </div>
  );
};

export default withRouter(Login);
