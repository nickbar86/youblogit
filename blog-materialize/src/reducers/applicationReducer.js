import * as commonActions from "./../actions/commonActions";
import { initState } from "./initStates/info";

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
    debugger
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
    default:
      return { ...state };
  }
}
