import React, { Component } from "react";
import { connect } from "react-redux";
import Container from "@material-ui/core/Container";
import { getAllPosts, reset } from "../actions/blogActions";
import Blog from "./../components/blog/listing";
import ErrorBoundary from "./../components/ErrorBoundary";
import { getSortState } from "../utils/paginationUtils";
class BlogListContainer extends Component {
  state = {
    ...getSortState(this.props.location, 20)
  };

  componentDidMount() {
    this.reset();
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 0 }, () => this.getAllPosts());
  };

  handleLoadMore = page => {
    this.setState({ activePage: this.state.activePage + 1 }, () =>
      this.getAllPosts()
    );
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === "asc" ? "desc" : "asc",
        sort: prop
      },
      () => this.reset()
    );
  };

  getAllPosts = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getAllPosts(activePage, itemsPerPage, `${sort},${order}`);
  };

  onSelect = id => {
    debugger;
    this.props.history.push(`/blog/${id}`);
  };

  render() {
    return (
      <ErrorBoundary>
        <Container style={{ marginTop: "30px", marginBottom: "100px" }} maxWidth="lg">
          <Blog
            posts={this.props.posts}
            handleLoadMore={this.handleLoadMore}
            activePage={this.state.activePage}
            links={this.props.links}
            onSelect={this.onSelect}
          />
        </Container>
      </ErrorBoundary>
    );
  }
}
function mapStateToPros({ blog: { posts, links } }, { location }) {
  return {
    posts,
    location,
    links
  };
}
export default connect(mapStateToPros, { getAllPosts, reset })(
  BlogListContainer
);
