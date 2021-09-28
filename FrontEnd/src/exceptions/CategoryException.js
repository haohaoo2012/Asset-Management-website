import * as ErrorCode from "../constants/ErrorCode";

export function getCategoryFailException(error) {
  if (error.response === undefined) {
    return "Fail to get category, try again later";
  }
  if (error.response.data.errorCode !== undefined) {
    if (error.response.data.errorCode === ErrorCode.ERR_RETRIEVE_CATEGORY_FAIL) {
      return "Fail to get category, try again later";
    }
    return error.response.data.errorCode;
  }
  if (error.response.data.message !== undefined) {
    return error.response.data.message;
  }
  return "Fail to get parent category, try again later";
}

export function createCategoryFailException(error) {
  if (error.response === undefined) {
    return "Fail to create category, try again later";
  }
  if (error.response.data.errorCode !== undefined) {
    if (error.response.data.errorCode === ErrorCode.ERR_CATEGORY_NAME_EXISTED) {
      return "Category name exist, please choose another";
    }
    if (error.response.data.errorCode === ErrorCode.ERR_CATEGORY_CODE_EXISTED) {
      return "Category prefix exist, please choose another";
    }

    return error.response.data.errorCode;
  }
  if (error.response.data.message !== undefined) {
    return error.response.data.message;
  }
  return "Fail to get create category, try again later";
}
