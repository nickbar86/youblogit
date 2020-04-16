import React, { Component } from "react";
import { connect } from "react-redux";
import { EditorState } from "draft-js";
import "react-draft-wysiwyg/dist/react-draft-wysiwyg.css";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";
import "./editorStyles.css";

import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";
import { compose } from "redux";
import { reduxForm, Field } from "redux-form";
import CssBaseline from "@material-ui/core/CssBaseline";
import MaterializeMultilineTextField from "./../fields/materializeMultilineTextField";
import RankingField from "./../fields/rankingField";
import InputHiddenField from "./../fields/inputHiddenField";
const styles = theme => {
  return {
    button: {
      marginTop: theme.spacing(2)
    }
  };
};

class ReviewForm extends Component {
  onSubmit = formProps => {
    debugger
    this.props.saveReview(formProps.postId, formProps.review, formProps.ranking, formProps.reviewId);
  };

  render() {
    const { classes, handleSubmit } = this.props;
    return (
      <Box color="white" bgcolor="#f5f5f5" p={1}>
        <form className={classes.form} onSubmit={handleSubmit(this.onSubmit)}>
          <CssBaseline />
          <Typography component="h1" variant="h4" gutterBottom color="primary">
            Add your review
          </Typography>
          <Field name="ranking" id="ranking" component={RankingField} />
          <Field
            name="review"
            id="review"
            label="Review"
            component={MaterializeMultilineTextField}
            rowsMax={4}
            rows={4}
            placeholder="Add your review"
          />
          <Button
            variant="contained"
            className={classes.button}
            type="submit"
          >
            Save
          </Button>
        </form>
      </Box>
    );
  }
}

function mapStateToPros(state, props) {
  const { postId, review, ranking, reviewId } = props;
  return {
    initialValues: {
      postId,
      review,
      ranking,
      reviewId
    },
    enableReinitialize: true
  };
}

ReviewForm = compose(
  connect(
    mapStateToPros,
    {}
  ),
  reduxForm({ form: "review" })
)(ReviewForm);
export default withStyles(styles)(ReviewForm);
