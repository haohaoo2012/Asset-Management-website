import React from "react";
import { post } from "../../httpHelper";
import './ReturnRequest.css'

export const ReturnRequest = (props) => {
    const handleConfirmReturnRequest = () => {
        console.log(props.assignmentId);
        console.log(props.staffCode);
        let Url="request/save";
        let data = {
            assignment: props.assignmentId,
            requestedBy: props.staffCode
        }
        post(Url, data)
            .then(() => {
                alert("Return Request OK!");
                props.handleReturnRequestShow();
            })
            .catch(response => console.log(response))
    }

    const handleCancelReturnRequest = () => {
        props.handleReturnRequestShow();
    }

    return (
        <div id="returning-request" className={!props.isShowReturnRequest ? "show-return-request" : ""}>
            <div className="return-request-header">
                <p>Are you sure?</p>
            </div>
            <div className="return-request-body">
                <p>Do you want to create a returning request for this asset?</p>
                <div className="button-flex">
                    <button className="yes-btn" onClick={handleConfirmReturnRequest}>Yes</button>
                    <button className="no-btn" onClick={handleCancelReturnRequest}>No</button>
                </div>
            </div>
        </div>
    )
}