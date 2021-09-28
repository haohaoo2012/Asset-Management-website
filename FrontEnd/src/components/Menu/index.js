import React, { Component } from "react";
import "./Menu.css";
import logo from "../../images/Logo_lk@2x.png";
import { instanceOf } from "prop-types";
import { withCookies, Cookies } from "react-cookie";
import { withRouter } from "react-router-dom";
import * as business from "../../constants/Business";
import * as URL from "../../constants/URL";

class index extends Component {
  static propTypes = {
    cookies: instanceOf(Cookies).isRequired,
  };

  constructor(props) {
    super(props);
    this.state = {
      user: this.props.cookies.get("user") || "",
      homeClassName: "",
      manageUserClassName: "",
      manageAssetclassName: "",
      manageAssignmentClassName: "",
      requestForReturningClassName: "",
      reportClassName: "",
      homeClassNameActive: "",
      manageUserClassNameActive: "",
      manageAssetclassNameActive: "",
      manageAssignmentClassNameActive: "",
      requestForReturningClassNameActive: "",
      reportClassNameActive: "",
    };
  }

  componentDidMount() {
    if (this.state.user !== "") {
      if (this.state.user.role === business.ROLE_STAFF) {
        this.setState({
          homeClassName: "d-none",
          manageUserClassName: "d-none",
          manageAssetclassName: "d-none",
          manageAssignmentClassName: "d-none",
          requestForReturningClassName: "d-none",
          reportClassName: "d-none",
          homeClassNameActive: "",
          manageUserClassNameActive: "d-none",
          manageAssetclassNameActive: "d-none",
          manageAssignmentClassNameActive: "d-none",
          requestForReturningClassNameActive: "d-none",
          reportClassNameActive: "d-none",
        });
      } else if (this.state.user.role === business.ROLE_ADMIN) {
        this.setState(
          {
            homeClassName: "",
            manageUserClassName: "",
            manageAssetclassName: "",
            manageAssignmentClassName: "",
            requestForReturningClassName: "",
            reportClassName: "",
            homeClassNameActive: "d-none",
            manageUserClassNameActive: "d-none",
            manageAssetclassNameActive: "d-none",
            manageAssignmentClassNameActive: "d-none",
            requestForReturningClassNameActive: "d-none",
            reportClassNameActive: "d-none",
          },
          () => {
            if (this.props.business === business.HOME) {
              this.setState({ homeClassNameActive: "" });
            } else if (this.props.business === business.MANAGE_USER) {
              this.setState({ manageUserClassNameActive: "" });
            } else if (this.props.business === business.MANAGE_ASSIGNMENT) {
              this.setState({ manageAssignmentClassNameActive: "" });
            } else if (this.props.business === business.REQUEST_FOR_RETURNING) {
              this.setState({ requestForReturningClassNameActive: "" });
            } else if (this.props.business === business.MANAGE_ASSET) {
              this.setState({ manageAssetclassNameActive: "" });
            } else if (this.props.business === business.REPORT) {
              this.setState({ reportClassNameActive: "" });
            }
          }
        );
      }
    } else {
      // dont't have cookie, redirect to sign in but we already have protected route so when we implement it for test only
    }
  }

  handleMenuClick(businessName) {
    if (this.state.user !== "") {
      if (this.state.user.role === business.ROLE_ADMIN) {
        if (businessName === business.HOME) {
          this.props.history.push("/admin");
        } else if (businessName === business.MANAGE_USER) {
          this.props.history.push("/manageuser");
        } else if (businessName === business.MANAGE_ASSIGNMENT) {
          this.props.history.push("/manageassignment");
        } else if (businessName === business.REQUEST_FOR_RETURNING) {
          this.props.history.push(URL.MANAGE_REQUEST);
        } else if (businessName === business.MANAGE_ASSET) {
          this.props.history.push("/manageasset");
        } else if (businessName === business.REPORT) {
          this.props.history.push("/report");
        }
      }
    } else {
    } // dont't have cookie, redirect to sign in but we already have protected route so when we implement it for test only
  }

