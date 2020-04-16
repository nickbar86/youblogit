import axios from "axios";
import { toast } from "react-toastify";

function callApi(endpoint, method, headers, data, params, authenticated) {
  const token = localStorage.getItem("token");
  if (authenticated) {
    if (token) {
      headers["Authorization"] = `Bearer ${token}`;
    } else {
    }
  } else {
    // throw "No token saved!"
  }

  if (params["authenticate"]) {
    //TODO this must come from env var
    const token = Buffer.from("writer:secret", "utf8").toString("base64");
    headers["Authorization"] = `Basic ${token}`;
    return axios({
      baseURL: "https://localhost:8443",
      method: method,
      url: endpoint,
      headers: headers,
      data: data
    });
  } else {
    return axios({
      baseURL: "https://localhost:8443",
      method: method,
      url: endpoint,
      headers: headers,
      data: data
    });
  }
}

export const CALL_API = Symbol("Call API");

export default store => next => action => {
  const callAPI = action[CALL_API];

  // So the middleware doesn't get applied to every single action
  if (typeof callAPI === "undefined") {
    return next(action);
  }

  let {
    endpoint,
    types,
    method,
    headers,
    data,
    params,
    authenticated
  } = callAPI;
  const [requestType, successType, errorType, commandType] = types;
  // UI is waiting for response
  store.dispatch({ type: requestType, params: params });

  return callApi(endpoint, method, headers, data, params, authenticated).then(
    response => {
      store.dispatch({
        type: successType,
        params: params
      });

      const { statusText, status } = response;
      if (statusText === "OK" || status === 200) {
        store.dispatch({
          payload: response.data,
          type: commandType,
          title: "Succesfull Request",
          message: response.statusText,
          headers: response.headers,
          params: params
        });
        if (params.callback) params.callback();
      } else {
        toast.error(
          `Error Notification !: ${response.statusText} There was an error.`,
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
      console.log(error);
      toast.error(
        `Error Notification !: ${
          error.message ? error.message : "There was an error"
        }.`,
        {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 8000
        }
      );
      store.dispatch({
        type: errorType,
        title: "There was an error.",
        message: error.response ? error.response.statusText : error,
        params: params
      });
    }
  );
};
