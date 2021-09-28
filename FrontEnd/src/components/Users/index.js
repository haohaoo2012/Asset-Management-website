import React, { useState, useEffect } from "react";
import "./Users.css";
import User from "./User";
import searchIcon from "../../images/search.png";
import Paginations from "./Pagination/Pagination";
import { Popover, Button } from "@material-ui/core";
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import ArrowDropUpIcon from "@material-ui/icons/ArrowDropUp";
import { useHistory } from "react-router";
import { get } from "../../httpHelper";
import SearchIcon from "@material-ui/icons/Search";

const Index = () => {
  const [users, setUsers] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPage, setTotalPage] = useState(5);
  const [size, setSize] = useState(15);
  const [sort, setSort] = useState("staffCodeASC");
  const [search, setSearch] = useState(
    "isDeleted:false,staffCode:,username:,role:,location:"
  );
  const [nameSearch, setNameSearch] = useState("");
  const [name, setName] = useState("");
  const [staffCode, setStaffCode] = useState("");
  const [roleSearch, setRoleSearch] = useState("");
  const [isStaffCodeASC, setIsStaffCodeASC] = useState(true);
  const [isFullNameCodeASC, setIsFullNameCodeASC] = useState(true);
  const [isUserNameASC, setisUserNameASC] = useState(true);
  const [isJoinedDateASC, setisJoinedDateASC] = useState(true);
  const [isTypeASC, setIsTypeASC] = useState(true);
  const [locationId, setLocationId] = useState("");
  const [open, setOpen] = React.useState(false);

  //const locationId = localStorage.getItem("locationId");

  const history = useHistory();

  const handleClick = () => {
    setOpen((prev) => !prev);
  };

  const handleClickAway = () => {
    setOpen(false);
  };

  useEffect(() => {
    const fetchUsers = async () => {
      const location = await localStorage.getItem("locationId");
      await setLocationId(location);
      const url =
        await `user?page=${currentPage}&size=${size}&sort=${sort}&search=isDeleted:false,staffCode:${staffCode},username:${name},role:${roleSearch},location:${location}`;
      const response = await get(url);
      const usersData = response.data.data;
      setTotalPage(usersData.totalPages);
      if (localStorage.getItem("topUser") !== null) {
        console.log(JSON.parse(localStorage.getItem("topUser")));
        const newAddedUser = JSON.parse(localStorage.getItem("topUser"));
        const newUsers = usersData.data.filter(
          (user) => user.staffCode !== newAddedUser.staffCode
        );
        setUsers([newAddedUser, ...newUsers]);
        localStorage.removeItem("topUser");
      } else if (localStorage.getItem("edittedUser") !== null) {
        const edditedUser = JSON.parse(localStorage.getItem("edittedUser"));
        const newUsers = usersData.data.filter(
          (user) => user.staffCode !== edditedUser.staffCode
        );
        setUsers([edditedUser, ...newUsers]);
        localStorage.removeItem("edittedUser");
      } else {
        setUsers(usersData.data);
      }
    };
    fetchUsers();
  }, [currentPage, size, sort, search, roleSearch]);

  const handleOnClickSearchButton = (e) => {
    e.preventDefault();
    setCurrentPage(0);
    if (nameSearch.toUpperCase().startsWith("SD")) {
      setStaffCode(nameSearch.toUpperCase());
      setName("");
      setSearch(
        `isDeleted:false,staffCode:${nameSearch},username:,role:${roleSearch},location:${locationId}`
      );
    } else {
      setName(nameSearch.toLocaleLowerCase());
      setStaffCode("");
      setSearch(
        `isDeleted:false,staffCode:,username:${nameSearch},role:${roleSearch},location:${locationId}`
      );
    }
  };

  const handleSortByStaffCode = () => {
    if (isStaffCodeASC) {
      setSort("staffCodeASC");
      setIsStaffCodeASC(!isStaffCodeASC);
    } else {
      setSort("staffCodeDES");
      setIsStaffCodeASC(!isStaffCodeASC);
    }
  };

  const handleSortByFullName = () => {
    if (isFullNameCodeASC) {
      setSort("lastNameASC");
      setIsFullNameCodeASC(!isFullNameCodeASC);
    } else {
      setSort("lastNameDES");
      setIsFullNameCodeASC(!isFullNameCodeASC);
    }
  };
  const handleSortByUserName = () => {
    if (isUserNameASC) {
      setSort("usernameASC");
      setisUserNameASC(!isUserNameASC);
    } else {
      setSort("usernameDES");
      setisUserNameASC(!isUserNameASC);
    }
  };
  const handleSortByJoinedDate = () => {
    if (isJoinedDateASC) {
      setSort("joinedDateASC");
      setisJoinedDateASC(!isJoinedDateASC);
    } else {
      setSort("joinedDateDES");
      setisJoinedDateASC(!isJoinedDateASC);
    }
  };
  const handleSortType = () => {
    if (isTypeASC) {
      setSort("roleASC");
      setIsTypeASC(!isTypeASC);
    } else {
      setSort("roleDES");
      setIsTypeASC(!isTypeASC);
    }
  };

  const handleClickCreateNew = () => {
    history.push("/createnewuser");
  };

  const onDelete = (staffCode) => {
    const newUsers = users.filter((user) => user.staffCode !== staffCode);
    setUsers(newUsers);
  };

  const [isAdminSelected, setIsAdminSelected] = useState(false);
  const [isStaffSelected, setIsStaffSelected] = useState(false);
  const [isAllSelected, setIsAllSelected] = useState(false);
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleTypeFilterClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const openType = Boolean(anchorEl);
  const id = open ? "simple-popover" : undefined;
  return (
    <div id="users">
      <p className="title">User List</p>
      <div id="Search_bar">
        <div id="filter_zone">
          <input id="type-input" placeholder="Type" disabled />
          <svg class="Line_6" viewBox="0 0 1 33">
            <path id="Line_6" d="M 0 0 L 0 33"></path>
          </svg>
          <svg
            class="funnel-fill"
            viewBox="1.5 1 15 15"
            onClick={handleTypeFilterClick}
          >
            <path
              id="funnel-fill"
              d="M 1.49999988079071 1.535728693008423 C 1.49999988079071 1.239854097366333 1.758297204971313 0.9999999403953552 2.076923131942749 1 L 15.92307758331299 1 C 16.24170112609863 1 16.49999809265137 1.239854097366333 16.49999809265137 1.535728693008423 L 16.49999809265137 3.678643465042114 C 16.49999809265137 3.81066632270813 16.44742393493652 3.938126087188721 16.35237503051758 4.036438941955566 L 11.307692527771 9.24165153503418 L 11.307692527771 14.39321804046631 C 11.30750370025635 14.62370872497559 11.14857292175293 14.8282527923584 10.91307735443115 14.90108776092529 L 7.451538562774658 15.9725456237793 C 7.275689601898193 16.02693367004395 7.08240795135498 15.99955558776855 6.932019710540771 15.89895534515381 C 6.781630516052246 15.79835605621338 6.692448616027832 15.63678741455078 6.692307472229004 15.464674949646 L 6.692307472229004 9.24165153503418 L 1.647692322731018 4.036510467529297 C 1.552624702453613 3.93821382522583 1.500025510787964 3.810762882232666 1.49999988079071 3.678643465042114 L 1.49999988079071 1.535728693008423 Z"
            ></path>
          </svg>
          <Popover
            id={id}
            className="popover-type"
            open={openType}
            anchorEl={anchorEl}
            onClose={handleClose}
            anchorOrigin={{
              vertical: "bottom",
              horizontal: "left",
            }}
            transformOrigin={{
              vertical: "top",
              horizontal: "center",
            }}
          >
            <div id="type_popover">
              {isAllSelected ? (
                <div className="type-item">
                  <label class="container1">
                    All
                    <input
                      type="checkbox"
                      checked
                      onChange={() => {
                        setCurrentPage(0);
                        setRoleSearch("");
                        setIsAllSelected(false);
                        setIsAdminSelected(false);
                        setIsStaffSelected(false);
                      }}
                    />
                    <span class="checkmark1"></span>
                  </label>
                </div>
              ) : (
                <div className="type-item">
                  <label class="container1">
                    All
                    <input
                      type="checkbox"
                      onChange={() => {
                        setCurrentPage(0);
                        setRoleSearch("");
                        setIsAllSelected(true);
                        setIsAdminSelected(true);
                        setIsStaffSelected(true);
                      }}
                    />
                    <span class="checkmark1"></span>
                  </label>
                </div>
              )}
              {isAdminSelected ? (
                <div className="type-item">
                  <label class="container1">
                    Admin
                    <input
                      type="checkbox"
                      checked
                      onChange={() => {
                        setCurrentPage(0);
                        if (isStaffSelected) {
                          setRoleSearch("1001");
                        } else {
                          setRoleSearch("");
                        }
                        setIsAdminSelected(false);
                        setIsAllSelected(false);
                      }}
                    />
                    <span class="checkmark1"></span>
                  </label>
                </div>
              ) : (
                <div className="type-item">
                  <label class="container1">
                    Admin
                    <input
                      type="checkbox"
                      onChange={() => {
                        setCurrentPage(0);
                        if (isStaffSelected) {
                          setIsAdminSelected(true);
                          setIsAllSelected(true);
                          setRoleSearch("");
                        } else {
                          setRoleSearch("1002");
                          setIsAdminSelected(true);
                        }
                      }}
                    />
                    <span class="checkmark1"></span>
                  </label>
                </div>
              )}
              {isStaffSelected ? (
                <div className="type-item">
                  <label class="container1">
                    Staff
                    <input
                      type="checkbox"
                      checked="checked"
                      onChange={() => {
                        setCurrentPage(0);
                        if (isAdminSelected) {
                          setRoleSearch("1002");
                        } else {
                          setRoleSearch("");
                        }
                        setIsStaffSelected(false);
                        setIsAllSelected(false);
                      }}
                    />
                    <span class="checkmark1"></span>
                  </label>
                </div>
              ) : (
                <div className="type-item">
                  <label class="container1">
                    Staff
                    <input
                      type="checkbox"
                      onChange={() => {
                        setCurrentPage(0);
                        if (isAdminSelected) {
                          setIsStaffSelected(true);
                          setIsAllSelected(true);
                          setRoleSearch("");
                        } else {
                          setIsStaffSelected(true);
                          setRoleSearch("1001");
                        }
                      }}
                    />
                    <span class="checkmark1"></span>
                  </label>
                </div>
              )}
            </div>
          </Popover>
        </div>
        <div id="right_search_bar">
          <input
            type="text"
            id="name_search_zone"
            onChange={(e) => setNameSearch(e.target.value)}
          />
          <svg class="Line_5" viewBox="0 0 1 33">
            <path id="Line_5" d="M 0 0 L 0 33"></path>
          </svg>
          <SearchIcon id="search_icon1" onClick={handleOnClickSearchButton} />
          <button id="create_button1" onClick={handleClickCreateNew}>
            Create new user
          </button>
        </div>
      </div>

      <table>
        <thead>
          <tr>
            <th onClick={handleSortByStaffCode} className="headerUser">
              Staff Code
              {isStaffCodeASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th onClick={handleSortByFullName} className="headerUser">
              Full Name
              {isFullNameCodeASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th onClick={handleSortByUserName} className="headerUser">
              Username
              {isUserNameASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th onClick={handleSortByJoinedDate} className="headerUser">
              Joined Date
              {isJoinedDateASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
            <th onClick={handleSortType} className="headerUser">
              Type
              {isTypeASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
            </th>
            <th> </th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => {
            return <User user={user} onDelete={onDelete} />;
          })}
        </tbody>
      </table>

      <Paginations
        currentPage={currentPage}
        totalPages={totalPage}
        setCurrentPage={(current) => setCurrentPage(current)}
        className="pagination"
      />
    </div>
  );
};

export default Index;
