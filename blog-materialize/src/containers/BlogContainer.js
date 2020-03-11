import React, { Component } from "react";
import { connect } from "react-redux";
import Container from '@material-ui/core/Container';
import * as blogActions from "../actions/blogActions";
import Post from "./../components/blog/Post";
import PostForm from "./../components/blog/PostForm";
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
          {this.props.edit ? (
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
            <Post post={this.props.post} />
          )}
        </Container>
      </ErrorBoundary>
    );
  }
}
function mapStateToPros(
  { blog: { post } },
  { match: { params }, history, location: { search } }
) {
  const edit = qs.parse(search).edit;
  return {
    edit,
    post,
    id: params.id
  };
}
export default connect(mapStateToPros, {
  ...blogActions
})(BlogContainer);
