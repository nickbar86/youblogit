import React, { Component } from "react";
import { connect } from "react-redux";
import Header from "./../components/Header";
import ErrorBoundary from "./../components/ErrorBoundary";

class HeaderContainer extends Component {
  render() {
    return (
      <ErrorBoundary>
        <Header {...this.props} />
      </ErrorBoundary>
    );
  }
}
function mapStateToPros({ info: { authenticated } }, { location }) {
  return {
    authenticated
  };
}
export default connect(mapStateToPros)(HeaderContainer);
