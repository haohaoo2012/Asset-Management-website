import React, { useState, useEffect } from "react";
import searchIcon from "../../images/search.png";
import { MenuItem, Select, FormControl } from "@material-ui/core";
import { InputLabel } from "@material-ui/core";
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import ArrowDropUpIcon from "@material-ui/icons/ArrowDropUp";
import { useHistory } from "react-router";
import { get, post } from "../../httpHelper";
import "./Assignment.css";
import Assign from "./Assign";
import Paginations from "./Pagination/Pagination";
import * as STATE from "../../constants/State";
import { Dropdown, DropdownMenu, DropdownToggle, Input, Label, } from "reactstrap";
  import { Table } from 'reactstrap';

let timeOfCallUseEffect = 0;
const Assignment = () => {
  const history = useHistory();

  const [dropdownOpen, setDropdownOpen] = useState(false);
  const toggle = () => setDropdownOpen((prevState) => !prevState);
  const [assignments, setAssignments] = useState([]);
  const [size, setSize] = useState(15);
  const [currentPage, setCurrentPage] = useState(0);
  const [searchKeyWord, setSearchKeyWord] = useState("");
  const [sortField, setSortField] = useState("assignedDate");
  const [sortType, setSortType] = useState("DSC");
  const [AssetCode, setAssetCode] = useState("");
  const [isNoASC, setIsNoASC] = useState(true);
  const [isAssetCodeASC, setIsAssetCodeASC] = useState(true);
  const [isAssetNameASC, setIsAssetNameASC] = useState(true);
  const [isAssignedtoASC, setIsAssignedtoASC] = useState(true);
  const [isAssignedbyASC, setIsAssignedbyASC] = useState(true);
  const [isAssignedDateASC, setIsAssignedDateASC] = useState(true);
  const [isStateASC, setIsStateASC] = useState(true);
  //state
  const [stateList, setStateList] = useState([]);
  const [stateAll, setStateAll] = useState(false);
  const [stateAccepted, setStateAccepted] = useState(false);
  const [stateWaiting, setStateWaiting] = useState(false);
  const [stateDeclined, setStateDeclined] = useState(false);
  //date
  const [localDateTime, setLocalDateTime] = useState("");
  const [tempList, setTempList] = useState([]);

  const handleClickCreateNew = () => {
    history.push("/creatnewasssignment");
  };

  const handleOnClickSearchButton = (e) => {
    e.preventDefault();
  };
  const ReloadAssignment = (res) => {
    setAssignments(res);
  };

  const loadNewOrEditData = (AssignmentListTemp) => {
    let newAssignment = localStorage.getItem("newAssignment");
    let editAssignment = localStorage.getItem("editAssignment");
    let AssignmentList = JSON.parse(JSON.stringify(AssignmentListTemp));
    if (newAssignment !== null) {
      newAssignment = JSON.parse(newAssignment);
      AssignmentList = AssignmentList.filter((e) => e.assignmentId !== newAssignment.assignmentId);
      if(AssignmentList.length===AssignmentListTemp.length){
        AssignmentList.pop()
      }
      AssignmentList.unshift(newAssignment);
      setAssignments(AssignmentList);
      // localStorage.removeItem("newAssignment");
    } else if (editAssignment !== null) {
      editAssignment = JSON.parse(editAssignment);
      AssignmentList = AssignmentList.filter((e) => e.assignmentId !== editAssignment.assignmentId);
      if(AssignmentList.length===AssignmentListTemp.length){
        AssignmentList.pop()
      }
      AssignmentList.unshift(editAssignment);
      setAssignments(AssignmentList);
      // localStorage.removeItem("editAssignment");
    } else {
      setAssignments(AssignmentList);
    }
    if (newAssignment !== null || editAssignment !== null) {
      timeOfCallUseEffect++;
      if (timeOfCallUseEffect >= 2) {
        removeLocalStorageAfterShowToFirst();
        timeOfCallUseEffect = 0;
      }
    }
  };

  const removeLocalStorageAfterShowToFirst = () => {
    localStorage.removeItem("newAssignment");
    localStorage.removeItem("editAssignment");
  };

  useEffect(() => {
    const fetchAssignments = async () => {
      const url = `assignment?page=${currentPage}&size=${size}`;
      get(url).then((response) => {
        setAssignments(response.data.data);
      });
    };
    fetchAssignments();
  }, []);

  useEffect(() => {
    const url = `assignment/filter-search-sort?page=${currentPage}&size=${size}`;
    const data = {
      states: stateList,
      localDateTime: localDateTime === "" ? localDateTime : new Date(localDateTime).toISOString().slice(0, 19),
      sortField,
      sortType,
      searchKeyWord,
    };
    post(url, data)
      .then((response) => {
        loadNewOrEditData(response.data.data);
      })
      .catch((err) => console.log(err));
  }, [stateList, localDateTime, sortField, sortType]);

  const handleSortByNo = () => {
    if (isNoASC) {
      setSortField("no");
      setSortType("ASC");
      setIsNoASC(!isNoASC);
    } else {
      setSortField("no");
      setSortType("DSC");
      setIsNoASC(!isNoASC);
    }
  };
  const handleSortByAssetCode = () => {
    if (isAssetCodeASC) {
      setSortField("assetCode");
      setSortType("ASC");
      setIsAssetCodeASC(!isAssetCodeASC);
    } else {
      setSortField("assetCode");
      setSortType("DSC");
      setIsAssetCodeASC(!isAssetCodeASC);
    }
  };

  const handleSortByAssetName = () => {
    if (isAssetNameASC) {
      setSortField("assetName");
      setSortType("ASC");
      setIsAssetNameASC(!isAssetNameASC);
    } else {
      setSortField("assetName");
      setSortType("DSC");
      setIsAssetNameASC(!isAssetNameASC);
    }
  };
  const handleSortByAssignedto = () => {
    if (isAssignedtoASC) {
      setSortField("assignedTo");
      setSortType("ASC");
      setIsAssignedtoASC(!isAssignedtoASC);
    } else {
      setSortField("assignedTo");
      setSortType("DSC");
      setIsAssignedtoASC(!isAssignedtoASC);
    }
  };

  const handleSortByAssignedby = () => {
    if (isAssignedtoASC) {
      setSortField("assignedBy");
      setSortType("ASC");
      setIsAssignedbyASC(!isAssignedbyASC);
    } else {
      setSortField("assignedBy");
      setSortType("DSC");
      setIsAssignedbyASC(!isAssignedbyASC);
    }
  };
  const handleSortByAssignedDate = () => {
    if (isAssignedDateASC) {
      setSortField("assignedDate");
      setSortType("ASC");
      setIsAssignedDateASC(!isAssignedDateASC);
    } else {
      setSortField("assignedDate");
      setSortType("DSC");
      setIsAssignedDateASC(!isAssignedDateASC);
    }
  };
  const handleSortByState = () => {
    if (isStateASC) {
      setSortField("state");
      setSortType("ASC");
      setIsStateASC(!isStateASC);
    } else {
      setSortField("state");
      setSortType("DSC");
      setIsStateASC(!isStateASC);
    }
  };

  const handleSeachKeyWord = () => {
    const url = `assignment/filter-search-sort?page=${currentPage}&size=${size}`;
    const data = {
      states: stateList,
      localDateTime: localDateTime === "" ? localDateTime : new Date(localDateTime).toISOString().slice(0, 19),
      sortField,
      sortType,
      searchKeyWord,
    };
    console.log(data);
    post(url, data)
      .then((response) => {
        setAssignments(response.data.data);
      })
      .catch((err) => console.log(err));
  };

  useEffect(() => {
    const list = [];
    const tempList = [];
    if (stateAccepted) {
      list.push(STATE.ACCEPTED);
    } else {
      if (stateWaiting) {
        tempList.push(STATE.WAITING_FOR_ACCEPTANCE);
      }
      if (stateDeclined) {
        tempList.push(STATE.DECLINED);
      }
    }
    if (stateWaiting) {
      list.push(STATE.WAITING_FOR_ACCEPTANCE);
    } else {
      if (stateAccepted) {
        tempList.push(STATE.ACCEPTED);
      }
      if (stateDeclined) {
        tempList.push(STATE.DECLINED);
      }
    }
    if (stateDeclined) {
      list.push(STATE.DECLINED);
    } else {
      if (stateAccepted) {
        tempList.push(STATE.ACCEPTED);
      }
      if (stateWaiting) {
        tempList.push(STATE.WAITING_FOR_ACCEPTANCE);
      }
    }

    if (!stateAccepted || !stateWaiting || !stateDeclined) {
      if (stateAll) {
        setStateAll(false);
      }
    }
    if (stateAccepted && stateWaiting && stateDeclined) {
      if (!stateAll) {
        setStateAll(true);
      }
    }
    setTempList(tempList);
    setStateList(list);
  }, [stateAccepted, stateDeclined, stateWaiting]);

  useEffect(() => {
    if (stateAll) {
      if (!stateAccepted) setStateAccepted(true);
      if (!stateWaiting) setStateWaiting(true);
      if (!stateDeclined) setStateDeclined(true);
      setTempList([]);
    } else {
      if (tempList.length <= 0) {
        if (stateAccepted) setStateAccepted(false);
        if (stateWaiting) setStateWaiting(false);
        if (stateDeclined) setStateDeclined(false);
      } else {
        tempList.forEach((e) => {
          if (e === STATE.ACCEPTED) {
            if (!stateAccepted) setStateAccepted(true);
          }
          if (e === STATE.WAITING_FOR_ACCEPTANCE) {
            if (!stateWaiting) setStateWaiting(true);
          }
          if (e === STATE.DECLINED) {
            if (!stateDeclined) setStateDeclined(true);
          }
        });
        setTempList([]);
      }
    }
  }, [stateAll]);

  return (
    <div id="assgignemts">
    <p className="title">Assignment List</p>
    <Dropdown isOpen={dropdownOpen} toggle={toggle} id="Filter_state-manageAssignment" class="Filter_state-manageAssignment">
      <DropdownToggle tag="span" data-toggle="dropdown" aria-expanded={dropdownOpen} id="Group_199-manageAssignment">
        <div>
          <svg class="Rectangle_336_hh-request">
            <rect id="Rectangle_336_hh-request" rx="5" ry="5" x="0" y="0" width="210" height="35"></rect>
          </svg>
          <svg class="Line_5_hi-request" viewBox="0 0 1 33">
            <path id="Line_5_hi-request" d="M 0 0 L 0 33"></path>
          </svg>
        </div>
        <svg class="funnel-fill-request" viewBox="1.5 1 13 13">
          <path
            id="funnel-fill-request"
            d="M 1.49999988079071 1.464298129081726 C 1.49999988079071 1.207873582839966 1.723857641220093 0.9999999403953552 2.000000238418579 1 L 14.00000286102295 1 C 14.27614307403564 1 14.5 1.207873582839966 14.5 1.464298129081726 L 14.5 3.32149076461792 C 14.5 3.43591046333313 14.45443630218506 3.546375513076782 14.37205982208252 3.631580114364624 L 10.00000190734863 8.142763137817383 L 10.00000190734863 12.60745429992676 C 9.999837875366211 12.80721282958984 9.86209774017334 12.98448371887207 9.658001899719238 13.047607421875 L 6.658000946044922 13.97620391845703 C 6.505598545074463 14.02334022521973 6.338087558746338 13.99961280822754 6.207751274108887 13.91242599487305 C 6.077413558959961 13.82524013519287 6.000123023986816 13.68521404266357 6.000000476837158 13.53604984283447 L 6.000000476837158 8.142763137817383 L 1.628000020980835 3.63164210319519 C 1.545608043670654 3.546451568603516 1.500022053718567 3.435994148254395 1.49999988079071 3.32149076461792 L 1.49999988079071 1.464298129081726 Z"
          ></path>
        </svg>
        <div id="State_hk-request">
          <span>State</span>
        </div>
      </DropdownToggle>
      <DropdownMenu>
        <div className="state-option-manageAssignment">
          <Label check>
            <input id="state-check-manageAssignment"  type="checkbox" checked={stateAll} onChange={() => setStateAll(!stateAll)} /> All
          </Label>
        </div>
        <div className="state-option-manageAssignment">
          <Label check>
          <input id="state-check-manageAssignment" type="checkbox"  checked={stateAccepted} onChange={() => setStateAccepted(!stateAccepted)} /> Accepted
          </Label>
        </div>
        <div className="state-option-manageAssignment">
          <Label check>
          <input id="state-check-manageAssignment" type="checkbox"  checked={stateWaiting} onChange={() => setStateWaiting(!stateWaiting)} /> Waiting for acceptance
          </Label>
        </div>
        <div className="state-option-manageAssignment">
          <Label check>
          <input id="state-check-manageAssignment" type="checkbox"  checked={stateDeclined} onChange={() => setStateDeclined(!stateDeclined)} /> Declined
          </Label>
        </div>
      </DropdownMenu>
    </Dropdown>
      <input id="date-manageAssingment" type="date" className="date" value={localDateTime} onChange={(e) => setLocalDateTime(e.target.value)} />
      <div id="Search_bar-manageAssingment">
      <Input type="text" id="input-search-request" value={searchKeyWord} onChange={(e) => setSearchKeyWord(e.target.value)} />
      <svg class="Line_5-request" viewBox="0 0 1 33">
        <path id="Line_5-request" d="M 0 0 L 0 33"></path>
      </svg>
      <svg class="search-request" viewBox="-0.001 0.002 16.001 15.998" onClick={handleSeachKeyWord}>
        <path
          id="search-request"
          d="M 11.74199962615967 10.3439998626709 C 13.75034236907959 7.603466033935547 13.3092155456543 3.77855372428894 10.72976493835449 1.567129611968994 C 8.150315284729004 -0.6442947387695313 4.30290412902832 -0.4960470199584961 1.90126895904541 1.907307624816895 C -0.5003666877746582 4.310660362243652 -0.6458611488342285 8.158176422119141 1.567407608032227 10.73604393005371 C 3.780675888061523 13.31391143798828 7.605902671813965 13.75230312347412 10.34499931335449 11.74200057983398 L 10.34399890899658 11.74199962615967 C 10.37399864196777 11.7819995880127 10.40599918365479 11.81999969482422 10.44199848175049 11.85699939727783 L 14.2919979095459 15.70699882507324 C 14.68246269226074 16.09774017333984 15.31575584411621 16.09796333312988 15.70649719238281 15.70749855041504 C 16.09723854064941 15.3170337677002 16.09746170043945 14.68374061584473 15.70699787139893 14.29299926757813 L 11.85699844360352 10.4429988861084 C 11.82124614715576 10.4068078994751 11.78280258178711 10.37337875366211 11.74199867248535 10.34299850463867 Z M 12 6.5 C 12 9.537567138671875 9.537566184997559 12 6.5 12 C 3.4624342918396 12 1.000000476837158 9.537566184997559 1.000000476837158 6.5 C 1.000000476837158 3.4624342918396 3.462434530258179 1.000000476837158 6.500000953674316 1.000000476837158 C 9.537567138671875 1.000000476837158 12 3.462434530258179 12 6.500000953674316 Z"
        ></path>
      </svg>
    </div>
      <div id="right_search_bar">
        <button id="create_button-manageAssignment" onClick={handleClickCreateNew}>
          Create new Assignment
        </button>
    </div>
    <Table>
      <thead>
        <tr>
          <th onClick={handleSortByNo}>
            No.
            {isNoASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
          </th>
          <th  onClick={handleSortByAssetCode}>
            Asset Code
            {isAssetCodeASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
          </th>
          <th onClick={handleSortByAssetName}>
            Asset Name
            {isAssetNameASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
          </th>
          <th onClick={handleSortByAssignedto}>
            Assigned to
            {isAssignedtoASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
          </th>
          <th onClick={handleSortByAssignedby}>
            Assigned by
            {isAssignedbyASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
          </th>
          <th onClick={handleSortByAssignedDate}>
            Assigned Date
            {isAssignedDateASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
          </th>
          <th onClick={handleSortByState}>
            State
            {isStateASC ? <ArrowDropDownIcon /> : <ArrowDropUpIcon />}
          </th>
        </tr>
      </thead>
        <tbody>
          {assignments.map((assignment) => {
            return <Assign ReloadAssignment={ReloadAssignment} currentPage={currentPage} size={size} assignment={assignment} />;
          })}
        </tbody>
      </Table>
      <Paginations setCurrentPage={(current) => setCurrentPage(current)} className="pagination" />
    </div>
  );
};

export default Assignment;
