import React, { Component } from "react";
import "./CreateAsset.css";
import { Input, FormGroup, Button, Modal, ModalHeader, ModalBody, ModalFooter } from "reactstrap";
import * as state from "../../constants/State";
import CreateCategoryModal from "./CreateCategoryModal";
import { dataNotFillException } from "../../exceptions/DataNotFillException";
import { getCategoryFailException } from "../../exceptions/CategoryException";
import { createAssetFailException } from "../../exceptions/AssetException";
import * as AssetService from "../../services/AssetService";
import * as CategoryService from "../../services/CategoryService";
import * as URL from "../../constants/URL";
import { withRouter } from "react-router-dom";

class index extends Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      category: "",
      specification: "",
      installDate: "",
      avalableCheck: "",
      notAvailableCheck: "",
      modal: false,
      notiContent: "",
      categoryList: [],
      disabledButon: true,
    };
    this.toggle = this.toggle.bind(this);
  }

  toggle() {
    this.setState({
      modal: !this.state.modal,
    });
  }

  handleFieldChange(e, key) {
    this.setState({ [key]: e.target.value }, () => {
      this.checktoEnabledButton();
    });
  }

  handleRadioChange(key) {
    if (key === "avalableCheck") {
      this.setState({ avalableCheck: true, notAvailableCheck: false }, () => {
        this.checktoEnabledButton();
      });
    } else {
      this.setState({ avalableCheck: false, notAvailableCheck: true }, () => {
        this.checktoEnabledButton();
      });
    }
  }

  checktoEnabledButton() {
    if (
      this.state.name.trim() === "" ||
      this.state.category === "" ||
      this.state.specification.trim() === "" ||
      this.state.installDate === "" ||
      (this.state.avalableCheck === "" && this.state.notAvailableCheck === "")
    ) {
      this.setState({ disabledButon: true });
    } else {
      this.setState({ disabledButon: false });
    }
  }

  cancelClick() {
    this.props.history.push(URL.MANAGE_ASSET);
  }

  saveClick() {
    if (this.state.name.trim() === "") {
      this.setState({
        notiContent: dataNotFillException("Name"),
      });
      this.toggle();
    } else if (this.state.category === "") {
      this.setState({
        notiContent: dataNotFillException("Category"),
      });
      this.toggle();
    } else if (this.state.specification.trim() === "") {
      this.setState({
        notiContent: dataNotFillException("Specification"),
      });
      this.toggle();
    } else if (this.state.installDate === "") {
      this.setState({
        notiContent: dataNotFillException("Install date"),
      });
      this.toggle();
    } else if (this.state.avalableCheck === "" && this.state.notAvailableCheck === "") {
      this.setState({
        notiContent: dataNotFillException("State"),
      });
      this.toggle();
    } else {
      this.saveCategory();
    }
  }

  async saveCategory() {
    let result;
    let asset = {
      assetName: this.state.name,
      state: this.state.avalableCheck === true ? state.AVAILABLE : state.NOT_AVAILABLE,
      installDate: `${this.state.installDate}T00:00:00`,
      specification: this.state.specification,
      categoryCode: this.state.category,
    };
    try {
      result = await AssetService.createNewAsset(URL.CREATE_ASSET_API, asset);
    } catch (error) {
      console.log(error);
      this.setState({
        notiContent: createAssetFailException(error),
      });
      this.toggle();
      return;
    }
    // this.setState({
    //   notiContent: "Create asset success",
    // });
    // this.toggle();
    // this.clearForm();
    localStorage.setItem("newAsset", JSON.stringify(result.data.data));
    this.props.history.push(URL.MANAGE_ASSET);
  }

  clearForm() {
    this.setState({
      name: "",
      category: "",
      specification: "",
      installDate: "",
      avalableCheck: "",
      notAvailableCheck: "",
    });
  }

  resultReturn(categoryCode, categoryName) {
    this.componentDidMount();
  }

  setCategory(categoryResult) {
    this.setState({
      categoryList: categoryResult,
    });
  }

  async componentDidMount() {
    let categoryResult = null;
    try {
      categoryResult = await CategoryService.getCategory(URL.GET_CATEGORY);
    } catch (error) {
      console.log(error);
      this.setState({
        notiContent: getCategoryFailException(error),
      });
      return;
    }
    this.setCategory(categoryResult.data.data);
  }

  render() {
    return (
      <div>
        <div id="Create_New_Asset_fz">
          <span>Create New Asset</span>
        </div>
        <div id="Form_f">
          <div id="DOB_f">
            <div id="Specification_f">
              <span>Specification</span>
            </div>
            <Input
              type="textarea"
              name="specification"
              className="Rectangle_330_f"
              id="Rectangle_330_f"
              value={this.state.specification}
              placeholder="Specification"
              onChange={(e) => this.handleFieldChange(e, "specification")}
            />
          </div>
          <div id="State_f">
            <div id="state_f">
              <FormGroup tag="fieldset">
                <FormGroup check id="available_f" className="available">
                  <div id="Radio_f" className="Radio">
                    <Input
                      className="Oval_f"
                      id="Oval_f"
                      type="radio"
                      name="radioState"
                      value={this.state.avalableCheck}
                      checked={this.state.avalableCheck}
                      onChange={() => this.handleRadioChange("avalableCheck")}
                    />{" "}
                    Available
                  </div>
                </FormGroup>
                <FormGroup check id="not_available_fb" className="not_available">
                  <div id="Radio_fd" className="Radio">
                    <Input
                      className="Oval_fe"
                      id="Oval_fe"
                      type="radio"
                      name="radioState"
                      value={this.state.notAvailableCheck}
                      checked={this.state.notAvailableCheck}
                      onChange={() => this.handleRadioChange("notAvailableCheck")}
                    />{" "}
                    Not available
                  </div>
                </FormGroup>
              </FormGroup>
            </div>
            <div id="State_ff">
              <span>State</span>
            </div>
          </div>
          <div id="Joined_Date_fg">
            <div id="Installed_Date_fh">
              <span>Installed Date</span>
            </div>
            <Input
              type="date"
              name="installDate"
              value={this.state.installDate}
              className="Rectangle_330_fi"
              id="Rectangle_330_fi"
              onChange={(e) => this.handleFieldChange(e, "installDate")}
            />
            {/* <div id="calendar-date-fill_fj">
              <svg className="Path_2_fk" viewBox="0 0 16 10.246">
                <path
                  id="Path_2_fk"
                  d="M 4 0.5 C 4 0.2238576114177704 3.776142358779907 0 3.5 0 C 3.223857641220093 0 3 0.2238576412200928 3 0.5 L 3 1 L 2 1 C 0.8954304456710815 1 -2.384185791015625e-07 1.89543080329895 0 3.000000238418579 L 0 4 L 16 4 L 16 3 C 16 1.895430445671082 15.10456943511963 1 14 1 L 13 1 L 13 0.5 C 13 0.2238576114177704 12.77614212036133 0 12.5 0 C 12.22385787963867 0 12 0.2238576412200928 12 0.5 L 12 1 L 4 1 L 4 0.5 Z M 9.402000427246094 10.24600028991699 C 10.02700042724609 10.24600028991699 10.58600044250488 9.76200008392334 10.58600044250488 9.065999984741211 C 10.58600044250488 8.234000205993652 10.05900001525879 7.835999965667725 9.426000595092773 7.835999965667725 C 8.840000152587891 7.835999965667725 8.258000373840332 8.222999572753906 8.258000373840332 9.045999526977539 C 8.258000373840332 9.86299991607666 8.801000595092773 10.24599933624268 9.402000427246094 10.24599933624268 Z"
                ></path>
              </svg>
              <svg className="Path_3_fl" viewBox="0 5 16 11">
                <path
                  id="Path_3_fl"
                  d="M 16 14 L 16 5 L 0 5 L 0 14 C 0 15.10456943511963 0.8954305648803711 16 2 16 L 14 16 C 15.10456943511963 16 16 15.10456943511963 16 14 Z M 9.336000442504883 12.78999996185303 C 8.226000785827637 12.78999996185303 7.680000305175781 12.02299976348877 7.633000373840332 11.38300037384033 L 8.315999984741211 11.38300037384033 C 8.359000205993652 11.75300025939941 8.703000068664551 12.20300006866455 9.366999626159668 12.20300006866455 C 10.21099948883057 12.20300006866455 10.66799926757813 11.35500049591064 10.67199993133545 10.03899955749512 L 10.64499950408936 10.03899955749512 C 10.49199962615967 10.45299911499023 10.00799942016602 10.82899951934814 9.261999130249023 10.82899951934814 C 8.409998893737793 10.82899951934814 7.585999011993408 10.2189998626709 7.585999011993408 9.059000015258789 C 7.585999011993408 7.921999931335449 8.456998825073242 7.25 9.382999420166016 7.25 C 10.55499935150146 7.25 11.33599948883057 7.984000205993652 11.33599948883057 9.918000221252441 C 11.33599948883057 11.72300052642822 10.5939998626709 12.78900051116943 9.335999488830566 12.78900051116943 Z M 6.446000099182129 7.355000019073486 L 6.446000099182129 12.6870002746582 L 5.769999980926514 12.6870002746582 L 5.769999980926514 8.079000473022461 L 5.757999897003174 8.079000473022461 C 5.467999935150146 8.235000610351563 4.875 8.599000930786133 4.5 8.856000900268555 L 4.5 8.159999847412109 C 4.920842170715332 7.865143775939941 5.359328269958496 7.596308708190918 5.813000202178955 7.354998588562012 L 6.444999694824219 7.355000019073486 Z"
                ></path>
              </svg>
            </div> */}
          </div>
          <div id="Joined_Date_fm">
            <div id="Category_fn">
              <span>Category</span>
            </div>
            <Input
              type="select"
              name="category"
              className="Path_8_fo my_category"
              id="Path_8_fo"
              value={this.state.category}
              onChange={(e) => this.handleFieldChange(e, "category")}
            >
              <option value="" disabled hidden>
                Choose here
              </option>
              {this.state.categoryList.map((cat) => (
                <option key={cat.categoryCode + cat.categoryName} id={cat.categoryCode} value={cat.categoryCode}>
                  {cat.categoryName}
                </option>
              ))}
            </Input>
            <CreateCategoryModal
              buttonClassName="float-right"
              buttonLabel="Create category"
              color="link"
              actionButtonColor="success"
              actionButtonLabel="Save"
              title="Create category"
              resultReturn={(categoryCode, categoryName) => this.resultReturn(categoryCode, categoryName)}
            />
            {/* <Button outline color="secondary" className="float-right ml-5">
              Create
            </Button> */}
          </div>
          <Button id="Cancel_btn_fp" className="Cancel_btn" outline color="secondary" onClick={() => this.cancelClick()}>
            Cancel
          </Button>
          <Button id="Save_btn_fs" className="Save_btn" color="danger" onClick={() => this.saveClick()} disabled={this.state.disabledButon}>
            Save
          </Button>

          <div id="Name_fw">
            <div id="Name_fx">
              <span>Name</span>
            </div>
            <Input
              type="text"
              name="name"
              className="Rectangle_330_fx"
              id="Rectangle_330_fx"
              value={this.state.name}
              placeholder="Name"
              onChange={(e) => this.handleFieldChange(e, "name")}
            />
          </div>
          <svg class="icondropdown_arrow_fa" viewBox="0.023 -6 12 6">
            <path
              id="icondropdown_arrow_fa"
              d="M 0.02301025576889515 -6 L 12.02301120758057 -6 L 6.002416610717773 1.862645149230957e-09 L 0.02301025576889515 -6 Z"
            ></path>
          </svg>
        </div>
        {/* noti modal */}
        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className} backdrop={["static"]} keyboard={false}>
          <ModalHeader toggle={this.toggle}>Alert</ModalHeader>
          <ModalBody>{this.state.notiContent}</ModalBody>
          <ModalFooter>
            <Button color="secondary" onClick={this.toggle}>
              Dismiss
            </Button>
          </ModalFooter>
        </Modal>
        {/* noti modal */}
      </div>
    );
  }
}
export default withRouter(index);
