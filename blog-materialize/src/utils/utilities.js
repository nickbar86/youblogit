import axios from "axios";
import React from "react";
import {
  HTTP_FAILURE_QUEUE,
  HTTP_REQUEST_QUEUE,
  HTTP_SUCCESS_QUEUE
} from "../actions/commonActions";
import { CALL_API } from "../middlewares/api";
import { toast } from "react-toastify";

export const isFetchingQueue = (fetchingQueue, key) => {
  return fetchingQueue.filter(queueKey => queueKey === key).length > 0;
};

export const constructParameters = (array1, array2) => {
  return array1
    .map((element, index) => {
      return array2[index] ? element + "=" + array2[index] : "";
    })
    .filter(param => param.length > 0)
    .join("&");
};

export const commonApiGetObject = (url, action) => {
  return {
    [CALL_API]: {
      endpoint: url,
      method: "GET",
      headers: { "Content-Type": "application/json" },
      types: [
        HTTP_REQUEST_QUEUE,
        HTTP_SUCCESS_QUEUE,
        HTTP_FAILURE_QUEUE,
        action
      ],
      params: { key: action }
    }
  };
};

export const commonApiPostObject = (url, data, action) => {
  return {
    [CALL_API]: {
      endpoint: url,
      method: "POST",
      headers: { "Content-Type": "application/json" },
      data: data,
      types: [
        HTTP_REQUEST_QUEUE,
        HTTP_SUCCESS_QUEUE,
        HTTP_FAILURE_QUEUE,
        action
      ],
      params: { key: action }
    }
  };
};

export const commonApiDeleteObject = (url, action) => {
  return {
    [CALL_API]: {
      endpoint: url,
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
      types: [
        HTTP_REQUEST_QUEUE,
        HTTP_SUCCESS_QUEUE,
        HTTP_FAILURE_QUEUE,
        action
      ],
      params: { key: action }
    }
  };
};

export const accessNullSafeFalse = func => {
  try {
    return func();
  } catch (e) {
    return false;
  }
};

export const accessNullSafeTrue = func => {
  try {
    return func();
  } catch (e) {
    return true;
  }
};

export const accessNullSafe = func => {
  try {
    return func();
  } catch (e) {
    return null;
  }
};

export const accessNullSafeGet0 = func => {
  try {
    return func();
  } catch (e) {
    return 0;
  }
};

export const accessNullSafeGetEmpty = func => {
  try {
    return func();
  } catch (e) {
    return "";
  }
};

export const showToast = msg => {
  toast.success(msg, {
    position: toast.POSITION.TOP_RIGHT,
    autoClose: 2000
  });
};

export const showStickyToast = msg => {
  toast.success(msg, {
    position: toast.POSITION.TOP_RIGHT,
    autoClose: false
  });
};

export function sendingHttpRequest(key) {
  return {
    type: "HTTP_REQUEST_QUEUE",
    params: { key: key }
  };
}
export function httpRequestSuccess(key) {
  return {
    type: "HTTP_SUCCESS_QUEUE",
    params: { key: key }
  };
}
export function httpRequestFailure(key) {
  return {
    type: "HTTP_FAILURE_QUEUE",
    params: { key: key }
  };
}

export const fetchAndReturnValue = (urlApi, action) => async dispatch => {
  dispatch(sendingHttpRequest(action));
  try {
    const resp = await axios.get(urlApi);
    if (resp) {
      dispatch(httpRequestSuccess(action));
      return resp.data;
    }
    return "";
  } catch (e) {
    dispatch(httpRequestFailure(action));
    return "";
  }
};

export const postAndReturnValue = (urlApi, data, action) => async dispatch => {
  dispatch(sendingHttpRequest(action));
  try {
    const resp = await axios.post(urlApi, data);
    if (resp) {
      dispatch(httpRequestSuccess(action));
      return resp.data;
    }
    return "";
  } catch (e) {
    dispatch(httpRequestFailure(action));
    return "";
  }
};
