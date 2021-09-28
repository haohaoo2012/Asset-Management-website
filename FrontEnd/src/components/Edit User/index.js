import axios from "axios";
import React, { useEffect, useState } from "react";
import { useHistory, useParams, Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import "./EditUser.css";
import { get, put } from "../../httpHelper";

const Checkbox = ({ label, value, onChange }) => {
  return (
    <>
      <input type="radio" checked={value} onChange={onChange} />
      <label>{label}</label>
    </>
  );
};

const EditUser = () => {
  const [user, setUser] = useState(false);
  const [cookies, setCookie, removeCookie] = useCookies(["user"]);
  const [lastName, setLastName] = useState("");
  const [firstName, setFirstName] = useState("");
  const [dateOfBirth, setDateOfBirth] = useState("");
  const [gender, setGender] = useState("");
  const [joinedDate, setJoinedDate] = useState("");
  const [role, setRole] = useState("ROLE_ADMIN");
  const [location, setLocation] = useState(1);

  const [checkedFemale, setCheckedFemale] = useState(false);
  const [checkedMale, setCheckedMale] = useState(false);

  const [valid, setValid] = useState("");
  const [validDOB, setValidDOB] = useState(true);
  const [validJD, setValidJD] = useState(true);

  const [isEnabled, setIsEnabled] = useState(false);

  const [userInfo, setUserInfo] = useState(null);

  let history = useHistory();
  let { staffCode } = useParams();

  useEffect(() => {
    try {
      //get user
      setUser(cookies.user);
      setLocation(cookies.user.idLocation);
    } catch (error) {
      history.push(URL.LOGIN);
    }
    //get info
    let Url = "user/" + staffCode.trim();
    get(Url)
      .then((response) => setUserInfo(response.data.data))
      .catch((err) => console.log(err));
  }, []);

  useEffect(() => {
    console.log(userInfo);
    if (userInfo !== null) {
      setFirstName(userInfo.firstName);
      setLastName(userInfo.lastName);
      setDateOfBirth(userInfo.dateOfBirth.slice(0, 10));
      setJoinedDate(userInfo.joinedDate.slice(0, 10));
      setRole(userInfo.role);
      if (userInfo.gender === "FEMALE") handleChangeFemale();
      else handleChangeMale();
    }
  }, [userInfo]);

  useEffect(() => {
    //do something
  }, [valid]);

  useEffect(() => {
    console.log(isEnabled);
    if (
      lastName.length > 0 &&
      firstName.length > 0 &&
      dateOfBirth.length > 0 &&
      gender.length > 0 &&
      joinedDate.length > 0
    )
      setIsEnabled(true);
    else setIsEnabled(false);
  }, [firstName, lastName, dateOfBirth, gender, joinedDate]);

  const handleValid = (mes, type) => {
    setValid(mes);
    if (!type) {
      setValidDOB(false);
      setValidJD(true);
    } else {
      setValidJD(false);
      setValidDOB(true);
    }
  };

  const handleChangeFemale = () => {
    setGender("Female");
    setCheckedFemale((checkedFemale) => !checkedFemale);
    setCheckedMale(false);
  };

  const handleChangeMale = () => {
    setGender("Male");
    setCheckedMale((checkedMale) => !checkedMale);
    setCheckedFemale(false);
  };

  const CheckValidation = () => {
    var dob = new Date(dateOfBirth);
    var jd = new Date(joinedDate);
    var now = new Date();
    var age = now.getFullYear() - dob.getFullYear();
    if (age < 18) {
      handleValid("User is under 18. Please select a different date", false);
      return false;
    }
    if (jd < dob) {
      handleValid(
        "Joined date is not later than Date of Birth. Please select a different date",
        true
      );
      return false;
    }
    if (jd.getDay() == 0 || jd.getDay() == 6) {
      handleValid(
        "Joined date is Saturday or Sunday. Please select a different date",
        true
      );
      return false;
    }
    return true;
  };

  const handleSaveUser = () => {
    const check = CheckValidation();
    const data = {
      dateOfBirth: dateOfBirth + " 00:00",
      gender,
      joinedDate: joinedDate + " 00:00",
      role,
      location,
    };
    let Url = "user/update/" + staffCode.trim();
    if (check) {
      put(Url, data)
        .then((response) => {
          alert("Edit User OK!");
          localStorage.setItem(
            "edittedUser",
            JSON.stringify(response.data.data)
          );
          history.push("/manageuser");
        })
        .catch((err) => console.log(err));
    }
  };

  const handleCancel = () => {
    //go back to manage user page
    history.push("/manageuser");
  };

  return (
    <div id="edit-user">
      <div id="edit-user-title">Edit User</div>
      <form id="edit-user-form">
        <div className="First_name">
          <label htmlFor="firstname" className="label">
            First Name
          </label>
          <input
            className="input"
            type="text"
            name="firstname"
            value={firstName}
            disabled
          />
        </div>
        <div className="Last_name">
          <label htmlFor="lastname" className="label">
            Last Name
          </label>
          <input
            className="input"
            type="text"
            name="lastname"
            value={lastName}
            disabled
          />
        </div>
        <div className="DOB">
          <label htmlFor="dateOfBirth" className="label">
            Date Of Birth
          </label>
          <input
            className={!validDOB ? "red-input" : "input"}
            type="date"
            name="dateOfBirth"
            value={dateOfBirth}
            onChange={({ target }) => {
              setDateOfBirth(target.value);
              setValidDOB(true);
            }}
          />
          <p className={!validDOB ? "show-error" : "none"}>{valid}</p>
        </div>
        <div className="Gender">
          <p>Gender</p>
          <div className="gender-check">
            <Checkbox
              label="Female"
              value={checkedFemale}
              onChange={handleChangeFemale}
            />
            &ensp;&ensp;&ensp;&ensp;&ensp;
            <Checkbox
              label="Male"
              value={checkedMale}
              onChange={handleChangeMale}
            />
          </div>
        </div>
        <div className="Joined_Date">
          <label htmlFor="joinedDate" className="label">
            Joined Date
          </label>
          <input
            className={!validJD ? "red-input" : "input"}
            type="date"
            name="joinedDate"
            value={joinedDate}
            onChange={({ target }) => {
              setJoinedDate(target.value);
              setValidJD(true);
            }}
          />
          <p className={!validJD ? "show-error" : "none"}>{valid}</p>
        </div>
        <div className="Type">
          <label htmlFor="role" className="label">
            Type
          </label>
          <select
            className="input"
            name="role"
            value={role}
            onChange={({ target }) => setRole(target.value)}
          >
            <option value="ROLE_ADMIN">Admin</option>
            <option value="ROLE_USER">Staff</option>
          </select>
        </div>
      </form>
      <div className="button-flex">
        <button
          className="save-btn"
          onClick={handleSaveUser}
          disabled={!isEnabled}
        >
          SAVE
        </button>
        <button className="cancel-btn" onClick={handleCancel}>
          CANCEL
        </button>
      </div>
    </div>
  );
};

export default EditUser;
