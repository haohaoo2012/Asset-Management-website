import React, { useEffect, useState } from "react";
import { useCookies } from 'react-cookie';
import "./content.css";
import useStyles from "./Style";
import { useHistory } from "react-router";
import { Dialog, Slide, Button } from "@material-ui/core";
import EditIcon from "@material-ui/icons/Edit";
import HighlightOffIcon from "@material-ui/icons/HighlightOff";
import RefreshIcon from '../../../images/refresh-icon.jpg';
import ReplayIcon from "@material-ui/icons/Replay";
import {get, del } from "../../../httpHelper";
import { red } from "@material-ui/core/colors";
import { ContactSupportOutlined, LensTwoTone } from "@material-ui/icons";
import { ReturnRequest } from "../../Return Request";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

const Index = ({ assignment, currentPage, size, ReloadAssignment }) => {
  const [openUpdateDialog, setOpenUpdateDialog] = useState(false);
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [isShowReturnRequest, setIsShowReturnRequest] = useState(false);
  const [isDisabled, setIsDisable] = useState(false);

  const [cookies, setCookie] = useCookies(['user']);

  const [state, setState] = useState();
  const classes = useStyles();
  const history = useHistory();

  useEffect(() => {
    console.log(assignment);
    switch (assignment.state) {
      case 1:
        setState("Accepted");
        break;
      case 2:
        setState("Wating for acceptance");
        break;
      case 3:
        setState("Decline");
        break;
      default:
        break;
    }
  }, [assignment]);

  const handleCloseUpdateForm = () => {
    setOpenUpdateDialog(false);
  };
  const handleOpenUpdateForm = () => {
    setOpenUpdateDialog(true);
  };
  const handleCloseDeleteDialog = () => {
    setOpenDeleteDialog(false);
  };
  const handleOpenDeleteForm = () => {
    setOpenDeleteDialog(true);
  };
  const showAssignmentDetail = () => {
    handleOpenUpdateForm();
  };

  const handleEditIconClick = () => {
    history.push(`/edit-assignment/${assignment.assignmentId}`);
  };
  const CallState = assignment.state;
  const handleDeleteAssignment = async () => {
    let url = `assignment/${assignment.assignmentId}`;
    await del(url)
    const url2 = `assignment?page=${currentPage}&size=${size}`;
    await get(url2).then((response) => {
      ReloadAssignment(response.data.data);
    });
    handleCloseDeleteDialog();
};
const handleOpenFormDelete =() => {
  if( assignment.state!=true){
    setIsDisable(true);
    handleOpenDeleteForm();
    }
  else {
    setIsDisable(false);
  }
};

  const handleReturnRequestShow = (assignmentId) => {
    if (assignmentId === undefined) {
      let url = `assignment?page=${currentPage}&size=${size}`;
      get(url).then((response) => {
        ReloadAssignment(response.data.data);
        });
    }
    setIsShowReturnRequest(!isShowReturnRequest);
  }

  return (
    <>
      <Dialog
        id="detailed_assignment"
        open={openUpdateDialog}
        TransitionComponent={Transition}
        keepMounted
        disableEscapeKeyDown
        disableBackdropClick
        onClose={handleCloseUpdateForm}
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <div className="dialog-title">
          <p>Detailed Assignment Information</p>
          <button className="close-btn" onClick={handleCloseUpdateForm}>
            X
          </button>
        </div>
        <div className="dialog-content">
          <div className="dialog-content-title">
            <div>Asset Code</div>
            <div>Asset Name</div>
            <div>Specification</div>
            <div>Assigned to</div>
            <div>Assigned by</div>
            <div>Assigned Date</div>
            <div>State</div>
            <div>Note</div>
          </div>
          <div className="dialog-content-data">
            <div>{assignment.asset.assetCode}</div>
            <div>{assignment.asset.assetName}</div>
            <div>{assignment.asset.specification}</div>
            <div>{assignment.assignedTo.username}</div>
            <div>{assignment.assignedBy.username}</div>
            <div>{assignment.assignedDate.slice(0, 10)}</div>
            <div>{state}</div>
            <div>{assignment.note}</div>
          </div>
        </div>
      </Dialog>
      <tr id={assignment.assignmentId}>
        <td onClick={showAssignmentDetail}>{assignment.assignmentId}</td>
        <td onClick={showAssignmentDetail}>{assignment.asset.assetCode}</td>
        <td onClick={showAssignmentDetail}>{assignment.asset.assetName}</td>
        <td onClick={showAssignmentDetail}>{assignment.assignedTo.username}</td>
        <td onClick={showAssignmentDetail}>{assignment.assignedBy.username}</td>
        <td onClick={showAssignmentDetail}>{assignment.assignedDate.slice(0, 10)}</td>
        <td onClick={showAssignmentDetail}>{state}</td>

        <div id="icon_zone">
          <div id="edit_icon">
            {assignment.state === 2 ? (
              <svg class="edit_icon_xp" viewBox="0 0 16.001 16" onClick={() => handleEditIconClick()}>
                <path
                  id="edit_icon_xp"
                  d="M 12.85400009155273 0.1459999978542328 C 12.65875053405762 -0.04919072985649109 12.34224987030029 -0.04919090867042542 12.14700031280518 0.1459997892379761 L 10.5 1.792999982833862 L 14.20699977874756 5.5 L 15.85400009155273 3.854000091552734 C 15.94804191589355 3.760195732116699 16.00089263916016 3.632827281951904 16.00089263916016 3.5 C 16.00089263916016 3.367172718048096 15.94804191589355 3.239804267883301 15.85400009155273 3.146000146865845 L 12.85400009155273 0.1460001468658447 Z M 13.5 6.206999778747559 L 9.793000221252441 2.5 L 3.292999982833862 9 L 3.5 9 C 3.776142358779907 9 4 9.223857879638672 4 9.5 L 4 10 L 4.5 10 C 4.776142597198486 10 5 10.22385787963867 5 10.5 L 5 11 L 5.5 11 C 5.776142597198486 11 6 11.22385787963867 6 11.5 L 6 12 L 6.5 12 C 6.776142597198486 12 7 12.22385787963867 7 12.5 L 7 12.70699977874756 L 13.5 6.206999778747559 Z M 6.032000064849854 13.67499923706055 C 6.010957717895508 13.61904811859131 6.000119686126709 13.55977725982666 5.999999523162842 13.5 L 6 13 L 5.5 13 C 5.223857402801514 13 5 12.77614212036133 5 12.5 L 5 12 L 4.5 12 C 4.223857402801514 12 4 11.77614212036133 4 11.5 L 4 11 L 3.5 11 C 3.223857641220093 11 3 10.77614212036133 3 10.5 L 3 10 L 2.5 10 C 2.440221071243286 9.999902725219727 2.380945920944214 9.98906421661377 2.325000286102295 9.968001365661621 L 2.146000146865845 10.14600086212158 C 2.098350048065186 10.19398593902588 2.060928821563721 10.25113868713379 2.03600025177002 10.31400108337402 L 0.03600025177001953 15.31400108337402 C -0.03834319114685059 15.49973201751709 0.005179375410079956 15.71189785003662 0.1466411948204041 15.85336017608643 C 0.288102924823761 15.99482154846191 0.5002692937850952 16.03834342956543 0.6860000491142273 15.9640007019043 L 5.686000347137451 13.9640007019043 C 5.748862743377686 13.93907165527344 5.806015014648438 13.90165138244629 5.854000568389893 13.85400104522705 L 6.032000541687012 13.67600059509277 Z"
                ></path>
              </svg>
            ) : (
              <svg class="edit_icon_xl" viewBox="0 0 16.001 16">
                <path
                  id="edit_icon_xl"
                  d="M 12.85400009155273 0.1459999978542328 C 12.65875053405762 -0.04919072985649109 12.34224987030029 -0.04919090867042542 12.14700031280518 0.1459997892379761 L 10.5 1.792999982833862 L 14.20699977874756 5.5 L 15.85400009155273 3.854000091552734 C 15.94804191589355 3.760195732116699 16.00089263916016 3.632827281951904 16.00089263916016 3.5 C 16.00089263916016 3.367172718048096 15.94804191589355 3.239804267883301 15.85400009155273 3.146000146865845 L 12.85400009155273 0.1460001468658447 Z M 13.5 6.206999778747559 L 9.793000221252441 2.5 L 3.292999982833862 9 L 3.5 9 C 3.776142358779907 9 4 9.223857879638672 4 9.5 L 4 10 L 4.5 10 C 4.776142597198486 10 5 10.22385787963867 5 10.5 L 5 11 L 5.5 11 C 5.776142597198486 11 6 11.22385787963867 6 11.5 L 6 12 L 6.5 12 C 6.776142597198486 12 7 12.22385787963867 7 12.5 L 7 12.70699977874756 L 13.5 6.206999778747559 Z M 6.032000064849854 13.67499923706055 C 6.010957717895508 13.61904811859131 6.000119686126709 13.55977725982666 5.999999523162842 13.5 L 6 13 L 5.5 13 C 5.223857402801514 13 5 12.77614212036133 5 12.5 L 5 12 L 4.5 12 C 4.223857402801514 12 4 11.77614212036133 4 11.5 L 4 11 L 3.5 11 C 3.223857641220093 11 3 10.77614212036133 3 10.5 L 3 10 L 2.5 10 C 2.440221071243286 9.999902725219727 2.380945920944214 9.98906421661377 2.325000286102295 9.968001365661621 L 2.146000146865845 10.14600086212158 C 2.098350048065186 10.19398593902588 2.060928821563721 10.25113868713379 2.03600025177002 10.31400108337402 L 0.03600025177001953 15.31400108337402 C -0.03834319114685059 15.49973201751709 0.005179375410079956 15.71189785003662 0.1466411948204041 15.85336017608643 C 0.288102924823761 15.99482154846191 0.5002692937850952 16.03834342956543 0.6860000491142273 15.9640007019043 L 5.686000347137451 13.9640007019043 C 5.748862743377686 13.93907165527344 5.806015014648438 13.90165138244629 5.854000568389893 13.85400104522705 L 6.032000541687012 13.67600059509277 Z"
                ></path>
              </svg>
            )}
          </div>
           <div onClick={handleOpenFormDelete}  disabled={isDisabled} id="x-circle-fill" class="x_circle_fill">
            <HighlightOffIcon style={{ marginLeft: "15px", color: `${CallState != true? "#DF0101" : "#FA5882" }` }} />
          </div>
          <Dialog open={openDeleteDialog} TransitionComponent={Transition} keepMounted disableEscapeKeyDown disableBackdropClick
            onClose={handleCloseDeleteDialog}
            aria-labelledby="alert-dialog-slide-title"
            aria-describedby="alert-dialog-slide-description" >
            <div id="Form_jo">
              <div id="header_jp" ><b>Are you sure?</b></div>
              <div id="background_jq">
                <div id="First_Name_jr">
                  Do you want to delete this Assignment?
                </div>
                <div id="button_zone-deleteAssignment">
                  <Button variant="contained" color="secondary" size="small"id="deleteAssignment_btn" onClick={handleDeleteAssignment}> Delete</Button>
                 <Button size="small" outline color="secondary" id="btncancelAssignment" onClick={handleCloseDeleteDialog}>Cancel </Button>
                
                </div>
              </div>
            </div>
          </Dialog>    
          <div>
           <button className="assign-click-button" disabled={assignment.state !== 1 ? true : assignment.requests.length > 0 ? true : false}
              onClick={() => handleReturnRequestShow(assignment.assignmentId)}>
                <img
                    src={RefreshIcon}
                    className={assignment.state !== 1 || assignment.requests.length > 0 ? 'assign-icon-disabled' : ""}
                    alt="refresh-icon"
                    width="14px"
                />
            </button>
          </div>
        </div>
      </tr>
      <ReturnRequest assignmentId={assignment.assignmentId} staffCode={cookies.user.staffCode} isShowReturnRequest={isShowReturnRequest} handleReturnRequestShow={handleReturnRequestShow}/>
    </>
  );
};

export default Index;
