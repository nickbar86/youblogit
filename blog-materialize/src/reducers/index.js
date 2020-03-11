import { combineReducers } from "redux";
import info from "./applicationReducer";
import blog from "./blogReducer";

export default combineReducers({
  info,
  blog
});
