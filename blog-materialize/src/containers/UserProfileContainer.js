import React, { Component } from "react";
import { connect } from "react-redux";
import * as applicationActions from "../actions/applicationActions";
import Profile from "./../components/auth/Profile";
import ErrorBoundary from "./../components/ErrorBoundary";

class UserProfileContainer extends Component {
  componentDidMount() {
    this.props.fetchUserProfile();
  }
  render() {
    return (
      <ErrorBoundary>
        <Profile {...this.props} />
      </ErrorBoundary>
    );
  }
}
function mapStateToPros({ info: { userProfile } }) {
  return { userProfile };
}
export default connect(
  mapStateToPros,
  {
    ...applicationActions
  }
)(UserProfileContainer);
