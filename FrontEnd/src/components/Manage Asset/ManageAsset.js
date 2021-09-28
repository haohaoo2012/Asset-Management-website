import React, { Component } from "react";
import Navbar from "../Navbar";
import Menu from "../Menu";
import * as business from "../../constants/Business";
import "./Manageasset.css";
import { Table, Button, UncontrolledDropdown, DropdownToggle, DropdownMenu, Modal, ModalHeader, ModalBody, ModalFooter, Alert, Input } from "reactstrap";
import SearchIcon from "../../images/search.png";
import FilterIcon from "../../images/filter-icon.png";
import DropdownIcon from "../../images/dropdown-icon.png";
import EditIcon from "../../images/edit-asset-icon.png";
import DeleteIcon from "../../images/delete-asset-icon.png";
import { post, get, del } from "../../httpHelper";
import { withRouter } from "react-router-dom";
import { instanceOf } from "prop-types";
import { withCookies, Cookies } from "react-cookie";
import * as URL from "../../constants/URL";
import * as role from "../../constants/Business";
import AssetDetail from "./AssetDetail";
import DeleteAsset, { DeleteAsset_No, DeleteAsset_Yes } from "../Delete Asset";

class ManageAsset extends Component {
  static propTypes = {
    cookies: instanceOf(Cookies).isRequired,
  };

  constructor(props) {
    super(props);
    this.state = {
      user: this.props.cookies.get("user") || "",
      assetList: [],
      categoryList: [],
      pageList: [],

      sizeInPage: 15,
      currentPage: 1,
      totalPages: 1,

      sortField: "assetCode",
      sortType: "ASC",
      searchValue: "",

      available: true,
      notAvailable: true,
      assigned: true,
      waiting: false,
      recycled: false,
      allState: false,

      allCategory: true,
      selectedCategory: [],

      modal: false,
      assetId: "",

      isFail: false,
      messageFail: "",

      deleteAssetNO: false,
      deleteAssetYES: false,
      deleteCode: "",
    };
  }

  componentDidMount() {
    this.loadCategory();
    this.loadData();
    this.returnPageList();
  }

  returnStateList() {
    let stateList = [];
    if (this.state.allState === false) {
      if (this.state.available === true) {
        stateList.push(1);
      }
      if (this.state.notAvailable === true) {
        stateList.push(2);
      }
      if (this.state.assigned === true) {
        stateList.push(3);
      }
      if (this.state.waiting === true) {
        stateList.push(4);
      }
      if (this.state.recycled === true) {
        stateList.push(5);
      }
    }
    return stateList;
  }

  returnSelectedCategory() {
    let selectedCategory;
    if (this.state.allCategory === true) {
      selectedCategory = [];
    } else {
      selectedCategory = this.state.selectedCategory;
    }
    return selectedCategory;
  }

  loadData() {
    let stateList = this.returnStateList();
    let selectedCategory = this.returnSelectedCategory();
    let url = `asset/filter-search-sort?page=${this.state.currentPage - 1}&size=${this.state.sizeInPage}`;
    let body = {
      states: stateList,
      categoriesCode: selectedCategory,
      sortField: this.state.sortField,
      sortType: this.state.sortType,
      searchKeyWord: this.state.searchValue,
    };
    post(url, body)
      .then((response) => {
        if (response.status === 200) {
          if (response.data.successCode === "ASSET_LOADED_SUCCESS") {
            this.setState(
              {
                assetList: response.data.data,
              },
              () => {
                let newAsset = localStorage.getItem("newAsset");
                let editAsset = localStorage.getItem("editAsset");
                let assetList = JSON.parse(JSON.stringify(this.state.assetList));
                if (newAsset !== null) {
                  newAsset = JSON.parse(newAsset);
                  assetList = assetList.filter((e) => e.assetCode !== newAsset.assetCode);
                  if (assetList.length === this.state.assetList.length) {
                    assetList.pop();
                  }
                  assetList.unshift(newAsset);
                  localStorage.removeItem("newAsset");
                  this.setState({ assetList: assetList });
                } else if (editAsset !== null) {
                  editAsset = JSON.parse(editAsset);
                  assetList = assetList.filter((e) => e.assetCode !== editAsset.assetCode);
                  let categoryFound = this.state.categoryList.find((e) => e.categoryCode === editAsset.categoryId);
                  editAsset.category = categoryFound;
                  if (assetList.length === this.state.assetList.length) {
                    assetList.pop();
                  }
                  assetList.unshift(editAsset);
                  localStorage.removeItem("editAsset");
                  this.setState({ assetList: assetList });
                }
              }
            );
          }
        }
      })
      .catch(() => {
        this.setState({
          isFail: true,
          messageFail: "Fail to load asset.",
        });
      });
  }

