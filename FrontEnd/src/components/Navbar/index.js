import React, { Component, useEffect, useState } from "react";
import "./Navbar.css";
import { UncontrolledButtonDropdown, DropdownMenu, DropdownItem, DropdownToggle } from "reactstrap";
import Logout from "../Logout";
import { useCookies } from "react-cookie";
import { useHistory } from "react-router-dom";
import Change_Password from "../Change Password";

const Navbar = (props) => {
  const [isShowLogout, setIsShowLogout] = useState(false);
  const [disabledPass, setDisabledPass] = useState(true);
  const [disabledLogout, setDisabledLogout] = useState(false);
  const [user, setUser] = useState(false);
  const [cookies, setCookie, removeCookie] = useCookies(["user"]);
  let history = useHistory();
  const [isShowChangePass, setIsShowChangePass] = useState(false);

  useEffect(() => {
    try {
      setUser(cookies.user);
      //change password
      if (cookies.user.firstLogin) setDisabledPass(false);
      else setDisabledPass(false);
      //logout
      if (window.location.pathname === "/first") setDisabledLogout(true);
      else setDisabledLogout(false);
    } catch (e) {
      history.push(URL.LOGIN);
    }
  }, []);

  const handleLogoutShow = () => {
    setIsShowLogout((isShowLogout) => true);
    setIsShowChangePass((isShowChangePass) => false);
  };

  const handleChangpassword = () => {
    setIsShowChangePass((isShowChangePass) => true);
    setIsShowLogout((isShowLogout) => false);
  };

  const handleLogoutCancel = () => {
    setIsShowLogout((isShowLogout) => false);
  };
  const handleChangpasswordCancel = () => {
    setIsShowChangePass((isShowChangePass) => false);
  };
  return (
    <div id="narbav_ecg">
      <svg className="Rectangle_329_ech">
        <rect id="Rectangle_329_ech" rx="0" ry="0" x="0" y="0" width="1920" height="72"></rect>
      </svg>
      <div id="Manage_Assignment__Create_New__eci">
        {/* <span>Manage Assignment {`>`} Create New Assignment</span> */}
        <span>{props.businessName}</span>
      </div>
      <UncontrolledButtonDropdown className="dropdown">
        <DropdownToggle caret color="#CF2338" className="username">
          {user.username}
        </DropdownToggle>
        <DropdownMenu right>
          <DropdownItem onClick={handleLogoutShow} disabled={disabledLogout}>
            Log Out
          </DropdownItem>
          <DropdownItem divider />
          <DropdownItem onClick={handleChangpassword} disabled={disabledPass}>
            Change Password
          </DropdownItem>
        </DropdownMenu>
      </UncontrolledButtonDropdown>
      <Logout isShowLogout={isShowLogout} handleLogoutCancel={handleLogoutCancel} />
      <Change_Password isShowChangePass={isShowChangePass} handleChangpasswordCancel={handleChangpasswordCancel} />
    </div>
  );
};

export default Navbar;
