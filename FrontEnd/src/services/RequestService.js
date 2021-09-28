import { get, post } from "../httpHelper";

export function getRequest(url) {
  return get(url);
}

export function countRequest(url) {
  return get(url);
}

export function getRequestFilterSearchSort(url, body) {
  return post(url, body);
}

export function countRequestFilterSearchSort(url, body) {
  return post(url, body);
}
