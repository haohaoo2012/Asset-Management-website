import React, { useState } from "react";
import "./user.css";
import useStyles from "./styles";
import { useHistory } from "react-router";
import { Dialog, Slide, Button } from "@material-ui/core";
import { get, del } from "../../../httpHelper";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});
const Index = ({ user, onDelete }) => {
  const [openUpdateDialog, setOpenUpdateDialog] = useState(false);
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [openDeleteNotice, setOpenDeleteNotice] = useState(false);

  const classes = useStyles();
  const history = useHistory();

  const handleCloseUpdateForm = () => {
    setOpenUpdateDialog(false);
  };
  const handleOpenUpdateForm = () => {
    setOpenUpdateDialog(true);
  };
  const handleCloseDeleteNotice = () => {
    setOpenDeleteNotice(false);
  };
  const handleOpenDeleteNotice = () => {
    setOpenDeleteNotice(true);
  };
  const handleCloseDeleteDialog = () => {
    setOpenDeleteDialog(false);
  };
  const handleOpenDeleteForm = () => {
    setOpenDeleteDialog(true);
  };
  const showUserDetail = () => {
    handleOpenUpdateForm();
  };

  const handleEditIconClick = () => {
    history.push(`/edituser/${user.staffCode}`);
  };

  const handleDeleteUser = async () => {
    const repsone = await get(`user/checkHaveAssignment/${user.staffCode}`);
    if (repsone.data.data !== true) {
      handleOpenDeleteForm();
    } else {
      handleOpenDeleteNotice();
    }
  };

  const handleDisableUser = async () => {
    const url = `user/${user.staffCode}`;
    del(url);
    onDelete(user.staffCode);
    handleCloseDeleteDialog();
  };
  return (
    <>
      <Dialog
        id="detailed_user"
        open={openUpdateDialog}
        TransitionComponent={Transition}
        keepMounted
        disableEscapeKeyDown
        disableBackdropClick
        onClose={handleCloseUpdateForm}
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <div id="dialog-title" id="detail_title">
          <h5> Detailed User Information</h5>
          <Button onClick={handleCloseUpdateForm} color="primary" size="small">
            X
          </Button>
        </div>
        <div className={classes.dialogContent}>
          <div className={classes.title}>
            <div>Staff Code</div>
            <div>Full Name</div>
            <div>Username</div>
            <div>Date Of Birth</div>
            <div>Gender</div>
            <div>Joined Date</div>
            <div>Type</div>
            <div>Location</div>
          </div>
          <div>
            <div>{user.staffCode}</div>
            <div>{user.fullName}</div>
            <div>{user.username}</div>
            <div>{user.dateOfBirth}</div>
            <div>{user.gender}</div>
            <div>{user.joinedDate}</div>
            <div>
              {user.type.includes("USER")
                ? "STAFF"
                : user.type.replace("ROLE_", "")}
            </div>
            <div>{user.location}</div>
          </div>
        </div>
      </Dialog>
      <tr id={user.staffCode}>
        <td onClick={showUserDetail} className="userItem">
          {user.staffCode}
        </td>
        <td>&nbsp;</td>
        <td onClick={showUserDetail} className="userItem">
          {user.fullName}
        </td>
        <td>&nbsp;</td>
        <td onClick={showUserDetail} className="userItem">
          {user.username}
        </td>
        <td>&nbsp;</td>
        <td onClick={showUserDetail} className="userItem">
          {user.joinedDate}
        </td>
        <td>&nbsp;</td>
        <td onClick={showUserDetail} className="userItem">
          {user.type.includes("USER") ? "Staff" : "Admin"}
        </td>
        <td>&nbsp;</td>
        <td id="icon_zone" className="buttonUser">
          <div id="edit_icon" class="edit_icon" onClick={handleEditIconClick}>
            <svg class="edit_icon_kh" viewBox="0 0 16.001 16">
              <path
                id="edit_icon_kh"
                d="M 12.85400009155273 0.1459999978542328 C 12.65875053405762 -0.04919072985649109 12.34224987030029 -0.04919090867042542 12.14700031280518 0.1459997892379761 L 10.5 1.792999982833862 L 14.20699977874756 5.5 L 15.85400009155273 3.854000091552734 C 15.94804191589355 3.760195732116699 16.00089263916016 3.632827281951904 16.00089263916016 3.5 C 16.00089263916016 3.367172718048096 15.94804191589355 3.239804267883301 15.85400009155273 3.146000146865845 L 12.85400009155273 0.1460001468658447 Z M 13.5 6.206999778747559 L 9.793000221252441 2.5 L 3.292999982833862 9 L 3.5 9 C 3.776142358779907 9 4 9.223857879638672 4 9.5 L 4 10 L 4.5 10 C 4.776142597198486 10 5 10.22385787963867 5 10.5 L 5 11 L 5.5 11 C 5.776142597198486 11 6 11.22385787963867 6 11.5 L 6 12 L 6.5 12 C 6.776142597198486 12 7 12.22385787963867 7 12.5 L 7 12.70699977874756 L 13.5 6.206999778747559 Z M 6.032000064849854 13.67499923706055 C 6.010957717895508 13.61904811859131 6.000119686126709 13.55977725982666 5.999999523162842 13.5 L 6 13 L 5.5 13 C 5.223857402801514 13 5 12.77614212036133 5 12.5 L 5 12 L 4.5 12 C 4.223857402801514 12 4 11.77614212036133 4 11.5 L 4 11 L 3.5 11 C 3.223857641220093 11 3 10.77614212036133 3 10.5 L 3 10 L 2.5 10 C 2.440221071243286 9.999902725219727 2.380945920944214 9.98906421661377 2.325000286102295 9.968001365661621 L 2.146000146865845 10.14600086212158 C 2.098350048065186 10.19398593902588 2.060928821563721 10.25113868713379 2.03600025177002 10.31400108337402 L 0.03600025177001953 15.31400108337402 C -0.03834319114685059 15.49973201751709 0.005179375410079956 15.71189785003662 0.1466411948204041 15.85336017608643 C 0.288102924823761 15.99482154846191 0.5002692937850952 16.03834342956543 0.6860000491142273 15.9640007019043 L 5.686000347137451 13.9640007019043 C 5.748862743377686 13.93907165527344 5.806015014648438 13.90165138244629 5.854000568389893 13.85400104522705 L 6.032000541687012 13.67600059509277 Z"
              ></path>
            </svg>
          </div>
          <div>
            <div
              onClick={handleDeleteUser}
              id="x-circle-fill"
              class="x_circle_fill"
            >
              <svg class="x-circle-fill_kj" viewBox="0 0 16 16">
                <path
                  id="x-circle-fill_kj"
                  d="M 16 8 C 16 12.41827774047852 12.41827774047852 16 7.999999523162842 16 C 3.581721782684326 16 0 12.41827774047852 0 7.999999523162842 C 0 3.581722259521484 3.581721782684326 4.76837158203125e-07 7.999999523162842 0 C 12.41827774047852 0 16 3.581721782684326 16 7.999999523162842 Z M 5.354000091552734 4.645999908447266 C 5.158491134643555 4.450490951538086 4.841508865356445 4.450490951538086 4.645999908447266 4.645999908447266 C 4.450490951538086 4.841508865356445 4.450490951538086 5.158491134643555 4.645999908447266 5.354000091552734 L 7.293000221252441 8 L 4.645999908447266 10.64599990844727 C 4.450490951538086 10.84150791168213 4.450490951538086 11.15849113464355 4.645999908447266 11.35400009155273 C 4.841508388519287 11.54950904846191 5.158491134643555 11.54950904846191 5.354000091552734 11.35400009155273 L 8 8.706999778747559 L 10.64599990844727 11.35400009155273 C 10.84150886535645 11.54950904846191 11.15849113464355 11.54950904846191 11.35400009155273 11.35400009155273 C 11.54950904846191 11.15849113464355 11.54950904846191 10.84150886535645 11.35400009155273 10.64599990844727 L 8.706999778747559 8 L 11.35400009155273 5.354000091552734 C 11.54950904846191 5.158490657806396 11.54950904846191 4.841508388519287 11.35400009155273 4.645999431610107 C 11.15849113464355 4.450490951538086 10.84150791168213 4.450490951538086 10.64599990844727 4.646000385284424 L 8 7.293000221252441 L 5.354000091552734 4.645999908447266 Z"
                ></path>
              </svg>
            </div>
            <Dialog
              open={openDeleteDialog}
              TransitionComponent={Transition}
              keepMounted
              disableEscapeKeyDown
              disableBackdropClick
              onClose={handleCloseDeleteDialog}
              aria-labelledby="alert-dialog-slide-title"
              aria-describedby="alert-dialog-slide-description"
            >
              <div id="Form_jo">
                <div id="header_jp">Are you sure?</div>
                <div id="background_jq">
                  <div id="First_Name_jr">
                    Do you want to disable this user?
                  </div>
                  <div id="button_zone">
                    <Button
                      variant="contained"
                      color="secondary"
                      size="small"
                      id="delete_btn"
                      onClick={handleDisableUser}
                    >
                      Delete
                    </Button>
                    <Button
                      size="small"
                      id="cancel_btn"
                      onClick={handleCloseDeleteDialog}
                    >
                      Cancel
                    </Button>
                  </div>
                </div>
              </div>
            </Dialog>
            <Dialog
              open={openDeleteNotice}
              TransitionComponent={Transition}
              keepMounted
              disableEscapeKeyDown
              disableBackdropClick
              onClose={handleCloseDeleteNotice}
              aria-labelledby="alert-dialog-slide-title"
              aria-describedby="alert-dialog-slide-description"
            >
              <div id="form_notice">
                <div id="header_notice">
                  <h4>Can not disable user</h4>
                  <button onClick={handleCloseDeleteNotice} id="cancel_notice">
                    X
                  </button>
                </div>
                <div id="content">
                  <div>There are valid assignments belongs to this user.</div>
                  <div>Plese close all assignments before disabling user.</div>
                </div>
              </div>
            </Dialog>
          </div>
        </td>
      </tr>
    </>
  );
};

export default Index;
