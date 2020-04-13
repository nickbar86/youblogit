import React, {Component} from 'react';
import {connect} from "react-redux";
import * as actions from "./../../actions/applicationActions"
class SignOut extends Component{
  componentDidMount() {
    this.props.signOut();
  }
  render(){
    return(
      <div>Sorry top see you go!</div>
    )
  }
}
export default connect(null, actions)(SignOut);
