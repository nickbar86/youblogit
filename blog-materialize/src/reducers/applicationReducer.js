import * as commonActions from "./../actions/commonActions";
import * as applicationActions from "./../actions/applicationActionTypes";
import { initState } from "./initStates/info";

import * as jwt from "jsonwebtoken";

export default function info(
  state = initState,
  { type, payload, params, message }
) {
  let index;
  let newFetchingQueue;
  switch (type) {
    case commonActions.HTTP_REQUEST_QUEUE:
      return {
        ...state,
        isFetchingQueue: [...state.isFetchingQueue, params.key],
        type: "info",
        httpMessage: "Loading data..."
      };
    case commonActions.HTTP_SUCCESS_QUEUE:
      debugger;
      index = state.isFetchingQueue.findIndex(que => que === params.key);
      newFetchingQueue = [
        ...state.isFetchingQueue.slice(0, index),
        ...state.isFetchingQueue.slice(index + 1)
      ];
      return {
        ...state,
        isFetchingQueue: newFetchingQueue,
        type: "success",
        httpMessage: "Success!"
      };

    case commonActions.HTTP_FAILURE_QUEUE:
      index = state.isFetchingQueue.findIndex(que => que === params.key);
      newFetchingQueue = [
        ...state.isFetchingQueue.slice(0, index),
        ...state.isFetchingQueue.slice(index + 1)
      ];
      return {
        ...state,
        isFetchingQueue: newFetchingQueue,
        type: "error",
        httpMessage: "Error!"
      };
    case applicationActions.SIGNIN:
      const decoded = jwt.decode(payload.access_token, { complete: true });
      debugger;
      //cookie.save('sessionId', token, { path: '/' });
      localStorage.setItem("token", payload.access_token);
      return {
        ...state,
        authenticated: true,
        user: {
          mail: decoded.payload.sub,
          authorities: [...decoded.payload.authorities]
        }
      };
    case applicationActions.SIGNOUT:
      localStorage.removeItem("token");
      return {
        ...state,
        authenticated: false,
        user: {
          mail: null,
          authorities: []
        }
      };
    case applicationActions.SIGNUP:
      return {
        ...state
      };
    case applicationActions.USER_DETAILS:
      return {
        ...state,
        userProfile: payload
      };
    default:
      return { ...state };
  }
}
