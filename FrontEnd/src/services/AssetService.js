import { post } from "../httpHelper";

export function createNewAsset(url, body) {
  return post(url, body);
}
