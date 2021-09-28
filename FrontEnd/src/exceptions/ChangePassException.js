import * as ErrorCode from "../constants/ErrorCode";

export function changePassFailException(error) {
  if (error.response.data.message !== undefined || error.response.data.errorCode !== undefined) {
    if (error.response.data.message === ErrorCode.ERR_USER_NOT_FOUND || error.response.data.errorCode === ErrorCode.ERR_USER_NOT_FOUND) {
      return "User not found";
    }
    if (error.response.data.message === ErrorCode.ERR_PASSWORD_NOT_CORRECT || error.response.data.errorCode === ErrorCode.ERR_PASSWORD_NOT_CORRECT) {
      return "Password is incorrect";
    }
    if (error.response.data.message === ErrorCode.ERR_PASSWORD_IS_EMPTY || error.response.data.errorCode === ErrorCode.ERR_PASSWORD_IS_EMPTY) {
      return "Password cannot empty";
    }
    if (error.response.data.message === ErrorCode.ERR_SAME_PASSWORD || error.response.data.errorCode === ErrorCode.ERR_SAME_PASSWORD) {
      return "Old and new password must be different";
    }
    if ((error.response.data.message === ErrorCode.ERR_CHANGE_PASSWORD) | (error.response.data.errorCode === ErrorCode.ERR_CHANGE_PASSWORD)) {
      return "Fail to change password, try again later";
    }
    return error.response.data.message;
  }
  return "Fail to change password, try again later";
}
