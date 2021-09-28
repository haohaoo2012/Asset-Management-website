import React from "react";
import { Redirect, Route } from "react-router-dom";
import { useCookies } from "react-cookie";
import * as URL from "../../constants/URL";

export default function ProtectedRoute({ component: Component, ...restOfProps }) {
  const [cookies, setCookie, removeCookie] = useCookies(["user"]);
  const isAuthenticated = cookies.user === undefined || cookies.user === "" ? false : true;
  return (
    <Route
      {...restOfProps}
      render={(props) => (isAuthenticated && 
        restOfProps.role.includes(cookies.user.role) ? <Component {...props} /> : <Redirect to={URL.LOGIN} />)}
    />
  );
}
