import React, { Component } from 'react';
import "./OwnAssignment.css";
import { Table, Button, Modal, ModalHeader, ModalBody, ModalFooter, Alert } from 'reactstrap';
import { instanceOf } from "prop-types";
import { withCookies, Cookies } from "react-cookie";
import { post, get } from "../../httpHelper";
import OwnAssignmentDetail from './OwnAssignmentDetail';
import DropdownIcon from '../../images/dropdown-icon.png';
import TickIcon from '../../images/tick-icon.svg';
import XIcon from '../../images/x-icon.png';
import RefreshIcon from '../../images/refresh-icon.jpg';
import { AcceptAssignment, DeclineAssignment } from '../Response Assignment';
import { ReturnRequest } from '../Return Request';

class index extends Component {

    static propTypes = {
        cookies: instanceOf(Cookies).isRequired,
    };

    constructor(props) {
        super(props);
        this.state = {
            user: this.props.cookies.get("user") || "",

            ownAssignments: [],
            pageList: [],

            pageNum: 1,
            pageSize: 15,
            totalPages: 1,
            orderBy: '',
            typeOrder: 'DESC',

            assignmentId: '',
            modal: false,

            isFail: false,
            messageFail: '',

            isShowAcceptAssignment: false,
            isShowDeclineAssignment: false,
            isShowReturnRequest: false,

            responseAssignmentId: ""
        };
    }

    componentDidMount() {
        this.loadOwnAssignments();
    }

    loadOwnAssignments() {
        let url = 'assignment/own';
        let body = {
            pageNum: this.state.pageNum,
            pageSize: this.state.pageSize,
            staffCode: this.state.user.staffCode,
            orderBy: this.state.orderBy,
            typeOrder: this.state.typeOrder,
        }
        post(url, body)
            .then(response => {
                if (response.status === 200) {
                    if (response.data.successCode === 'LOAD_OWN_ASSIGNMENT_SUCCESS') {
                        this.setState({
                            ownAssignments: response.data.data.ownAssignments,
                            totalPages: response.data.data.totalPages,
                            isFail: false
                        })
                        this.handlePageList(response);
                        console.log(response.data.data.ownAssignments);
                    }
                }
            })
            .catch(err => {
                if(err.response){
                    if(err.response.data.errorCode === 'USER_NOT_FOUND'){
                        this.setState({messageFail: 'User not found.'});
                    }
                    else if(err.response.data.errorCode === 'USER_IS_DISABLED'){
                        this.setState({messageFail: 'User is disabled.'});
                    }
                    else{
                        this.setState({messageFail: 'Error to load own assignments.'})
                    }
                }
                else{
                    this.setState({messageFail: 'Fail to load own assignments.'})
                }
                this.setState({isFail: true})
            })
    }

    handlePageList(response) {
        var list = [];
        for (let i = 0; i < response.data.data.totalPages; i++) {
            list.push(i + 1);
        }
        if (list.length > 1) {
            this.setState({ pageList: list });
        }
        else {
            this.setState({ pageList: [] });
        }
    }

    toggleShowButton(assignmentId) {
        this.setState({
            assignmentId: assignmentId,
            modal: !this.state.modal
        })
    }

    toggleShow() {
        this.setState({
            assignmentId: '',
            modal: !this.state.modal
        })
    }

    orderByAssetCode() {
        this.setState({
            orderBy: 'assetCode',
            typeOrder: this.state.typeOrder === 'ASC' ? 'DESC' : 'ASC'
        }, () => {
            this.loadOwnAssignments();
        })
    }

    orderByAssetName() {
        this.setState({
            orderBy: 'assetName',
            typeOrder: this.state.typeOrder === 'ASC' ? 'DESC' : 'ASC'
        }, () => {
            this.loadOwnAssignments();
        })
    }

    orderByCategory() {
        this.setState({
            orderBy: 'category',
            typeOrder: this.state.typeOrder === 'ASC' ? 'DESC' : 'ASC'
        }, () => {
            this.loadOwnAssignments();
        })
    }

    orderByState() {
        this.setState({
            orderBy: 'state',
            typeOrder: this.state.typeOrder === 'ASC' ? 'DESC' : 'ASC'
        }, () => {
            this.loadOwnAssignments();
        })
    }

