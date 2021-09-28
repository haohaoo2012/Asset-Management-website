/*export const EndPoint = "http://localhost:9994/asset-management";*/
export const EndPoint =
  process.env.REACT_APP_BACKEND_URL ||
  "https://java-backend-group03-test.azurewebsites.net/asset-management";

// ASSET
export const MANAGE_ASSET = "/manageasset";
export const CREATE_ASSET = "/create-asset";
export const EDIT_ASSET = "/editasset/:assetCode";
export const ASSET_REPORT = "/report";
//Assignment
export const MANAGE_ASSIGNMENT = "/manageassignment";
export const CREATE_ASSIGNMENT = "/creatnewasssignment";
export const EDIT_ASSIGNMENT = "/edit-assignment/:assignmentId";

// USER
export const MANAGE_USER = "/manageuser";
export const CREATE_USER = "/createnewuser";
export const EDIT_USER = "/edituser/:staffCode";

// HOME
export const HOME_ADMIN = "/admin";
export const HOME_USER = "/user";
export const CHANGE_PASS = "/changepassword";

//Login
export const LOGIN = "/";
export const FIRST_LOGIN = "/first";

//Request
export const MANAGE_REQUEST = "/manage-request";

//------------------------ API ---------------------------------
// CATEGORY
export const GET_CATEGORY = "category";
export const CREATE_CATEGORY = "category";

//ASSET
export const CREATE_ASSET_API = "asset";

//CATEGORY
export const GET_REQUEST_API = "request";
export const COUNT_REQUEST_API = "request/count";
export const GET_REQUEST_FILTER_SEARCH_SORT_API = "request/filter-search-sort";
export const COUNT_REQUEST_FILTER_SEARCH_SORT_API = "request/count/filter-search-sort";
