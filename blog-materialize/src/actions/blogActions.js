import { CALL_API } from "./../middlewares/api";
import {
  //commonApiDeleteObject,
  commonApiGetObject,
  //constructParameters,
  //fetchAndReturnValue,
  commonApiPostObject,
  commonApiPutObject
} from "../utils/utilities";
import { convertToRaw } from "draft-js";
export const FETCH_POSTS = "FETCH_POSTS";
export const FETCH_POST = "FETCH_POST";
export const SUBMIT_POST = "SUBMIT_POST";
export const INIT_CONTENT_STATE = "INIT_CONTENT_STATE";
export const UPDATE_CONTENT_STATE = "UPDATE_CONTENT_STATE";
export const UPDATE_TITLE_STATE = "UPDATE_TITLE_STATE";
export const UPDATE_SUMMARY_STATE = "UPDATE_SUMMARY_STATE";
export const RESET = "RESET";
const url = "blog-post";

export function getAllPosts(page, size, sort) {
  const requestUrl = `${url}/${
    sort ? `?page=${page}&size=${size}&sort=${sort}` : ""
  }`;
  return commonApiGetObject(`${requestUrl}`, FETCH_POSTS);
}

export function reset() {
  return {
    type: RESET
  };
}

export function getPostById(id) {
  return commonApiGetObject(`${url}/posts/${id}/`, FETCH_POST);
}
export function savePost(post) {
  const postRaw = {
    ...post,
    content: JSON.stringify(convertToRaw(post.content.getCurrentContent()))
  };
  return commonApiPutObject(`${url}/posts/`, postRaw, SUBMIT_POST, true);
}

export function initPostContentState(editorState) {
  return {
    payload: editorState,
    type: INIT_CONTENT_STATE
  };
}

export function updatePostContent(editorState) {
  return {
    payload: editorState,
    type: UPDATE_CONTENT_STATE
  };
}

export function updatePostTitle(titleEvt) {
  return {
    payload: titleEvt.target.value,
    type: UPDATE_TITLE_STATE
  };
}

export function updatePostSummary(summaryEvt) {
  return {
    payload: summaryEvt.target.value,
    type: UPDATE_SUMMARY_STATE
  };
}
