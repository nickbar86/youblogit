import React from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import Link from "@material-ui/core/Link";
import Grid from "@material-ui/core/Grid";
import Box from "@material-ui/core/Box";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import { reduxForm, Field } from "redux-form";
import MaterializeTextField from "./../fields/MaterializeTextField";
import { withStyles } from "@material-ui/core/styles";
import { signIn } from "./../../actions/applicationActions";
import { connect } from "react-redux";
import { compose } from "redux";
function Copyright() {
  return (
    <Typography variant="body2" color="textSecondary" align="center">
      {"Copyright © "}
      <Link color="inherit" href="https://material-ui.com/">
        YouBlog IT
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

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

class SignUp extends React.Component {
  onSubmit = formProps => {
    this.props.signIn(formProps.email, formProps.password, () => {
      this.props.history.push("/blog");
    });
  };
  debugger;

  render() {
    debugger;
    const { classes, handleSubmit } = this.props;
    return (
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <form className={classes.form} noValidate>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <Field
                  name="name"
                  id="name"
                  label="Display Name"
                  component={MaterializeTextField}
                  autoComplete="name"
                  required
                />
              </Grid>
              <Grid item xs={12}>
                <Field
                  name="email"
                  id="email"
                  label="Email Address"
                  component={MaterializeTextField}
                  autoComplete="email"
                  required
                />
              </Grid>
              <Grid item xs={12}>
                <Field
                  name="password"
                  id="password"
                  label="Password"
                  component={MaterializeTextField}
                  autoComplete="current-password"
                  type="password"
                  required
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
            >
              Sign Up
            </Button>
            <Grid container justify="flex-end">
              <Grid item>
                <Link href="/user/signin" variant="body2">
                  Already have an account? Sign in
                </Link>
              </Grid>
            </Grid>
          </form>
        </div>
        <Box mt={5}>
          <Copyright />
        </Box>
      </Container>
    );
  }
}
SignUp = compose(
  connect(
    null,
    { signIn }
  ),
  reduxForm({ form: "signup" })
)(SignUp);
export default withStyles(styles)(SignUp);