  render() {
    return (
      <div>
        <div id="Menu_mis">
          {/* Report inactive */}
          <div id="Report_mit" className={`Report ${this.state.reportClassName}`} onClick={() => this.handleMenuClick(business.REPORT)}>
            <svg className="Path_1_miu" viewBox="0 0 262 60">
              <path id="Path_1_miu" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Report_miv">
              <span>Report</span>
            </div>
          </div>
          {/* Report active */}
          <div id="Report_k" className={`Report ${this.state.reportClassNameActive}`}>
            <svg className="Path_1_la" viewBox="0 0 262 60">
              <path id="Path_1_la" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Report_la">
              <span>Report</span>
            </div>
          </div>
          {/* Request for return inactive */}
          <div
            id="Request_for_Returning_miw"
            className={`Request_for_Returning ${this.state.requestForReturningClassName}`}
            onClick={() => this.handleMenuClick(business.REQUEST_FOR_RETURNING)}
          >
            <svg className="Path_1_mix" viewBox="0 0 262 60">
              <path id="Path_1_mix" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Request_for_Returning_miy">
              <span>Request for Returning</span>
            </div>
          </div>
          {/* Request for return active */}
          <div id="Request_for_Returning_cf" className={`Request_for_Returning ${this.state.requestForReturningClassNameActive}`}>
            <svg className="Path_1_cg" viewBox="0 0 262 60">
              <path id="Path_1_cg" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Request_for_Returning_ch">
              <span>Request for Returning</span>
            </div>
          </div>
          {/* Manage assignment inactive */}
          <div
            id="Manage_Assignment_miz"
            className={`Manage_Assignment ${this.state.manageAssignmentClassName}`}
            onClick={() => this.handleMenuClick(business.MANAGE_ASSIGNMENT)}
          >
            <svg className="Path_1_mi" viewBox="0 0 262 60">
              <path id="Path_1_mi" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Manage_Assignment_mi">
              <span>Manage Assignment</span>
            </div>
          </div>
          {/* Manage assignment active */}
          <div id="Manage_Assignment_m" className={`Manage_Assignment ${this.state.manageAssignmentClassNameActive}`}>
            <svg className="Path_1_n" viewBox="0 0 262 60">
              <path id="Path_1_n" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Manage_Assignment_o">
              <span>Manage Assignment</span>
            </div>
          </div>
          {/* Manage asset inactive */}
          <div id="Manage_Asset_mi" className={`Manage_Asset ${this.state.manageAssetclassName}`} onClick={() => this.handleMenuClick(business.MANAGE_ASSET)}>
            <svg className="Path_1_mja" viewBox="0 0 262 60">
              <path id="Path_1_mja" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Manage_Asset_mja">
              <span>Manage Asset</span>
            </div>
          </div>
          {/* Manage asset active */}
          <div id="Manage_Asset_fo" className={`Manage_Asset ${this.state.manageAssetclassNameActive}`}>
            <svg className="Path_1_fp" viewBox="0 0 262 60">
              <path id="Path_1_fp" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Manage_Asset_fq">
              <span>Manage Asset</span>
            </div>
          </div>
          {/* Manage user inactive */}
          <div id="Manage_User_mi" className={`Manage_User ${this.state.manageUserClassName}`} onClick={() => this.handleMenuClick(business.MANAGE_USER)}>
            <svg className="Path_1_mjb" viewBox="0 0 262 60">
              <path id="Path_1_mjb" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Manage_User_mja">
              <span>Manage User</span>
            </div>
          </div>
          {/* Manage asset active */}
          <div id="Manage_User_jl" className={`Manage_User ${this.state.manageUserClassNameActive}`}>
            <svg className="Path_1_jr" viewBox="0 0 262 60">
              <path id="Path_1_jr" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Manage_User_jm">
              <span>Manage User</span>
            </div>
          </div>
          {/* inactive */}
          <div id="Home" className={`Home ${this.state.homeClassName}`} onClick={() => this.handleMenuClick(business.HOME)}>
            <svg className="Path_1_lj" viewBox="0 0 262 60">
              <path id="Path_1_lj" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Home_lk">
              <span>Home</span>
            </div>
          </div>
          {/*active */}
          <div id="Home_mi" className={`Home ${this.state.homeClassNameActive}`}>
            <svg className="Path_1_mjc" viewBox="0 0 262 60">
              <path id="Path_1_mjc" d="M 0 0 L 262 0 L 262 60 L 0 60 L 0 0 Z"></path>
            </svg>
            <div id="Home_mja">
              <span>Home</span>
            </div>
          </div>
        </div>
        <div id="Logo_mjb">
          <div id="Online_Asset_Management_mjc">
            <span>Online Asset Management</span>
          </div>
          <img id="Logo_mjd" src={logo} />
        </div>
      </div>
    );
  }
}

export default withCookies(withRouter(index));