  loadCategory() {
    let url = "category";
    get(url)
      .then((response) => {
        if (response.status === 200) {
          if (response.data.successCode === "CATEGORIES_LOADED_SUCCESS") {
            this.setState({
              categoryList: response.data.data,
            });
          }
        }
      })
      .catch(() => {
        this.setState({
          isFail: true,
          messageFail: "Fail to load category.",
        });
      });
  }

  returnPageList() {
    let stateList = this.returnStateList();
    let selectedCategory = this.returnSelectedCategory();
    let url = "asset/count/filter-search-sort";
    let body = {
      states: stateList,
      categoriesCode: selectedCategory,
      sortField: this.state.sortField,
      sortType: this.state.sortType,
      searchKeyWord: this.state.searchValue,
    };
    post(url, body)
      .then((response) => {
        if (response.status === 200) {
          if (response.data.successCode === "ASSET_COUNT_SUCCESS") {
            let numberOfAssets = response.data.data.numberOfEntity;
            let page = [];
            if (numberOfAssets > this.state.sizeInPage) {
              let totalPage = Math.floor(numberOfAssets / this.state.sizeInPage);
              if (numberOfAssets % this.state.sizeInPage != 0) {
                totalPage++;
              }
              for (var i = 0; i < totalPage; i++) {
                page.push(i + 1);
              }
            }
            if (page.length === 0) {
              this.setState({
                pageList: page,
                totalPages: 1,
              });
            } else {
              this.setState({
                pageList: page,
                totalPages: page.length,
              });
            }
          }
        }
      })
      .catch(() => {
        this.setState({
          isFail: true,
          messageFail: "Fail to load pagination.",
        });
      });
  }

  search(e) {
    this.setState(
      {
        currentPage: 1,
        searchValue: e.target.value,
      },
      () => {
        this.loadData();
        this.returnPageList();
      }
    );
  }

  sortByAssetCode() {
    this.setState(
      {
        sortField: "assetCode",
        sortType: this.state.sortType === "ASC" ? "DSC" : "ASC",
      },
      () => {
        this.loadData();
      }
    );
  }

  sortByAssetName() {
    this.setState(
      {
        sortField: "assetName",
        sortType: this.state.sortType === "ASC" ? "DSC" : "ASC",
      },
      () => {
        this.loadData();
      }
    );
  }

  sortByCategory() {
    this.setState(
      {
        sortField: "category",
        sortType: this.state.sortType === "ASC" ? "DSC" : "ASC",
      },
      () => {
        this.loadData();
      }
    );
  }

  sortByState() {
    this.setState(
      {
        sortField: "state",
        sortType: this.state.sortType === "ASC" ? "DSC" : "ASC",
      },
      () => {
        this.loadData();
      }
    );
  }

  changePage(page) {
    this.setState(
      {
        currentPage: page,
      },
      () => {
        this.loadData();
        this.returnPageList();
      }
    );
  }

