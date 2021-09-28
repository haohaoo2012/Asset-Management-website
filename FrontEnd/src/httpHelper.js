import axios from "axios";
import * as URL from "./constants/URL";
import * as errorCode from "./constants/ErrorCode";
const endpoint = URL.EndPoint;

function getToken() {
  return getCookie("user") !== "" ? JSON.parse(getCookie("user")).token : "";
}

function getCookie(cname) {
  let name = cname + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(";");
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) === " ") {
      c = c.substring(1);
    }
    if (c.indexOf(name) === 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function setCookie(cname, cvalue, exdays) {
  const d = new Date();
  d.setTime(d.getTime() + exdays * 24 * 60 * 60 * 1000);
  let expires = "expires=" + d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function handleError(error) {
  if (error.response !== undefined) {
    if (error.response.data !== undefined) {
      if (
        error.response.data.error === errorCode.ERR_ROLE_DONT_HAVE_PERMISSION ||
        error.response.data.error === errorCode.ERR_USER_UNAUTHORIZED ||
        error.response.data.errorCode === errorCode.ERR_ACCESS_IS_DENIED ||
        error.response.data.errorCode === errorCode.ERR_BAD_CREDENTIALS
      ) {
        if (getCookie("user") !== "") {
          setCookie("user", "", 0);
        }
        if (window.location.pathname !== URL.LOGIN) window.location = URL.LOGIN;
      }
    }
  }
  console.log("Fail to call api");
  console.log(error);
}

export function get(url) {
  let token = getToken();
  var config = {
    method: "get",
    url: `${endpoint}/${url}`,
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  };

  let promise = axios(config);
  promise.catch((err) => {
    handleError(err);
  });
  return promise;
}

export function post(url, body) {
  let token = getToken();
  var config = {
    method: "post",
    url: `${endpoint}/${url}`,
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    data: body,
  };

  let promise = axios(config);
  promise.catch((err) => {
    handleError(err);
  });
  return promise;
}

export function put(url, body) {
  let token = getToken();
  var config = {
    method: "put",
    url: `${endpoint}/${url}`,
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    data: body,
  };
  let promise = axios(config);
  promise.catch((err) => {
    handleError(err);
  });
  return promise;
}

export function del(url) {
  let token = getToken();
  var config = {
    method: "delete",
    url: `${endpoint}/${url}`,
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  };
  let promise = axios(config);
  promise.catch((err) => {
    handleError(err);
  });
  return promise;
}
