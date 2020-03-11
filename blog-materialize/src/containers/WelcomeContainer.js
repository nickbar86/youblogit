import React, { Component } from "react";
import { connect } from "react-redux";
import Container from "@material-ui/core/Container";
import { Link } from "react-router-dom";
class WelcomeContainer extends Component {
  render() {
    return (
      <Container style={{ textAlign: "center", marginTop: "30px" }}>
        <h2>Welcome</h2>
        <Link to="/blog">
          {" "}
          <h5>START</h5>
        </Link>
      </Container>
    );
  }
}
function mapStateToPros({ auth }) {
  return {}
}
export default connect(mapStateToPros, null)(WelcomeContainer);