  handleSelectCategory(e) {
    let list = this.state.selectedCategory;
    if (e.target.checked) {
      if (list.indexOf(e.target.value) === -1) {
        list.push(e.target.value);
      }
    } else {
      let index = list.indexOf(e.target.value);
      if (index > -1) {
        list.splice(index, 1);
      }
    }
    this.setState({ selectedCategory: list });
  }

  toggleShowButton(assetCode) {
    this.setState({
      modal: !this.state.modal,
      assetId: assetCode,
    });
  }

  toggleShow() {
    this.setState({
      modal: !this.state.modal,
      assetId: "",
    });
  }

  reloadPage() {
    this.setState({ currentPage: 1 }, () => {
      this.loadData();
      this.returnPageList();
    });
  }

  handleEditAsset(assetCode, assetState) {
    this.props.history.push(`editasset/${assetCode}`);
  }

  handleDeleteAssetShow = () => {
    this.setState({
      deleteAssetNO: false,
      deleteAssetYES: false,
    });
    this.reloadPage();
  };

  handleDeleteAssetNO = () => {
    this.setState({
      deleteAssetNO: true,
      deleteAssetYES: false,
    });
  };

  handleDeleteAssetYES = () => {
    this.setState({
      deleteAssetNO: false,
      deleteAssetYES: true,
    });
  };

  handleDeleteAsset(assetCode) {
    this.setState({
      deleteCode: assetCode,
    });
    let Url = `asset/check/${assetCode}`;
    del(Url)
      .then((response) => {
        if (response.data.successCode === "ASSET_ABLE_TO_DELETE") this.handleDeleteAssetYES();
      })
      .catch((error) => {
        if (error.response.data.errorCode === "ERR_ASSET_ALREADY_HAVE_ASSIGNMENT") this.handleDeleteAssetNO();
      });
  }

  createAsset() {
    this.props.history.push(URL.CREATE_ASSET);
  }

  prePage() {
    if (this.state.currentPage - 1 >= 1) {
      this.setState(
        {
          currentPage: this.state.currentPage - 1,
        },
        () => {
          this.loadData();
        }
      );
    }
  }

  nextPage() {
    if (this.state.currentPage + 1 <= this.state.totalPages) {
      this.setState(
        {
          currentPage: this.state.currentPage + 1,
        },
        () => {
          this.loadData();
        }
      );
    }
  }

