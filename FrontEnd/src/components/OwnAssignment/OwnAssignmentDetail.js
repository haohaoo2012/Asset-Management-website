import React, { Component } from 'react';
import { Container, Row, Col, Alert } from 'reactstrap';
import { get } from '../../httpHelper';

export default class OwnAssignmentDetail extends Component {

    constructor(props) {
        super(props);
        this.state = {
            detail: {},

            isFail: false,
            messageFail: ''
        };
    }

    componentDidMount() {
        this.loadDetail();
    }

    loadDetail() {
        let url = `assignment/own/${this.props.id}`;
        get(url)
            .then(response => {
                if (response.status === 200) {
                    if (response.data.successCode === 'LOAD_OWN_ASSIGNMENT_DETAIL_SUCCESS') {
                        this.setState({
                            detail: response.data.data,
                            isFail: false
                        })
                    }
                }
            })
            .catch(err => {
                if (err.response) {
                    if (err.response.data.errorCode === 'ASSIGNMENT_NOT_FOUND') {
                        this.setState({ messageFail: 'Assignment not found.' });
                    }
                    else if (err.response.data.errorCode === 'ASSIGNMENT_IS_DELETED') {
                        this.setState({ messageFail: 'Assignment is deleted.' });
                    }
                    else if (err.response.data.errorCode === 'ASSIGNMENT_IS_DECLINED') {
                        this.setState({ messageFail: 'Assignment is declined.' });
                    }
                    else {
                        this.setState({ messageFail: 'Error to load own assignment detail.' })
                    }
                }
                else {
                    this.setState({ messageFail: 'Fail to load own assignment detail.' })
                }
                this.setState({ isFail: true })
            })
    }

    render() {
        return (
            <div>
                {
                    this.state.isFail === true &&
                    <Alert color="danger">
                        {this.state.messageFail}
                    </Alert>
                }
                {
                    this.state.isFail === false &&
                    <Container style={{fontSize: '16px'}}>
                        <Row xs="3" className="mb-4">
                            <Col>
                                Asset Code
                            </Col>
                            <Col xs="8">
                                {this.state.detail.assetCode}
                            </Col>
                        </Row>
                        <Row xs="3" className="mb-4">
                            <Col>
                                Asset Name
                            </Col>
                            <Col xs="8">
                                {this.state.detail.assetName}
                            </Col>
                        </Row>
                        <Row xs="3" className="mb-4">
                            <Col>
                                Specification
                            </Col>
                            <Col xs="8">
                                {this.state.detail.specification}
                            </Col>
                        </Row>
                        <Row xs="3" className="mb-4">
                            <Col>
                                Assigned to
                            </Col>
                            <Col xs="8">
                                {this.state.detail.assignedTo}
                            </Col>
                        </Row>
                        <Row xs="3" className="mb-4">
                            <Col>
                                Assigned by
                            </Col>
                            <Col xs="8">
                                {this.state.detail.assignedBy}
                            </Col>
                        </Row>
                        <Row xs="3" className="mb-4">
                            <Col>
                                Assigned Date
                            </Col>
                            <Col xs="8">
                                {this.state.detail.assignedDate}
                            </Col>
                        </Row>
                        <Row xs="3" className="mb-4">
                            <Col>
                                State
                            </Col>
                            <Col xs="8">
                                {this.state.detail.state}
                            </Col>
                        </Row>
                        <Row xs="3" className="mb-4">
                            <Col>
                                Note
                            </Col>
                            <Col xs="8">
                                {this.state.detail.note}
                            </Col>
                        </Row>
                    </Container>
                }
            </div>
        )
    }
}
