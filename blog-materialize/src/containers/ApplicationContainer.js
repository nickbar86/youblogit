import React, { Component } from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import CssBaseline from "@material-ui/core/CssBaseline";
import Container from "@material-ui/core/Container";
import { withStyles } from "@material-ui/core/styles";
import { connect } from "react-redux";
import * as actions from "../actions";
import * as applicationActions from "../actions/applicationActions";
import WelcomeContainer from "../containers/WelcomeContainer";
import Footer from "../components/Footer";
import NoMatch from "../components/NoMatch";
import NotAllowed from "../components/NotAllowed";
import PrivateRoute from "./../components/PrivateRoute";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import BlogContainer from "./BlogContainer";
import BlogListContainer from "./BlogListContainer";
import HeaderContainer from "./HeaderContainer";
import Authentication from "./../components/auth";
//const AsyncCasesRoutes = asyncComponent(() => import("./cases"));
const styles = theme => {
  return {
    root: {
      display: "flex",
      flexDirection: "column",
      minHeight: "100vh"
    },
    main: {
      marginTop: theme.spacing(2),
      marginBottom: theme.spacing(2)
    }
  };
};
class ApplicationContainer extends Component {
  async componentDidMount() {
    //await this.props.connectToWS();
  }

  render() {
    const { classes } = this.props;
    return (
      <div>
        <CssBaseline />
        <BrowserRouter className={classes.main}>
          <div className={classes.root}>
            <HeaderContainer />
            <Container component="main" className={classes.main} maxWidth="lg">
              <Switch>
                <Route exact path="/" component={WelcomeContainer} />
                <Route exact path="/blog" component={BlogListContainer} />
                <Route exact path="/blog/:id" component={BlogContainer} />
                <Route path="/user" component={Authentication} />
                <Route exact path="/forbidden" component={NotAllowed} />
                <Route component={NoMatch} />
              </Switch>
            </Container>
            <Footer />
          </div>
          <ToastContainer />
        </BrowserRouter>
      </div>
    );
  }
}
function mapStateToProps({ auth }) {
  return { auth };
}
const mapDispatchToProps = {
  ...actions,
  ...applicationActions
};
ApplicationContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ApplicationContainer);

export default withStyles(styles)(ApplicationContainer);