    orderByAssignedDate() {
        this.setState({
            orderBy: '',
            typeOrder: this.state.typeOrder === 'ASC' ? 'DESC' : 'ASC'
        }, () => {
            this.loadOwnAssignments();
        })
    }

    changPage(page) {
        this.setState({pageNum: page}, () => {
            this.loadOwnAssignments();
        })
    }

    handleAcceptAssignmentShow = (assignmentId) => {
        if (assignmentId === undefined) {
            this.loadOwnAssignments();
        } 
        this.setState({
            isShowAcceptAssignment: !this.state.isShowAcceptAssignment,
            isShowDeclineAssignment: false,
            isShowReturnRequest: false,
            responseAssignmentId: assignmentId
        })
    }

    handleDeclineAssignmentShow = (assignmentId) => {
        if (assignmentId === undefined) {
            this.loadOwnAssignments();
        }
        this.setState({
            isShowAcceptAssignment: false,
            isShowReturnRequest: false,
            isShowDeclineAssignment: !this.state.isShowDeclineAssignment,
            responseAssignmentId: assignmentId
        })
    }

    handleReturnRequestShow = (assignmentId) => {
        if (assignmentId === undefined) {
            this.loadOwnAssignments();
        }
        this.setState({
            isShowReturnRequest: !this.state.isShowReturnRequest,
            isShowAcceptAssignment: false,
            isShowDeclineAssignment: false,
            responseAssignmentId: assignmentId
        })
    }

    prePage() {
        if(this.state.pageNum - 1 >= 1){
			this.setState({
				pageNum: this.state.pageNum - 1
			}, () => {
				this.loadOwnAssignments();
			})
		}
    }

    nextPage() {
        if(this.state.pageNum + 1 <= this.state.totalPages){
			this.setState({
				pageNum: this.state.pageNum + 1
			}, () => {
				this.loadOwnAssignments();
			})
		}
    }

