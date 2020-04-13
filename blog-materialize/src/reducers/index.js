import { combineReducers } from "redux";
import info from "./applicationReducer";
import blog from "./blogReducer";
import { reducer as formReducer } from "redux-form";

export default combineReducers({
  info,
  blog,
  form: formReducer
});
