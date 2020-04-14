import React from "react";
import { Route, Switch } from "react-router-dom";
import SignIn from "./SignIn";
import SignUp from "./SignUp";
import SignOut from "./SignOut";
import UserProfileContainer from "./../../containers/UserProfileContainer"
import PrivateRoute from "./../PrivateRoute";
import { connect } from "react-redux";
export const Routes = ({ match, history, authenticated }) => {
  debugger;
  return (
    <Switch>
      <Route exact path={`${match.path}/signin`} component={SignIn} />
      <Route exact path={`${match.path}/signup`} component={SignUp} />
      <Route exact path={`${match.path}/recoverpass`} component={SignIn} />
      <Route exact path={`${match.path}/signout`} component={SignOut} />
      <PrivateRoute
        exact
        path={`${match.path}/profile`}
        isAllowed={authenticated}
        component={UserProfileContainer}
      />
    </Switch>
  );
};
function mapStateToProps({ info }) {
  return { authenticated: info.authenticated };
}
export default connect(
  mapStateToProps,
  null
)(Routes);
