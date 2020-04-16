import React from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import { reduxForm, Field, formValueSelector } from "redux-form";
import MaterializeTextField from "./../fields/materializeTextField";
import { withStyles } from "@material-ui/core/styles";
import { compose } from "redux";
import { connect } from "react-redux";
import { DateTimePicker } from "@material-ui/pickers";

const styles = theme => {
  return {
    paper: {
      marginTop: theme.spacing(4),
      display: "flex",
      flexDirection: "column",
      alignItems: "center"
    },
    avatarUnlocked: {
      margin: theme.spacing(1),
      backgroundColor: theme.palette.primary.main
    },
    avatarLocked: {
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

class Profile extends React.Component {
  onSubmit = formProps => {
    this.props.updateUserProfile(
      formProps.name,
      formProps.email,
      formProps.password
    );
  };

  render() {
    const { classes, handleSubmit, submitting, formValues } = this.props;
    return (
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <div className={classes.paper}>
          <Avatar
            className={
              formValues.enabled ? classes.avatarUnlocked : classes.avatarLocked
            }
          >
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            {`Hello ${formValues.name}`}
          </Typography>
          <form className={classes.form} onSubmit={handleSubmit(this.onSubmit)}>
            <Grid container spacing={2}>
              <Grid item xs={6}>
                <DateTimePicker
                  variant="inline"
                  label="Date Created"
                  value={formValues.created}
                  disabled
                />
              </Grid>
              <Grid item xs={6}>
                <DateTimePicker
                  variant="inline"
                  label="Date Updated"
                  value={formValues.modified}
                  disabled
                />
              </Grid>

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
                  label="New Password"
                  component={MaterializeTextField}
                  //autoComplete="current-password"
                  type="password"
                  required
                />
              </Grid>
              <Grid item xs={12}>
                <Field
                  name="password2"
                  id="password2"
                  label="Retype Password"
                  component={MaterializeTextField}
                  //autoComplete="current-password"
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
              disabled={submitting}
              className={classes.submit}
            >
              Update
            </Button>
          </form>
        </div>
      </Container>
    );
  }
}
function validate({ password, password2 }) {
  const errors = {};
  if (password !== password2) {
    errors.password = "Passwords do not match";
    errors.password2 = "Passwords do not match";
  }
  return errors;
}
const selector = formValueSelector("profile");
function mapStateToProps(state, props) {
  return {
    initialValues: props.userProfile,
    enableReinitialize: true,
    formValues: selector(
      state,
      "name",
      "role",
      "enabled",
      "created",
      "modified"
    )
  };
}

Profile = compose(
  connect(
    mapStateToProps,
    null
  ),
  reduxForm({ form: "profile", validate })
)(Profile);
export default withStyles(styles)(Profile);
