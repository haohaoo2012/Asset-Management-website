import { get, post } from "../httpHelper";

export function getCategory(url) {
  return get(url);
}

export function createNewCategory(url, body) {
  return post(url, body);
}
