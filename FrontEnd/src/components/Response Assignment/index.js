import React from "react";
import { put } from "../../httpHelper";
import './ResponseAssignment.css'

export const AcceptAssignment = (props) => {
    const handleConfirmAcceptAssignment = () => {
        console.log(props.assignmentId);
        let Url=`assignment/accept/${props.assignmentId}`;
        put(Url)
            .then(() => {
                alert("Accept Assignment OK!");
                props.handleAcceptAssignmentShow();
            })
            .catch(response => console.log(response))
    }

    const handleCancelAcceptAssignment = () => {
        props.handleAcceptAssignmentShow();
    }

    return (
        <div id="accept-assignment" className={!props.isShowAcceptAssignment ? "show-response-assignment" : ""}>
            <div className="response-assignment-header">
                <p>Are you sure?</p>
            </div>
            <div className="response-assignment-body">
                <p>Do you want to accept this assignment?</p>
                <div className="button-flex">
                    <button className="accept-btn" onClick={handleConfirmAcceptAssignment}>Accept</button>
                    <button className="cancel-btn" onClick={handleCancelAcceptAssignment}>Cancel</button>
                </div>
            </div>
        </div>
    )
}

export const DeclineAssignment = (props) => {
    const handleConfirmDeclineAssignment = () => {
        let Url=`assignment/decline/${props.assignmentId}`;
        put(Url)
            .then(() => {
                alert("Decline Assignment OK!");
                props.handleDeclineAssignmentShow();
            })
            .catch(response => console.log(response))
    }

    const handleCancelDeclineAssignment = () => {
        props.handleDeclineAssignmentShow();
    }

    return (
        <div id="accept-assignment" className={!props.isShowDeclineAssignment ? "show-response-assignment" : ""}>
            <div className="response-assignment-header">
                <p>Are you sure?</p>
            </div>
            <div className="response-assignment-body">
                <p>Do you want to decline this assignment?</p>
                <div className="button-flex">
                    <button className="decline-btn" onClick={handleConfirmDeclineAssignment}>Decline</button>
                    <button className="cancel-btn" onClick={handleCancelDeclineAssignment}>Cancel</button>
                </div>
            </div>
        </div>
    )
}