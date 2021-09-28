import React, { useState, useEffect } from "react";
import "./ManageRequest.css";
import { Table } from "reactstrap";
import { Dropdown, DropdownMenu, DropdownToggle, Input, Label, Modal, ModalHeader, ModalBody, Button, Alert } from "reactstrap";
import DropdownIcon from "../../images/dropdown-icon.png";
import TickIcon from "../../images/tick-icon.svg";
import XIcon from "../../images/x-icon.png";
import Paginations from "./Pagination";
import * as STATE from "../../constants/State";
import * as URL from "../../constants/URL.js";
import { put } from "../../httpHelper";
import { getRequest, countRequest, getRequestFilterSearchSort, countRequestFilterSearchSort } from "../../services/RequestService";

const Index = () => {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const toggle = () => setDropdownOpen((prevState) => !prevState);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [pageSize, setPageSize] = useState(15);
  const [searchKeyWord, setSearchKeyWord] = useState("");
  const [assignedDate, setAssignedDate] = useState("");
  const [stateAll, setStateAll] = useState(false);
  const [stateWaitingForReturning, setStateWaitingForReturning] = useState(false);
  const [stateCompleted, setStateCompleted] = useState(false);
  const [stateList, setStateList] = useState([]);
  const [stateListTemp, setStateListTemp] = useState([]);
  const [sortField, setSortField] = useState("");
  const [sortType, setSortType] = useState("");
  const [requestList, setRequestList] = useState([]);
  const [reRenderPagination, setReRenderPagination] = useState(Math.random() + "abcxyz");
  const [modalComplete, setModalComplete] = useState(false);
  const [requestId, setRequestId] = useState("");
  const [isCompleteFail, setIsCompleteFail] = useState(false);
  const [messageFail, setMessageFail] = useState("");
  const [modalCancel, setModalCancel] = useState(false);

  useEffect(() => {
    // console.log(searchKeyWord);
    // console.log(assignedDate);
    // console.log(stateList);
    // console.log(sortField);
    // console.log(sortType);
    // console.log(currentPage);
    loadRequestTable();
  }, [stateList, assignedDate, sortField, sortType, currentPage]);

  const loadRequestTable = async () => {
    let usingFilterSearchSortApi = stateList.length > 0 || searchKeyWord.trim() !== "" || assignedDate !== "" || sortField !== "" || sortType !== "";
    if (usingFilterSearchSortApi) {
      let filterSearchSort = {
        states: stateList,
        localDateTime: assignedDate !== "" ? `${assignedDate}T00:00:00` : assignedDate,
        sortField: sortField,
        sortType: sortType,
        searchKeyWord: searchKeyWord,
      };
      try {
        let result = await countRequestFilterSearchSort(URL.COUNT_REQUEST_FILTER_SEARCH_SORT_API, filterSearchSort);
        loadTotalPages(result.data.data.numberOfEntity);
      } catch (error) {
        alert(error.response.data.errorCode);
        return;
      }
      try {
        let result = await getRequestFilterSearchSort(`${URL.GET_REQUEST_FILTER_SEARCH_SORT_API}?page=${currentPage}&size=${pageSize}`, filterSearchSort);
        setRequestList(result.data.data);
      } catch (error) {
        alert(error.response.data.errorCode);
        return;
      }
    } else {
      try {
        let result = await countRequest(URL.COUNT_REQUEST_API);
        loadTotalPages(result.data.data.numberOfEntity);
      } catch (error) {
        alert(error.response.data.errorCode);
        return;
      }
      try {
        let result = await getRequest(`${URL.GET_REQUEST_API}?page=${currentPage}&size=${pageSize}`);
        setRequestList(result.data.data);
      } catch (error) {
        alert(error.response.data.errorCode);
        return;
      }
    }
  };

  const loadTotalPages = (numberOfObject) => {
    let totalPages = 1;
    if (numberOfObject / pageSize === 0) {
      totalPages = 1;
    } else if (numberOfObject % pageSize === 0) {
      totalPages = numberOfObject / pageSize;
    } else {
      totalPages = Math.floor(numberOfObject / pageSize) + 1;
    }
    setTotalPages(totalPages);
    if (currentPage + 1 > totalPages && currentPage + 1 !== 1) {
      setCurrentPage(0);
      setReRenderPagination(Math.random() + "abcxyz");
    }
  };

  const handleSort = (incomeSortField) => {
    if (sortField !== incomeSortField) {
      setSortField(incomeSortField);
      setSortType("ASC");
    } else {
      if (sortType === "ASC") {
        setSortType("DSC");
      } else {
        setSortType("ASC");
      }
    }
  };

  useEffect(() => {
    if (stateAll) {
      if (!stateWaitingForReturning) {
        setStateWaitingForReturning(true);
      }
      if (!stateCompleted) {
        setStateCompleted(true);
      }
    } else {
      if (stateListTemp.length > 0) {
        stateListTemp.forEach((e) => {
          if (e === STATE.WAITING_FOR_RETURNING) {
            if (!stateWaitingForReturning) {
              setStateWaitingForReturning(true);
            }
          } else if (e === STATE.COMPLETED) {
            if (!stateCompleted) {
              setStateCompleted(true);
            }
          }
        });
        setStateListTemp([]);
      } else {
        if (stateCompleted) {
          setStateCompleted(false);
        }
        if (stateWaitingForReturning) {
          setStateWaitingForReturning(false);
        }
      }
    }
  }, [stateAll]);

  useEffect(() => {
    let incomeStateList = [];
    let stateListTemp = [];
    if (stateWaitingForReturning) {
      incomeStateList.push(STATE.WAITING_FOR_RETURNING);
    } else {
      if (stateCompleted) {
        stateListTemp.push(STATE.COMPLETED);
      }
    }
    if (stateCompleted) {
      incomeStateList.push(STATE.COMPLETED);
    } else {
      if (stateWaitingForReturning) {
        stateListTemp.push(STATE.WAITING_FOR_RETURNING);
      }
    }
    if (!stateCompleted || !stateWaitingForReturning) {
      if (stateAll) {
        setStateAll(false);
      }
    }
    if (stateCompleted && stateWaitingForReturning) {
      if (!stateAll) {
        setStateAll(true);
      }
    }
    setStateListTemp(stateListTemp);
    if (JSON.stringify(incomeStateList) !== JSON.stringify(stateList)) setStateList(incomeStateList);
  }, [stateWaitingForReturning, stateCompleted]);

  const searchRequest = (e) => {
    if (e.keyCode === 13) {
      loadRequestTable();
    }
  };

  const removeDatePickerValue = (e) => {
    if (e.keyCode === 27) {
      if (assignedDate !== "") setAssignedDate("");
    }
  };

  const removeDatePickerRightClick = (e) => {
    e.preventDefault();
    if (assignedDate !== "") setAssignedDate("");
  };

  const toggleShowComplete = (requestId) => {
    setModalComplete(!modalComplete);
    setRequestId(requestId);
  };

  const toggleComplete = () => {
    setModalComplete(!modalComplete);
    setRequestId("");
    setIsCompleteFail(false);
  };

  const handleCompleteRequest = () => {
    let url = `request/complete/${requestId}`;
    let body = {};
    put(url, body)
      .then((response) => {
        if (response.status === 200) {
          if (response.data.successCode === "REQUEST_COMPLETE_SUCCESS") {
            toggleComplete();
            loadRequestTable();
          }
        }
      })
      .catch((err) => {
        if (err.response) {
          if (err.response.data.errorCode === "ERR_REQUEST_NOT_FOUND") {
            setMessageFail("Request not found.");
          } else if (err.response.data.errorCode === "ERR_REQUEST_ALREADY_COMPLETE") {
            setMessageFail("Request already complete.");
          } else if (err.response.data.errorCode === "ERR_USER_NOT_FOUND") {
            setMessageFail("User not found.");
          } else {
            setMessageFail("Error to complete request.");
          }
        } else {
          setMessageFail("Fail to complete request.");
        }
        setIsCompleteFail(true);
      });
  };

  const handleCancelRequest = (requestId) => {
    console.log("cancel");
  };
  const toggleShowCancel = (requestId) => {
    setModalCancel(!modalCancel);
    setRequestId(requestId);
  };

  const toggleCancel = () => {
    setModalCancel(!modalCancel);
    setIsCompleteFail(false);
  };
  const handleCanceltoRequest = () => {
    let url = `request/cancel/${requestId}`;
    let body = {};
    put(url, body)
      .then((response) => {
        if (response.status === 200) {
          if (response.data.successCode === "REQUEST_CANCEL_SUCCESS") {
            toggleCancel();
            loadRequestTable();
          }
        }
      })
      .catch((err) => {
        if (err.response) {
          if (err.response.data.errorCode === "ERR_REQUEST_NOT_FOUND") {
            setMessageFail("Request not found.");
          } else if (err.response.data.errorCode === "ERR_REQUEST_ALREADY_COMPLETE") {
            setMessageFail("Request already complete.");
          } else if (err.response.data.errorCode === "ERR_USER_NOT_FOUND") {
            setMessageFail("User not found.");
          }
        } else {
          setMessageFail("Fail to cancel request.");
        }
        setIsCompleteFail(true);
      });
  };
  return (
    <div>
      <div id="Request_List-request">
        <span>Request List</span>
      </div>
      <Dropdown isOpen={dropdownOpen} toggle={toggle} id="Filter_state-request" class="Filter_state-request">
        <DropdownToggle tag="span" data-toggle="dropdown" aria-expanded={dropdownOpen} id="Group_199-request">
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
          {/* <div onClick={toggle}>Custom dropdown item</div> */}
          <div className="state-option-request">
            <Label check>
              <Input type="checkbox" checked={stateAll} onChange={() => setStateAll((prevState) => !prevState)} /> All
            </Label>
          </div>
          <div className="state-option-request">
            <Label check>
              <Input type="checkbox" checked={stateCompleted} onChange={() => setStateCompleted((prevState) => !prevState)} /> Completed
            </Label>
          </div>
          <div className="state-option-request">
            <Label check>
              <Input type="checkbox" checked={stateWaitingForReturning} onChange={() => setStateWaitingForReturning((prevState) => !prevState)} /> Waiting for
              returning
            </Label>
          </div>
        </DropdownMenu>
      </Dropdown>
      <div id="Filter_assigned_date-request" class="Filter_assigned_date-request">
        <div id="Group_199_h-request">
          <label>Returned Date</label>
          <Input
            type="date"
            id="input-date-request"
            value={assignedDate}
            onChange={(e) => setAssignedDate(e.target.value)}
            onKeyUp={(e) => removeDatePickerValue(e)}
            onContextMenu={(e) => removeDatePickerRightClick(e)}
          />
        </div>
      </div>
      <div id="Search_bar-request">
        <Input type="text" id="input-search-request" onChange={(e) => setSearchKeyWord(e.target.value)} onKeyUp={(e) => searchRequest(e)} />
        {/* <svg class="Rectangle_336-request">
          <rect id="Rectangle_336-request" rx="5" ry="5" x="0" y="0" width="255" height="35"></rect>
        </svg> */}
        <svg class="Line_5-request" viewBox="0 0 1 33">
          <path id="Line_5-request" d="M 0 0 L 0 33"></path>
        </svg>
        <svg class="search-request" viewBox="-0.001 0.002 16.001 15.998" onClick={loadRequestTable}>
          <path
            id="search-request"
            d="M 11.74199962615967 10.3439998626709 C 13.75034236907959 7.603466033935547 13.3092155456543 3.77855372428894 10.72976493835449 1.567129611968994 C 8.150315284729004 -0.6442947387695313 4.30290412902832 -0.4960470199584961 1.90126895904541 1.907307624816895 C -0.5003666877746582 4.310660362243652 -0.6458611488342285 8.158176422119141 1.567407608032227 10.73604393005371 C 3.780675888061523 13.31391143798828 7.605902671813965 13.75230312347412 10.34499931335449 11.74200057983398 L 10.34399890899658 11.74199962615967 C 10.37399864196777 11.7819995880127 10.40599918365479 11.81999969482422 10.44199848175049 11.85699939727783 L 14.2919979095459 15.70699882507324 C 14.68246269226074 16.09774017333984 15.31575584411621 16.09796333312988 15.70649719238281 15.70749855041504 C 16.09723854064941 15.3170337677002 16.09746170043945 14.68374061584473 15.70699787139893 14.29299926757813 L 11.85699844360352 10.4429988861084 C 11.82124614715576 10.4068078994751 11.78280258178711 10.37337875366211 11.74199867248535 10.34299850463867 Z M 12 6.5 C 12 9.537567138671875 9.537566184997559 12 6.5 12 C 3.4624342918396 12 1.000000476837158 9.537566184997559 1.000000476837158 6.5 C 1.000000476837158 3.4624342918396 3.462434530258179 1.000000476837158 6.500000953674316 1.000000476837158 C 9.537567138671875 1.000000476837158 12 3.462434530258179 12 6.500000953674316 Z"
          ></path>
        </svg>
      </div>

      <Table id="request-table-request">
        <thead>
          <tr>
            <th style={{ cursor: "pointer" }} id="Component_10__1706" onClick={() => handleSort("no")}>
              <span>No.</span>
              {sortField === "no" && sortType === "DSC" ? (
                <img src={DropdownIcon} width="12px" />
              ) : (
                <img className="sort-revert-request" src={DropdownIcon} width="12px" />
              )}
            </th>
            <th style={{ cursor: "pointer" }} id="Component_10__1705" onClick={() => handleSort("assetCode")}>
              <span>Asset Code</span>
              {sortField === "assetCode" && sortType === "DSC" ? (
                <img src={DropdownIcon} width="12px" />
              ) : (
                <img className="sort-revert-request" src={DropdownIcon} width="12px" />
              )}
            </th>
            <th style={{ cursor: "pointer" }} id="Component_10__1704" onClick={() => handleSort("assetName")}>
              <span>Asset Name</span>
              {sortField === "assetName" && sortType === "DSC" ? (
                <img src={DropdownIcon} width="12px" />
              ) : (
                <img className="sort-revert-request" src={DropdownIcon} width="12px" />
              )}
            </th>
            <th style={{ cursor: "pointer" }} id="Component_10__1707" onClick={() => handleSort("requestedBy")}>
              <span>Requested by</span>
              {sortField === "requestedBy" && sortType === "DSC" ? (
                <img src={DropdownIcon} width="12px" />
              ) : (
                <img className="sort-revert-request" src={DropdownIcon} width="12px" />
              )}
            </th>
            <th style={{ cursor: "pointer" }} id="Component_10__1708" onClick={() => handleSort("assignedDate")}>
              <span>Assigned Date</span>
              {sortField === "assignedDate" && sortType === "DSC" ? (
                <img src={DropdownIcon} width="12px" />
              ) : (
                <img className="sort-revert-request" src={DropdownIcon} width="12px" />
              )}
            </th>
            <th style={{ cursor: "pointer" }} id="Component_10__1710" onClick={() => handleSort("acceptedBy")}>
              <span>Accepted by</span>
              {sortField === "acceptedBy" && sortType === "DSC" ? (
                <img src={DropdownIcon} width="12px" />
              ) : (
                <img className="sort-revert-request" src={DropdownIcon} width="12px" />
              )}
            </th>
            <th style={{ cursor: "pointer" }} id="Component_10__1709" onClick={() => handleSort("returnedDate")}>
              <span>Returned Date</span>
              {sortField === "returnedDate" && sortType === "DSC" ? (
                <img src={DropdownIcon} width="12px" />
              ) : (
                <img className="sort-revert-request" src={DropdownIcon} width="12px" />
              )}
            </th>
            <th style={{ cursor: "pointer" }} id="Component_10__2934" onClick={() => handleSort("state")}>
              <span>State</span>
              {sortField === "state" && sortType === "DSC" ? (
                <img src={DropdownIcon} width="12px" />
              ) : (
                <img className="sort-revert-request" src={DropdownIcon} width="12px" />
              )}
            </th>
            <th></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {requestList.map((e) => (
            <tr id={e.requestId} key={e.requestId + "-" + e.state}>
              <td>{e.requestId}</td>
              <td>{e.assignment.asset.assetCode}</td>
              <td>{e.assignment.asset.assetName}</td>
              <td>{e.requestedBy.username}</td>
              <td>{new Intl.DateTimeFormat("en-GB").format(new Date(e.assignment.assignedDate.split("T")[0]))}</td>
              <td>{e.acceptedBy !== null ? e.acceptedBy.username : ""}</td>
              <td>{e.returnedDate !== null ? new Intl.DateTimeFormat("en-GB").format(new Date(e.returnedDate.split("T")[0])) : ""}</td>
              <td>{e.state === STATE.WAITING_FOR_RETURNING ? "Waiting for returning" : e.state === STATE.COMPLETED ? "Completed" : ""}</td>

              {e.state === STATE.WAITING_FOR_RETURNING ? (
                <td onClick={() => toggleShowComplete(e.requestId)}>
                  <img src={TickIcon} width="16px" />
                </td>
              ) : (
                <td>
                  <img src={TickIcon} width="16px" style={{ opacity: "0.3" }} />
                </td>
              )}
              {e.state === STATE.WAITING_FOR_RETURNING ? (
                <td onClick={() => toggleShowCancel(e.requestId)}>
                  <img src={XIcon} width="16px" />
                </td>
              ) : (
                <td>
                  <img src={XIcon} width="16px" style={{ opacity: "0.3" }} />
                </td>
              )}
            </tr>
          ))}
        </tbody>
        <br />
        <Paginations
          key={reRenderPagination}
          id={reRenderPagination}
          totalPages={totalPages}
          setCurrentPage={(current) => setCurrentPage(current)}
          defaultPage={currentPage + 1}
        />
      </Table>
      <Modal isOpen={modalComplete} toggle={toggleComplete} style={{ marginTop: "100px" }}>
        <ModalHeader
          style={{
            backgroundColor: "rgba(239,241,245,1)",
            color: "rgba(207, 35, 56, 1)",
            paddingLeft: "50px",
            borderBottom: "1px solid #000",
          }}
        >
          Are you sure?
        </ModalHeader>
        <ModalBody>
          <div style={{ marginBottom: "20px", marginLeft: "8px" }}>Do you want to mark this returning request as 'Completed'?</div>
          <div style={{ marginLeft: "8px" }}>
            <Button color="danger" onClick={handleCompleteRequest}>
              Yes
            </Button>
            <Button outline color="secondary" style={{ marginLeft: "16px" }} onClick={toggleComplete}>
              No
            </Button>
          </div>
          {isCompleteFail === true && (
            <div style={{ marginTop: "16px" }}>
              <Alert color="danger">{messageFail}</Alert>
            </div>
          )}
        </ModalBody>
      </Modal>
      <Modal isOpen={modalCancel} toggle={toggleCancel}>
        <ModalHeader style={{ backgroundColor: "rgba(239,241,245,1)", color: "rgba(207, 35, 56, 1)", paddingLeft: "50px", borderBottom: "1px solid #000" }}>
          <b> Are you sure?</b>
        </ModalHeader>
        <ModalBody>
          <div style={{ marginBottom: "20px", marginLeft: "8px" }}>Do you want to cancel this returning request?</div>
          <div style={{ marginLeft: "8px" }}>
            <Button color="danger" onClick={handleCanceltoRequest}>
              Yes{" "}
            </Button>
            <Button outline color="secondary" style={{ marginLeft: "16px" }} onClick={toggleCancel}>
              {" "}
              No{" "}
            </Button>
          </div>
          {isCompleteFail === true && (
            <div style={{ marginTop: "16px" }}>
              <Alert color="danger">{messageFail}</Alert>
            </div>
          )}
        </ModalBody>
      </Modal>
    </div>
  );
};
export default Index;
