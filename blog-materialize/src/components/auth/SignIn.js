import React from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import Link from "@material-ui/core/Link";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import { reduxForm, Field } from "redux-form";
import MaterializeTextField from "./../fields/materializeTextField";
import { withStyles } from "@material-ui/core/styles";
import { signIn } from "./../../actions/applicationActions";
import { connect } from "react-redux";
import { compose } from "redux";

const styles = theme => {
  return {
    paper: {
      marginTop: theme.spacing(8),
      display: "flex",
      flexDirection: "column",
      alignItems: "center"
    },
    avatar: {
      margin: theme.spacing(1),
      backgroundColor: theme.palette.secondary.main
    },
    form: {
      width: "100%", // Fix IE 11 issue.
      marginTop: theme.spacing(1)
    },
    submit: {
      margin: theme.spacing(3, 0, 2)
    }
  };
};

class SignIn extends React.Component {
  onSubmit = formProps => {
    this.props.signIn(formProps.email, formProps.password, () => {
      this.props.history.push("/user/blog");
    });
  };

  render() {
    const { classes, handleSubmit } = this.props;
    return (
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <form className={classes.form} onSubmit={handleSubmit(this.onSubmit)}>
            <Field
              name="email"
              id="email"
              label="Email Address"
              component={MaterializeTextField}
              autoComplete="email"
              required
            />
            <Field
              name="password"
              id="password"
              label="Password"
              component={MaterializeTextField}
              autoComplete="current-password"
              type="password"
              required
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
                <Link href="#" variant="body2">
                  Forgot password?
                </Link>
              </Grid>
              <Grid item>
                <Link href="/user/signup" variant="body2">
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </form>
        </div>
      </Container>
    );
  }
}
SignIn = compose(
  connect(
    null,
    { signIn }
  ),
  reduxForm({ form: "signin" })
)(SignIn);
export default withStyles(styles)(SignIn);
