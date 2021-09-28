import * as ErrorCode from "../constants/ErrorCode";

export function createAssetFailException(error) {
  if (error.response === undefined) {
    return "Fail to create category, try again later";
  }
  if (error.response.data.errorCode !== undefined) {
    if (error.response.data.errorCode === ErrorCode.ERR_CATEGORY_NAME_EXISTED) {
      return "Category name exist, please choose another";
    }

    return error.response.data.errorCode;
  }
  if (error.response.data.message !== undefined) {
    return error.response.data.message;
  }
  return "Fail to get create category, try again later";
}
