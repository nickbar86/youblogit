import axios from "axios";
import { toast } from "react-toastify";
function callApi(endpoint, method, headers, data, params) {
  return axios({
    baseURL: "https://localhost:8443",
    method: method,
    url: endpoint,
    headers: headers,
    data: data
  });
}

export const CALL_API = Symbol("Call API");

export default store => next => action => {
  const callAPI = action[CALL_API];

  // So the middleware doesn't get applied to every single action
  if (typeof callAPI === "undefined") {
    return next(action);
  }

  let { endpoint, types, method, headers, data, params } = callAPI;
  const [requestType, successType, errorType, commandType] = types;
  // UI is waiting for response
  store.dispatch({ type: requestType, params: params });

  return callApi(endpoint, method, headers, data, params).then(
    response => {

      store.dispatch({
        type: successType,
        params: params
      });

      const { statusText, status } = response;
      if (statusText === "OK" || status === 200) {
        debugger
        store.dispatch({
          payload: response.data,
          type: commandType,
          title: "Succesfull Request",
          message: response.statusText,
          headers: response.headers,
          params: params
        });
      } else {
        toast.error(
          `Error Notification !: ${
            response.statusText
          } There was an error.`,
          {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 8000
          }
        );
        store.dispatch({
          type: errorType,
          title: "There was an error.",
          message: response.statusText,
          params: params
        });
      }
    },
    error => {
      console.log(error)
      debugger
      toast.error(
        `Error Notification !: ${
          error.message
            ? error.message
            : "There was an error"
        }.`,
        {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 8000
        }
      );
      store.dispatch({
        type: errorType,
        title: "There was an error.",
        message: error.response ? error.response.statusText:error,
        params: params
      });
    }
  );
};
