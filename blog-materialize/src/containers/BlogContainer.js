import React, { Component } from "react";
import { connect } from "react-redux";
import Container from "@material-ui/core/Container";
import * as blogActions from "../actions/blogActions";
import Post from "./../components/blog/Post";
import PostForm from "./../components/blog/PostForm";
import ReviewForm from "./../components/blog/ReviewForm";
import ErrorBoundary from "./../components/ErrorBoundary";
import * as qs from "query-string";

class BlogContainer extends Component {
  componentDidMount() {
    if (this.props.id && this.props.id !== "new") {
      this.props.getPostById(this.props.id);
    }
  }
  render() {
    return (
      <ErrorBoundary>
        <Container style={{ marginTop: "30px" }}>
          {this.props.edit && this.props.isAuthorisedToEdit ? (
            <PostForm
              post={this.props.post}
              initPostContentState={this.props.initPostContentState}
              updatePostContent={this.props.updatePostContent}
              updatePostTitle={this.props.updatePostTitle}
              updatePostSummary={this.props.updatePostSummary}
              savePost={this.props.savePost}
              isNew={this.props.id === "new"}
            />
          ) : (
            <>
              <Post post={this.props.post} />{" "}
              {!this.props.isAuthorisedToEdit && this.props.authenticated ? (
                <ReviewForm
                  saveReview={this.props.saveReview}
                  postId={this.props.post.id}
                />
              ) : null}
            </>
          )}
        </Container>
      </ErrorBoundary>
    );
  }
}
function mapStateToPros(
  { blog: { post }, info },
  { match: { params }, history, location: { search } }
) {
  const edit = qs.parse(search).edit;
  const isAuthorisedToEdit =
    info.authenticated &&
    (params.id === "new" ||
      (post && post.user && post.user.email === info.user.mail));
  return {
    edit,
    post,
    isAuthorisedToEdit: isAuthorisedToEdit,
    authenticated: info.authenticated,
    id: params.id
  };
}
export default connect(
  mapStateToPros,
  {
    ...blogActions
  }
)(BlogContainer);
