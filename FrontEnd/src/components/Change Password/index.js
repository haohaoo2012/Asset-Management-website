import React, { Component, useEffect, useState } from "react";
import "./ChangePassword.css";
import { useHistory, useParams } from "react-router-dom";
import { Button } from "reactstrap";
import { useCookies } from "react-cookie";
import { put } from "../../httpHelper";
import { changePassFailException } from "../../exceptions/ChangePassException";
import * as ROLE from "../../constants/Business";
import * as URL from "../../constants/URL";

const ChangePassword = (props) => {
  const [user, setUser] = useState(false);
  const [username, setUsername] = useState("");
  const [cookies, setCookie, removeCookie] = useCookies(["user"]);
  const [newPassword, setNewPassword] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [isDisabled, setIsDisable] = useState(true);
  const [message, setMessage] = useState("");
  const [notification, setnotification] = useState(false);

  let history = useHistory();

  useEffect(() => {
    try {
      //getuser and set username
      setUser(cookies.user);
      setUsername(cookies.user.username);
    } catch (error) {
      history.push(URL.LOGIN)
    }
  }, []);

  useEffect(() => {
    if (newPassword.length > 0 && oldPassword.length > 0) setIsDisable(false);
    else setIsDisable(true);
  }, [newPassword, oldPassword]);

  const handleChangePassword = () => {
    if (cookies.user.role === ROLE.ROLE_ADMIN) {
      handleAdminChangePassword();
    } else if (cookies.user.role === ROLE.ROLE_STAFF) {
      handleUserChangePassword();
    }
  };

  const handleAdminChangePassword = () => {
    let Url = "admin/password/" + username;
    let data = {
      oldPassword,
      newPassword,
    };
    put(Url, data)
      .then(() => {
        setnotification(true);
        props.handleChangpasswordCancel();
      })
      .catch((err) => {
        setMessage(changePassFailException(err));
        clearMessage();
      });
  };
  const handleUserChangePassword = () => {
    let Url = "user/password/" + username;
    let data = {
      oldPassword,
      newPassword,
    };
    put(Url, data)
      .then(() => {
        setnotification(true);
        props.handleChangpasswordCancel();
      })
      .catch((err) => {
        setMessage(changePassFailException(err));
        clearMessage();
      });
  };
  const clearMessage = () => {
    setTimeout(() => {
      setMessage("");
    }, 6000);
  };

  const handleCancelChangePassword = () => {
    //cancel
    setnotification(false);
    props.handleChangpasswordCancel();
  };

  return (
    <>
      <div id="Change-password" className={`${!props.isShowChangePass ? "d-none" : ""}`}>
        <div id="changepassword-title">
          <p>Change Password</p>
        </div>
        <div id="changepassword-form">
          <form>
            <div className="changepassword-data">
              <label htmlFor="old">Old Password</label>
              <input
                className={message && "erroldpass"}
                type="password"
                name="old"
                value={oldPassword}
                onChange={({ target }) => setOldPassword(target.value)}
              />
              <p className="Change-password-alert ">{message}</p>
            </div>
            <div className="changepassword-data">
              <label htmlFor="new">New Password</label>
              <input type="password" name="new" value={newPassword} onChange={({ target }) => setNewPassword(target.value)} />
            </div>
          </form>
          <div className="changepassword-btn">
            <Button className="save-btn" disabled={isDisabled} onClick={handleChangePassword}>
              SAVE
            </Button>
            <button className="cancel-btn" onClick={handleCancelChangePassword}>
              CANCEL
            </button>
          </div>
        </div>
      </div>
      {notification && (
        <div id="notification">
          <div className="delete-asset-header">
            <p>Change Password</p>
          </div>
          <div className="delete-asset-body">
            <br></br>
            <p>Your password has been change successfully!</p>
            <Button id="cancel-changepasss" onClick={handleCancelChangePassword} outline color="secondary">
              Close
            </Button>{" "}
            <br></br>
            <br></br>
          </div>
        </div>
      )}
    </>
  );
};

export default ChangePassword;
