import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import CircularProgress from "@material-ui/core/CircularProgress";
import { Link as RouterLink } from "react-router-dom";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import InfiniteScroll from "react-infinite-scroller";
import Grid from "@material-ui/core/Grid";
import Hidden from "@material-ui/core/Hidden";
import MainListing from "./MainListing";

const useStyles = makeStyles(theme => ({
  media: {
    height: 140
  },
  card: {
    //display: "flex",
    marginBottom: "10px"
  },
  cardDetails: {
    flex: 1
  },
  cardMedia: {
    width: 160
  }
}));
/*
<CardActions>

</CardActions>*/
export default ({ posts, user, handleLoadMore, activePage, links, onSelect }) => {
  const classes = useStyles();
  const list =
    posts &&
    posts.slice(1, posts.length).map(post => {
      return (
        <Grid item xs={12} md={10} key={post.id}>
          <Card className={classes.card}>
            <CardActionArea onClick={() => onSelect(post.id)}>
              <div className={classes.cardDetails}>
                <CardContent>
                  <Typography component="h2" variant="h5">
                    {post.title}
                  </Typography>
                  <Typography variant="subtitle1" color="textSecondary">
                    {`${new Date(post.datePosted).toLocaleString()}`}
                  </Typography>
                  <Typography variant="subtitle1" paragraph>
                    {post.summary}
                  </Typography>
                  <Typography variant="subtitle1" color="primary">
                    Continue reading...
                  </Typography>
                </CardContent>
              </div>
              {post.image && (
                <Hidden xsDown>
                  <CardMedia
                    className={classes.cardMedia}
                    image={post.image}
                    title={post.imageTitle}
                  />
                </Hidden>
              )}
            </CardActionArea>

            {post.blogUserId===user.userid?<CardActions>
              <Button
                //  style={{ marginBottom: "10px" }}
                component={RouterLink}
                to={`/blog/${post.id}?edit=true`}
                variant="contained"
              >
                Edit
              </Button>
            </CardActions>:null}
          </Card>
        </Grid>
      );
    });
  return (
    <React.Fragment>
      {posts && posts.length > 0 && (
        <MainListing post={posts[0]} onSelect={onSelect} />
      )}

      <InfiniteScroll
        pageStart={activePage}
        initialLoad={true}
        loadMore={handleLoadMore}
        hasMore={activePage <= links.last}
        threshold={1}
        loader={<CircularProgress key="-1" />}
      >
        {list}
      </InfiniteScroll>
    </React.Fragment>
  );
};
