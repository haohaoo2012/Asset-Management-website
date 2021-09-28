import React, { Component } from 'react';
import Navbar from "../Navbar";
import Menu from "../Menu";
import * as business from "../../constants/Business";
import { withRouter } from "react-router-dom";
import { instanceOf } from 'prop-types';
import { withCookies, Cookies } from 'react-cookie';
import OwnAssignment from "../OwnAssignment";
import { Modal, ModalHeader, ModalBody } from 'reactstrap';
import FirstLogin from "../FirstLogin";

class index extends Component {

    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            user: this.props.cookies.get('user') || '',
        }
    }

    componentDidMount() {

    }

    reload() {
        this.setState({
          user: this.props.cookies.get("user") || "",
        })
    }

    render() {
        return (
            <div>
                <Navbar businessName="Staff Home Page" />
                <Menu business={business.HOME} />
                <OwnAssignment />
                <Modal isOpen={this.state.user.firstLogin === false}>
                    <ModalHeader style={{backgroundColor: 'rgba(239,241,245,1)', color: 'red', paddingLeft: '12%'}}>
                        Change password
                    </ModalHeader>
                    <ModalBody>
                        <FirstLogin reload={() => this.reload()} />
                    </ModalBody>
                </Modal>
            </div>
        )
    }
}

export default withCookies(withRouter(index));