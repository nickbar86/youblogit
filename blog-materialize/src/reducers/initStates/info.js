import * as jwt from "jsonwebtoken";
const decoded = jwt.decode(localStorage.getItem("token"), { complete: true });
export const initState = {
  httpMessage: { title: "", message: "" },
  type: "",
  isFetchingQueue: [],
  authenticated: decoded ? true : false,
  user: {
    mail: decoded ? decoded.payload.sub : null,
    authorities: decoded ? [...decoded.payload.authorities] : []
  },
  userProfile: {}
};

export const resetState = {};