    render() {
        return (
            <div>
                <div className="own-assignment-title">
                    <b>My Assignment</b>
                </div>
                <div className="own-assignment-content">
                    <Table hover>
                        <thead>
                            <tr>
                                <th 
                                    onClick={() => this.orderByAssetCode()}
                                    style={{cursor: 'pointer'}}
                                >
                                    Asset Code
                                    <img src={DropdownIcon} width="12px" alt="dropdown-icon"/>
                                </th>
                                <th 
                                    onClick={() => this.orderByAssetName()}
                                    style={{cursor: 'pointer'}}
                                >
                                    Asset Name
                                    <img src={DropdownIcon} width="12px" alt="dropdown-icon"/>
                                </th>
                                <th 
                                    onClick={() => this.orderByCategory()}
                                    style={{cursor: 'pointer'}}
                                >
                                    Category
                                    <img src={DropdownIcon} width="12px" alt="dropdown-icon"/>
                                </th>
                                <th 
                                    onClick={() => this.orderByAssignedDate()}
                                    style={{cursor: 'pointer'}}
                                >
                                    Assigned Date
                                    <img src={DropdownIcon} width="12px" alt="dropdown-icon"/>
                                </th>
                                <th 
                                    onClick={() => this.orderByState()}
                                    style={{cursor: 'pointer'}}
                                >
                                    State
                                    <img src={DropdownIcon} width="12px" alt="dropdown-icon"/>
                                </th>
                                <th></th>
                                <th></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.ownAssignments.map((assignment) => {
                                    return (
                                        <tr
                                            id={assignment.assignmentId}
                                            key={assignment.assignmentId}
                                        >
                                            <td onClick={() => this.toggleShowButton(assignment.assignmentId)}>{assignment.assetCode}</td>
                                            <td onClick={() => this.toggleShowButton(assignment.assignmentId)}>{assignment.assetName}</td>
                                            <td onClick={() => this.toggleShowButton(assignment.assignmentId)}>{assignment.categoryName}</td>
                                            <td onClick={() => this.toggleShowButton(assignment.assignmentId)}>{assignment.assignedDate}</td>
                                            <td onClick={() => this.toggleShowButton(assignment.assignmentId)}>{assignment.state}</td>
                                            <td>
                                                <button className="own-assignment-click-button" disabled={assignment.state === 'Accepted' ? true : false}
                                                        onClick={() => this.handleAcceptAssignmentShow(assignment.assignmentId)}>
                                                    <img 
                                                        src={TickIcon}
                                                        alt="tick-icon"
                                                        className={assignment.state === 'Accepted' ? 'own-assignment-icon-disabled' : 'own-assignment-icon-normal'}
                                                        width="14px"
                                                    />
                                                </button>
                                            </td>
                                            <td>
                                                <button className="own-assignment-click-button" disabled={assignment.state === 'Accepted' ? true : false}
                                                    onClick={() => this.handleDeclineAssignmentShow(assignment.assignmentId)}>
                                                    <img
                                                        src={XIcon}
                                                        alt="x-icon"
                                                        className={assignment.state === 'Accepted' ? 'own-assignment-icon-disabled' : 'own-assignment-icon-normal'}
                                                        width="14px"
                                                    />
                                                </button>
                                            </td>
                                            <td>
                                                <button className="own-assignment-click-button" disabled={assignment.state !== 'Accepted' ? true : assignment.isReturnRequest}
                                                    onClick={() => this.handleReturnRequestShow(assignment.assignmentId)}>
                                                    <img
                                                        src={RefreshIcon}
                                                        className={assignment.state !== 'Accepted' || assignment.isReturnRequest ? 'own-assignment-icon-disabled' : 'own-assignment-icon-normal'}
                                                        alt="refresh-icon"
                                                        width="14px"
                                                    />
                                                </button>
                                            </td>
                                        </tr>
                                    )
                                })
                            }
                        </tbody>
                    </Table>
                </div>
                <div className="paging">
                    {
						this.state.ownAssignments.length > 0 &&
						<div 
							className={this.state.pageNum === 1 ? "pre-next pre-next-disabled" : "pre-next"}
							onClick={() => this.prePage()}
						>
							{"<"}
						</div>
					}
                    {
                        this.state.pageList.map((page) => {
                            return (
                                <div
                                    id={page}
                                    key={page}
                                    className={this.state.pageNum === page ? "current-page" : "paging-page"}
                                    onClick={() => this.changPage(page)}
                                >
                                    {page}
                                </div>
                            )
                        })
                    }
                    {
						this.state.totalPages < 2 && this.state.ownAssignments.length > 0 &&
						<div className="current-page">
							1
						</div>
					}
                    {
						this.state.ownAssignments.length > 0 &&
						<div 
							className={this.state.pageNum === this.state.totalPages ? "pre-next pre-next-disabled" : "pre-next"}
							onClick={() => this.nextPage()}
						>
							{">"}
						</div>
					}
                </div>
                {
                    this.state.ownAssignments.length === 0 &&
                    <div className="own-assignment-notificaton">
                        <Alert color="secondary">
                            No assignments until today.
                        </Alert>
                    </div>
                }
                {
                    this.state.isFail === true &&
                    <div className="own-assignment-notificaton">
                        <Alert color="danger">
                            {this.state.messageFail}
                        </Alert>
                    </div>
                }
                <Modal isOpen={this.state.modal} toggle={() => this.toggleShow()}>
                    <ModalHeader style={{backgroundColor: 'rgba(239,241,245,1)', color: 'red'}}>
                        <span>Detailed Assignment Information</span>
                        <span 
                            className="close-own-assignment-detail"
                            onClick={() => this.toggleShow()}
                        >
                            X
                        </span>
                    </ModalHeader>
                    <ModalBody>
                        <OwnAssignmentDetail id={this.state.assignmentId}/>
                    </ModalBody>
                </Modal>
                <AcceptAssignment assignmentId={this.state.responseAssignmentId} isShowAcceptAssignment={this.state.isShowAcceptAssignment} handleAcceptAssignmentShow={this.handleAcceptAssignmentShow}/>
                <DeclineAssignment assignmentId={this.state.responseAssignmentId} isShowDeclineAssignment={this.state.isShowDeclineAssignment} handleDeclineAssignmentShow={this.handleDeclineAssignmentShow}/>
                <ReturnRequest assignmentId={this.state.responseAssignmentId} staffCode={this.state.user.staffCode} isShowReturnRequest={this.state.isShowReturnRequest} handleReturnRequestShow={this.handleReturnRequestShow}/>
            </div>
        )
    }
}
export default withCookies(index)
