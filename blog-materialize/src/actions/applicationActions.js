import { CALL_API } from "./../middlewares/api";
import {
  HTTP_FAILURE_QUEUE,
  HTTP_REQUEST_QUEUE,
  HTTP_SUCCESS_QUEUE
} from "./commonActions";
import * as actions from "./applicationActionTypes";
const authUrl = "/oauth/token";

export function signIn(username, password, callback) {
  const loginByPassBody = new URLSearchParams();
  loginByPassBody.append("grant_type", "password");
  loginByPassBody.append("username", username);
  loginByPassBody.append("password", password);
  return {
    [CALL_API]: {
      endpoint: authUrl,
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      data: loginByPassBody,
      authenticated: false,
      types: [
        HTTP_REQUEST_QUEUE,
        HTTP_SUCCESS_QUEUE,
        HTTP_FAILURE_QUEUE,
        actions.SIGNIN
      ],
      params: { key: actions.SIGNIN, authenticate: true, callback }
    }
  };
}

export function signOut() {
  return {
    type: actions.SIGNOUT
  };
}

export function connected() {
  return {
    type: actions.CONNECTED
  };
}

export function disconnected() {
  return {
    type: actions.DISCONNECTED
  };
}

export function connectToWS() {
  return {
    url: `wss://${window.location.hostname}`,
    type: actions.CONNECT
  };
}

export function disconnectFromWS() {
  return {
    type: actions.DISCONNECT
  };
}
