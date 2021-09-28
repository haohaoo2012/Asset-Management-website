import "./EditAssignment.css";
import React, { useState, useEffect } from "react";
import { Dialog, Slide, Button, TextareaAutosize } from "@material-ui/core";
import SearchIcon from "@material-ui/icons/Search";
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import ArrowDropUpIcon from "@material-ui/icons/ArrowDropUp";
import { get, put } from "../../httpHelper";
import Paginations from "../Users/Pagination/Pagination";
import { useHistory, useParams } from "react-router-dom";
import * as URL from "../../constants/URL";
import * as STATE from "../../constants/State";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});
const EditAssignment = () => {
  const [username, setUsername] = useState("");
  const [asset, setAsset] = useState("");
  const [assignedDate, setAssignedDate] = useState("");
  const [note, setNote] = useState("");
  const [isOpenUserDialog, setIsOpenUserDialog] = useState(false);
  const [isOpenAssetDialog, setIsOpenAssetDialog] = useState(false);
  const [totalPage, setTotalPage] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [usernameSearch, setUsernameSearch] = useState("");
  const [staffCodeSearch, setStaffCodeSearch] = useState("");
  const [sort, setSort] = useState("staffCode");
  const [users, setUsers] = useState([]);
  const [boxusersearch, setBoxUserSearch] = useState();
  const [isStaffCodeASC, setIsStaffCodeASC] = useState(true);
  const [isFullNameCodeASC, setIsFullNameCodeASC] = useState(true);
  const [isTypeASC, setIsTypeASC] = useState(true);
  const [staffCodeSelected, setStaffCodeSelected] = useState("");
  const [NameSelected, setNameSelected] = useState("");
  const [staffCode, setStaffCode] = useState("");
  const [assetCode, setAssetCode] = useState("");
  const [assetCodeSelected, setAssetCodeSelected] = useState("");
  const [assetNameSelected, setAssetNameSelected] = useState("");
  const [loadNotDone, setLoadNotDone] = useState(true);

  const history = useHistory();

  const handleSelectUser = () => {
    setUsername(NameSelected);
    setIsOpenUserDialog(false);
    setStaffCode(staffCodeSelected);
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
  const handleSortType = () => {
    if (isTypeASC) {
      setSort("roleASC");
      setIsTypeASC(!isTypeASC);
    } else {
      setSort("roleDES");
      setIsTypeASC(!isTypeASC);
    }
  };
  const [isAssetCodeASC, setIsAssetCodeASC] = useState(false);
  const handleSortByAssetCode = () => {
    if (isAssetCodeASC) {
      setAssetSort("assetCodeASC");
      setIsAssetCodeASC(!isAssetCodeASC);
    } else {
      setAssetSort("assetCodeDES");
      setIsAssetCodeASC(!isAssetCodeASC);
    }
  };
  const [isAssetNameASC, setIsAssetNameASC] = useState(false);
  const handleSortByAssetName = () => {
    if (isAssetNameASC) {
      setAssetSort("assetNameASC");
      setIsAssetNameASC(!isAssetNameASC);
    } else {
      setAssetSort("assetNameDES");
      setIsAssetNameASC(!isAssetNameASC);
    }
  };
  const [isCategoryASC, setIsCategoryASC] = useState(false);
  const handleSortByCategory = () => {
    if (isCategoryASC) {
      setAssetSort("categoryASC");
      setIsCategoryASC(!isCategoryASC);
    } else {
      setAssetSort("categoryDES");
      setIsCategoryASC(!isCategoryASC);
    }
  };

  useEffect(() => {
    const fetchUsers = async () => {
      const url = `user/assignment?page=${currentPage}&size=16&sort=${sort}&search=isDeleted:false,staffCode:${staffCodeSearch},username:${usernameSearch},location:${localStorage.getItem(
        "locationId"
      )}`;
      const response = await get(url);
      setUsers(response.data.data.data);
      setTotalPage(response.data.data.totalPages);
    };
    fetchUsers();
  }, [usernameSearch, staffCodeSearch, currentPage, sort]);

  const [currentAssetPage, setCurrentAssetPage] = useState(0);
  const [assetSort, setAssetSort] = useState("assetNameASC");
  const [nameAssetSearch, setNameAssetSearch] = useState("");
  const [assetCodeSearch, setAssetCodeSearch] = useState("");
  const [assets, setAssets] = useState([]);
  const [totalAssetPage, setTotalAssetPage] = useState(0);
  useEffect(() => {
    const fetchAsset = async () => {
      const url = `asset/assetInAssignment?page=${currentAssetPage}&size=10&sort=${assetSort}&search=isDeleted:false,state:1,assetName:${nameAssetSearch},assetCode:${assetCodeSearch},location:${localStorage.getItem(
        "locationId"
      )}`;
      const response = await get(url);
      setAssets(response.data.data.data);
      setTotalAssetPage(response.data.data.totalPages);
    };
    fetchAsset();
  }, [currentAssetPage, assetSort, nameAssetSearch, assetCodeSearch]);
  const handleUserSearch = (e) => {
    e.preventDefault();
    setCurrentPage(0);
    if (boxusersearch.toUpperCase().startsWith("SD")) {
      setStaffCodeSearch(boxusersearch.toUpperCase());
      setUsernameSearch("");
    } else {
      setUsernameSearch(boxusersearch);
      setStaffCodeSearch("");
    }
  };

  var date = new Date();

  var day = date.getDate();
  var month = date.getMonth() + 1;
  var year = date.getFullYear();

  if (month < 10) month = "0" + month;
  if (day < 10) day = "0" + day;

  var today = year + "-" + month + "-" + day + "T00:00:00";
  var date = year + "-" + month + "-" + day;
  const handleEditAssignment = async () => {
    const url = "assignment";
    let repsonse;
    try {
      repsonse = await put(url, {
        assignedToUserId: staffCode,
        assetCode: assetCode,
        assignedDate: assignedDate + "T00:00:00",
        note: note.trim(),
        assignmentId: parseInt(assignmentId),
      });
    } catch (error) {
      alert(error.response.data.errorCode);
    }
    localStorage.setItem("editAssignment", JSON.stringify(repsonse.data.data));
    history.push(URL.MANAGE_ASSIGNMENT);
  };
  const [assetSearch, setAssetSearch] = useState("");

  // load data
  let { assignmentId } = useParams();
  useEffect(() => {
    const fetchAssignment = async () => {
      const url = `assignment/${assignmentId}`;
      let response = undefined;
      try {
        response = await get(url);
      } catch (error) {
        if (error.response.data.errorCode === "ERR_ASSIGNMENT_ID_NOT_FOUND") {
          history.push(URL.MANAGE_ASSIGNMENT);
        }
      }
      if (response !== undefined) {
        if (response.data.data.state !== STATE.WAITING_FOR_ACCEPTANCE) history.push(URL.MANAGE_ASSIGNMENT);
        let assignment = response.data.data;
        setStaffCode(assignment.assignedTo.staffCode);
        setUsername(`${assignment.assignedTo.firstName} ${assignment.assignedTo.lastName}`);
        setAsset(assignment.asset.assetName);
        setAssetCode(assignment.asset.assetCode);
        setNote(assignment.note);
        setAssignedDate(assignment.assignedDate <= today ? date : assignment.assignedDate.split("T")[0]);
        setLoadNotDone(false);
      }
    };
    fetchAssignment();
  }, []);
  return (
    <div id="Form-edit-assignment">
      <Dialog
        id="user_dialog"
        open={isOpenUserDialog}
        TransitionComponent={Transition}
        keepMounted
        disableEscapeKeyDown
        disableBackdropClick
        onClose={() => setIsOpenUserDialog(false)}
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <div id="dialog-title-edit-assignment">
          <div id="name-dialog-edit-assignment">
            <b>Select User</b>
          </div>
          <div className="">
            <input className="search-input-edit-assignment" onChange={(e) => setBoxUserSearch(e.target.value)} />
            <SearchIcon id="search_user_icon-edit-assignment" onClick={handleUserSearch} />
          </div>
        </div>
        <div>
          <table id="users-table">
            <thead>
              <tr className="tr-edit-assignment">
                <th className="th-edit-assignment"></th>
                <th className="th-edit-assignment" onClick={handleSortByStaffCode} className="users-header">
                  Staff Code
                  {isStaffCodeASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
                </th>
                <th className="th-edit-assignment" onClick={handleSortByFullName} className="users-header">
                  Full Name
                  {isFullNameCodeASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
                </th>
                <th className="th-edit-assignment" onClick={handleSortType} className="users-header">
                  Type
                  {isTypeASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
                </th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => {
                return (
                  <tr className="tr-edit-assignment" id={user.staffCode}>
                    <input
                      type="radio"
                      name="user"
                      checked={staffCodeSelected === user.staffCode ? true : false}
                      onChange={() => {
                        setStaffCodeSelected(user.staffCode);
                        setNameSelected(user.fullName);
                      }}
                    />
                    <td className="user-item">{user.staffCode}</td>
                    <td className="user-item">{user.fullName}</td>
                    <td className="user-item">{user.role.includes("ADMIN") ? "Admin" : "Staff"}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
        <Paginations
          totalPages={totalPage}
          setCurrentPage={(currentPage) => {
            setCurrentPage(currentPage);
          }}
          className="pagination-edit-assignment"
        />
        <div id="button-zone1-edit-assignment">
          <Button
            variant="contained"
            color="secondary"
            size="medium"
            id="bt_save-edit-assignment"
            onClick={handleSelectUser}
            disabled={staffCodeSelected === "" ? true : false}
          >
            Save
          </Button>
          <Button variant="outlined" color="secondary" size="medium" onClick={() => setIsOpenUserDialog(false)}>
            Cancel
          </Button>
        </div>
      </Dialog>
      <Dialog
        id="asset_dialog"
        open={isOpenAssetDialog}
        TransitionComponent={Transition}
        keepMounted
        disableEscapeKeyDown
        disableBackdropClick
        onClose={() => setIsOpenAssetDialog(false)}
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <div id="dialog-title-edit-assignment">
          <div id="name-dialog-edit-assignment">
            <b>Select Asset</b>
          </div>
          <div className="">
            <input className="search-input-edit-assignment" onChange={(e) => setAssetSearch(e.target.value)} />
            <SearchIcon
              id="search_user_icon-edit-assignment"
              onClick={() => {
                if (assetSearch.length <= 2) {
                  setAssetCodeSearch(assetSearch.toUpperCase());
                  setNameAssetSearch("");
                } else if (!isNaN(assetSearch[2])) {
                  setAssetCodeSearch(assetSearch);
                  setNameAssetSearch("");
                } else {
                  setNameAssetSearch(assetSearch);
                  setAssetCodeSearch("");
                }
              }}
            />
          </div>
        </div>
        <table id="asset-table" className="table-edit-assignment">
          <thead>
            <tr className="tr-edit-assignment">
              <th className="th-edit-assignment"></th>
              <th className="th-edit-assignment" onClick={handleSortByAssetCode} className="asset-header">
                Asset Code
                {isAssetCodeASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
              </th>
              <th className="th-edit-assignment" onClick={handleSortByAssetName} className="asset-header">
                Asset Name
                {isAssetNameASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
              </th>
              <th className="th-edit-assignment" onClick={handleSortByCategory} className="asset-header">
                Category
                {isCategoryASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
              </th>
            </tr>
          </thead>
          <tbody>
            {assets.map((asset) => {
              return (
                <tr className="tr-edit-assignment" id={asset.code}>
                  <input
                    type="radio"
                    name="asset"
                    checked={assetCodeSelected === asset.code ? true : false}
                    onChange={() => {
                      setAssetCodeSelected(asset.code);
                      setAssetNameSelected(asset.name);
                    }}
                  />
                  <td className="asset-item">{asset.code}</td>
                  <td className="asset-item">{asset.name}</td>
                  <td className="asset-item">{asset.category}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
        <Paginations
          totalPages={totalAssetPage}
          setCurrentPage={(currentPage) => {
            setTotalAssetPage(currentPage);
          }}
          className="pagination"
        />

        <div id="button-zone2-edit-assignment">
          <Button
            variant="contained"
            color="secondary"
            size="medium"
            id="bt_save-edit-assignment"
            onClick={() => {
              setIsOpenAssetDialog(false);
              setAsset(assetNameSelected);
              setAssetCode(assetCodeSelected);
            }}
            disabled={assetCodeSelected === "" ? true : false}
          >
            Save
          </Button>
          <Button variant="outlined" color="secondary" size="medium" onClick={() => setIsOpenAssetDialog(false)}>
            Cancel
          </Button>
        </div>
      </Dialog>
      <h5 id="title_form-edit-assignment">
        <b>Edit Assignment</b>
      </h5>
      <div id="form-edit-assignment-2">
        <div className="field-edit-assignment">
          <label className="edit-assignment-label">User</label>
          <input disabled value={username} className="input-field-edit-assignment" />
          <SearchIcon
            id="search_icon-edit-assignment"
            onClick={() => {
              setStaffCodeSelected(staffCode);
              setNameSelected(username);
              setIsOpenUserDialog(true);
            }}
          />
        </div>
        <div className="field-edit-assignment">
          <label className="edit-assignment-label">Asset</label>
          <input value={asset} className="input-field-edit-assignment" disabled />
          <SearchIcon
            id="search_icon-edit-assignment"
            onClick={() => {
              setAssetCodeSelected(assetCode);
              setAssetNameSelected(asset);
              setIsOpenAssetDialog(true);
            }}
          />
        </div>
        <div className="field-edit-assignment">
          <label className="edit-assignment-label">Assigned Date</label>
          <input
            type="date"
            min={date}
            className="input-field-edit-assignment"
            value={assignedDate}
            onChange={(e) => {
              setAssignedDate(e.target.value);
            }}
          />
        </div>
        <div className="field-edit-assignment">
          <label className="edit-assignment-label">Note</label>
          <textarea
            id="note-edit-assignment"
            className="input-field-edit-assignment textarea-edit-assignment"
            rows="5"
            maxLength="250"
            value={note}
            onChange={(e) => setNote(e.target.value)}
          />
        </div>
      </div>
      <div id="button-zone-edit-assignment">
        {username === "" || asset === "" || assignedDate === "" || loadNotDone ? (
          <Button variant="contained" color="secondary" size="medium" id="bt_save-edit-assignment" disabled>
            Save
          </Button>
        ) : (
          <Button variant="contained" color="secondary" size="medium" id="bt_save-edit-assignment" onClick={handleEditAssignment}>
            Save
          </Button>
        )}

        <Button
          variant="outlined"
          color="secondary"
          size="medium"
          onClick={() => {
            history.push("/manageassignment");
          }}
        >
          Cancel
        </Button>
      </div>
    </div>
  );
};

export default EditAssignment;