  render() {
    return (
      <div>
        <Navbar businessName=" Manage Asset" />
        <Menu business={business.MANAGE_ASSET} />
        <div className="title">
          <b>Asset List</b>
        </div>
        <div className="filter">
          <div className="filter-state">
            <UncontrolledDropdown>
              <DropdownToggle
                style={{
                  backgroundColor: "white",
                  color: "grey",
                  width: "200px",
                  textAlign: "left",
                }}
              >
                State
                <span
                  style={{
                    float: "right",
                    borderLeft: "1px solid #6c757d",
                    paddingLeft: "8px",
                  }}
                >
                  <img src={FilterIcon} width="20px" />
                </span>
              </DropdownToggle>
              <DropdownMenu style={{ width: "200px", backgroundColor: "rgba(239,241,245,1)" }}>
                <div>
                  <input
                    style={{ marginRight: "10px", marginLeft: "10px" }}
                    type="checkbox"
                    checked={this.state.allState === true}
                    onChange={(e) => {
                      this.setState({ allState: e.target.checked }, () => {
                        this.reloadPage();
                      });
                    }}
                  />
                  All
                </div>
                <div>
                  <input
                    style={{ marginRight: "10px", marginLeft: "10px" }}
                    type="checkbox"
                    checked={this.state.assigned === true}
                    onChange={(e) => {
                      this.setState({ assigned: e.target.checked }, () => {
                        this.reloadPage();
                      });
                    }}
                  />
                  Assigned
                </div>
                <div>
                  <input
                    style={{ marginRight: "10px", marginLeft: "10px" }}
                    type="checkbox"
                    checked={this.state.available === true}
                    onChange={(e) => {
                      this.setState({ available: e.target.checked }, () => {
                        this.reloadPage();
                      });
                    }}
                  />
                  Available
                </div>
                <div>
                  <input
                    style={{ marginRight: "10px", marginLeft: "10px" }}
                    type="checkbox"
                    checked={this.state.notAvailable === true}
                    onChange={(e) => {
                      this.setState({ notAvailable: e.target.checked }, () => {
                        this.reloadPage();
                      });
                    }}
                  />
                  Not available
                </div>
                <div>
                  <input
                    style={{ marginRight: "10px", marginLeft: "10px" }}
                    type="checkbox"
                    checked={this.state.waiting === true}
                    onChange={(e) => {
                      this.setState({ waiting: e.target.checked }, () => {
                        this.reloadPage();
                      });
                    }}
                  />
                  Waiting for recycling
                </div>
                <div>
                  <input
                    style={{ marginRight: "10px", marginLeft: "10px" }}
                    type="checkbox"
                    checked={this.state.recycled === true}
                    onChange={(e) => {
                      this.setState({ recycled: e.target.checked }, () => {
                        this.reloadPage();
                      });
                    }}
                  />
                  Recycled
                </div>
              </DropdownMenu>
            </UncontrolledDropdown>
          </div>
          <div className="filter-category">
            <UncontrolledDropdown>
              <DropdownToggle
                style={{
                  backgroundColor: "white",
                  color: "grey",
                  width: "200px",
                  textAlign: "left",
                }}
              >
                Category
                <span
                  style={{
                    float: "right",
                    borderLeft: "1px solid #6c757d",
                    paddingLeft: "8px",
                  }}
                >
                  <img src={FilterIcon} width="20px" />
                </span>
              </DropdownToggle>
              <DropdownMenu style={{ width: "200px", backgroundColor: "rgba(239,241,245,1)" }}>
                <div>
                  <input
                    style={{ marginRight: "10px", marginLeft: "10px" }}
                    type="checkbox"
                    checked={this.state.allCategory === true}
                    onChange={(e) => {
                      this.setState({ allCategory: e.target.checked }, () => {
                        this.reloadPage();
                      });
                    }}
                  />
                  All
                </div>
                {this.state.categoryList.map((category) => {
                  let isSelected = this.state.selectedCategory.findIndex((selected) => selected === category.categoryCode);
                  return (
                    <div key={category.categoryCode} id={category.categoryCode}>
                      <input
                        style={{ marginRight: "10px", marginLeft: "10px" }}
                        type="checkbox"
                        value={category.categoryCode}
                        checked={isSelected > -1}
                        onChange={(e) => {
                          this.handleSelectCategory(e);
                          this.reloadPage();
                        }}
                      />
                      {category.categoryName}
                    </div>
                  );
                })}
              </DropdownMenu>
            </UncontrolledDropdown>
          </div>
          <div className="search">
            <Input type="text" style={{ width: "200px", height: "34px", paddingRight: "36px" }} onChange={(e) => this.search(e)} />
            <div className="search-icon">
              <img src={SearchIcon} width="20px" />
            </div>
          </div>
          <div className="create-asset">
            <Button color="danger" onClick={() => this.createAsset()}>
              Create new asset
            </Button>
          </div>
        </div>
        <div className="table-content">
          <Table hover>
            <thead>
              <tr>
                <th style={{ cursor: "pointer" }} onClick={() => this.sortByAssetCode()}>
                  Asset Code
                  <img src={DropdownIcon} width="12px" />
                </th>
                <th style={{ cursor: "pointer" }} onClick={() => this.sortByAssetName()}>
                  Asset Name
                  <img src={DropdownIcon} width="12px" />
                </th>
                <th style={{ cursor: "pointer" }} onClick={() => this.sortByCategory()}>
                  Category
                  <img src={DropdownIcon} width="12px" />
                </th>
                <th style={{ cursor: "pointer" }} onClick={() => this.sortByState()}>
                  State
                  <img src={DropdownIcon} width="12px" />
                </th>
                <th></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {this.state.assetList.map((asset) => {
                return (
                  <tr key={asset.assetCode} id={asset.assetCode}>
                    <td onClick={() => this.toggleShowButton(`${asset.assetCode}`)}>{asset.assetCode}</td>
                    <td onClick={() => this.toggleShowButton(`${asset.assetCode}`)}>{asset.assetName}</td>
                    <td onClick={() => this.toggleShowButton(`${asset.assetCode}`)}>{asset.category.categoryName}</td>
                    <td onClick={() => this.toggleShowButton(`${asset.assetCode}`)}>
                      {asset.state === 1 && <span>Available</span>}
                      {asset.state === 2 && <span>Not Available</span>}
                      {asset.state === 3 && <span>Assigned</span>}
                      {asset.state === 4 && <span>Waiting for recycling</span>}
                      {asset.state === 5 && <span>Recycled</span>}
                    </td>
                    <td>
                      <button
                        className="click-button"
                        onClick={() => this.handleEditAsset(`${asset.assetCode}`, asset.state)}
                        disabled={asset.state === 3 ? true : false}
                      >
                        <img className={asset.state === 3 ? "asset-disable-icon" : "edit-icon"} src={EditIcon} width="20px" />
                      </button>
                    </td>
                    <td>
                      <button className="click-button" onClick={() => this.handleDeleteAsset(asset.assetCode)} disabled={asset.state === 3 ? true : false}>
                        <img className={asset.state === 3 ? "asset-disable-icon" : "delete-icon"} src={DeleteIcon} width="24px" />
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </Table>
        </div>
        <div className="paging">
          {this.state.assetList.length > 0 && (
            <div className={this.state.currentPage === 1 ? "pre-next pre-next-disabled" : "pre-next"} onClick={() => this.prePage()}>
              {"<"}
            </div>
          )}
          {this.state.pageList.length > 1 &&
            this.state.pageList.map((page) => {
              let isActive = this.state.currentPage === page;
              return (
                <div id={page} key={page} className={isActive ? "current-page" : "paging-page"} onClick={() => this.changePage(page)}>
                  {page}
                </div>
              );
            })}
          {this.state.totalPages < 2 && this.state.assetList.length > 0 && <div className="current-page">1</div>}
          {this.state.assetList.length > 0 && (
            <div className={this.state.currentPage === this.state.totalPages ? "pre-next pre-next-disabled" : "pre-next"} onClick={() => this.nextPage()}>
              {">"}
            </div>
          )}
        </div>
        {this.state.assetList.length === 0 && (
          <div className="fail">
            <Alert color="secondary">No results.</Alert>
          </div>
        )}
        {this.state.isFail === true && (
          <div className="fail">
            <Alert color="danger">{this.state.messageFail}</Alert>
          </div>
        )}
        {this.state.user === "" && this.props.history.push(URL.LOGIN)}
        {this.state.user.role !== role.ROLE_ADMIN && this.props.history.push(URL.LOGIN)}
        <Modal size="lg" isOpen={this.state.modal} toggle={() => this.toggleShow()}>
          <ModalHeader style={{ backgroundColor: "rgba(239,241,245,1)", color: "red" }}>
            Detailed Asset Information
            <span className="close-asset-detail" onClick={() => this.toggleShow()}>
              X
            </span>
          </ModalHeader>
          <ModalBody>
            <AssetDetail id={this.state.assetId} />
          </ModalBody>
        </Modal>
        <DeleteAsset_Yes assetCode={this.state.deleteCode} handleDeleteAssetShow={this.handleDeleteAssetShow} deleteAssetYES={this.state.deleteAssetYES} />
        <DeleteAsset_No assetCode={this.state.deleteCode} handleDeleteAssetShow={this.handleDeleteAssetShow} deleteAssetNO={this.state.deleteAssetNO} />
      </div>
    );
  }
}
export default withCookies(withRouter(ManageAsset));
