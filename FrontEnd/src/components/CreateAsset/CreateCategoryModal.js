import React, { Component } from "react";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, Col, Form, FormGroup, Label, Input } from "reactstrap";
import { dataNotFillException } from "../../exceptions/DataNotFillException";
import { checkPattern } from "../../commons/filter";
import * as CategoryService from "../../services/CategoryService";
import { createCategoryFailException } from "../../exceptions/CategoryException";
import * as URL from "../../constants/URL";

export default class CreateCategoryModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      modal: false,
      categoryName: "",
      categoryCode: "",
      message: "",
      disabledButon: true,
    };
    this.toggle = this.toggle.bind(this);
    this.toggleSave = this.toggleSave.bind(this);
    this.handleCategoryChange = this.handleCategoryChange.bind(this);
  }

  clearMessage() {
    setTimeout(() => {
      this.setState({
        message: "",
      });
    }, 2000);
  }

  toggle() {
    this.setState({
      modal: !this.state.modal,
    });
  }

  async toggleSave() {
    let result;
    let category = {
      categoryName: this.state.categoryName,
      categoryCode: this.state.categoryCode,
    };
    try {
      result = await CategoryService.createNewCategory(URL.CREATE_CATEGORY, category);
    } catch (error) {
      console.log(error);
      this.setState({
        message: createCategoryFailException(error),
      });
      this.clearMessage();
      return;
    }
    this.props.resultReturn(this.state.categoryCode, this.state.categoryName);
    this.setState({
      modal: !this.state.modal,
    });
    this.clearCategoryForm();
  }

  clearCategoryForm() {
    this.setState({
      categoryName: "",
      categoryCode: "",
    });
  }

  handleSaveClick = () => {
    let categoryCodePattern = /(^[A-Z]{2,3}$)/;
    if (this.state.categoryCode.trim() === "" || !checkPattern(categoryCodePattern, this.state.categoryCode.trim())) {
      this.setState({
        message: dataNotFillException("Category code"),
      });
      this.clearMessage();
    } else if (this.state.categoryName.trim() === "") {
      this.setState({
        message: dataNotFillException("Category name"),
      });
      this.clearMessage();
    } else {
      this.toggleSave();
    }
  };

  handleCategoryChange(e, key) {
    if (key === "categoryCode") {
        this.setState({ [key]: e.target.value.toUpperCase() }, () => {
          this.checktoEnabledButton();
        });
    } else {
      this.setState({ [key]: e.target.value }, () => {
        this.checktoEnabledButton();
      });
    }
  }

  checktoEnabledButton() {
    let categoryCodePattern = /(^[A-Z]{2,3}$)/;
    if (this.state.categoryCode.trim() === "" || !checkPattern(categoryCodePattern, this.state.categoryCode.trim()) || this.state.categoryName.trim() === "") {
      this.setState({ disabledButon: true });
    } else {
      this.setState({ disabledButon: false });
    }
  }

  render() {
    return (
      <div>
        <Button
          color={this.props.color}
          onClick={this.toggle}
          className={this.props.buttonClassName}
          style={{ "margin-left": "25rem", width: "8rem" }}
          size="sm"
        >
          {this.props.buttonLabel}
        </Button>
        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className} size="lg" backdrop={["static"]} keyboard={false}>
          <ModalHeader toggle={this.toggle}>{this.props.title}</ModalHeader>
          <ModalBody>
            <h4 className="text-danger">{this.state.message}</h4>
            <Form>
              <FormGroup row>
                <Label sm={3}>Category code:</Label>
                <Col sm={8}>
                  <Input
                    type="text"
                    name="categoryCode"
                    id="categoryCode"
                    placeholder="2 - 3 character"
                    value={this.state.categoryCode}
                    onChange={(e) => this.handleCategoryChange(e, "categoryCode")}
                  />
                </Col>
              </FormGroup>
              <FormGroup row>
                <Label for="categoryName" sm={3}>
                  Category name:
                </Label>
                <Col sm={8}>
                  <Input
                    type="text"
                    name="categoryName"
                    id="categoryName"
                    placeholder="Category name"
                    value={this.state.categoryName}
                    onChange={(e) => this.handleCategoryChange(e, "categoryName")}
                  />
                </Col>
              </FormGroup>
            </Form>
          </ModalBody>
          <ModalFooter>
            <Button color={this.props.actionButtonColor} onClick={this.handleSaveClick} disabled={this.state.disabledButon}>
              {this.props.actionButtonLabel}
            </Button>
            <Button color="secondary" onClick={this.toggle}>
              Cancel
            </Button>
          </ModalFooter>
        </Modal>
      </div>
    );
  }
}
