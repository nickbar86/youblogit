import React from "react";
import { Route, Switch } from "react-router-dom";
import SignIn from "./SignIn"
import SignUp from "./SignUp"
import SignOut from "./SignOut"
export const Routes = ({ match, history }) => {
  debugger
  return (
    <Switch>
      <Route
        exact
        path={`${match.path}/signin`}
        component={SignIn}
      />
      <Route
        exact
        path={`${match.path}/signup`}
        component={SignUp}
      />
      <Route
        exact
        path={`${match.path}/recoverpass`}
        component={SignIn}
      />
      <Route
        exact
        path={`${match.path}/signout`}
        component={SignOut}
      />
    </Switch>
  );
};

export default Routes;
