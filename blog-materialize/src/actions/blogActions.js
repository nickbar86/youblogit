import {
  commonApiGetObject,
  commonApiPostObject,
  commonApiPutObject
} from "../utils/utilities";
import { convertToRaw } from "draft-js";
import * as actions from "./blogActionTypes";
const url = "blog-post";

export function getAllPosts(userPosts, page, size, sort) {
  const requestUrl = `${url}${
    sort ? `?userPosts=${userPosts}&page=${page}&size=${size}&sort=${sort}` : ""
  }`;
  return commonApiGetObject(`${requestUrl}`, actions.FETCH_POSTS, true);
}

export function reset() {
  return {
    type: actions.RESET
  };
}

export function getPostById(id) {
  return commonApiGetObject(`${url}/posts/${id}`, actions.FETCH_POST);
}

export function savePost(post) {
  const postRaw = {
    ...post,
    content: JSON.stringify(convertToRaw(post.content.getCurrentContent()))
  };
  if (postRaw.id) {
    return commonApiPostObject(
      `${url}/posts`,
      postRaw,
      actions.SUBMIT_POST,
      true
    );
  } else {
    return commonApiPutObject(
      `${url}/posts`,
      postRaw,
      actions.SUBMIT_POST,
      true
    );
  }
}

export function saveReview(postId, review, ranking, reviewId) {
  const reviewJson = {
    postId,
    review,
    ranking,
    reviewId
  };
  if (postId) {
    return commonApiPostObject(
      `${url}/reviews`,
      reviewJson,
      actions.SUBMIT_REVIEW,
      true
    );
  } else {
    return commonApiPutObject(
      `${url}/reviews`,
      reviewJson,
      actions.SUBMIT_REVIEW,
      true
    );
  }
}

export function initPostContentState(editorState) {
  return {
    payload: editorState,
    type: actions.INIT_CONTENT_STATE
  };
}

export function updatePostContent(editorState) {
  return {
    payload: editorState,
    type: actions.UPDATE_CONTENT_STATE
  };
}

export function updatePostTitle(titleEvt) {
  return {
    payload: titleEvt.target.value,
    type: actions.UPDATE_TITLE_STATE
  };
}

export function updatePostSummary(summaryEvt) {
  return {
    payload: summaryEvt.target.value,
    type: actions.UPDATE_SUMMARY_STATE
  };
}
