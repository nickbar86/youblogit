import * as blogActions from "./../actions/blogActionTypes";
import { initState } from "./initStates/blog";
import { convertFromRaw, EditorState } from "draft-js";
import { showToast } from "../utils/utilities";
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled
} from "../utils/paginationUtils";
export default function info(
  state = initState,
  { type, payload, params, message, headers }
) {
  switch (type) {
    case blogActions.FETCH_POSTS:
      const links = parseHeaderForLinks(headers.link);
      return {
        ...state,
        links: { last: links.last },
        posts: loadMoreDataWhenScrolled(state.posts, payload, links)
      };
    case blogActions.FETCH_POST:
      return {
        ...state,
        post: {
          ...payload,
          content: EditorState.createWithContent(
            convertFromRaw(JSON.parse(payload.content))
          )
        }
      };
    case blogActions.INIT_CONTENT_STATE:
      return {
        ...state,
        post: {
          title: "",
          summary: "",
          content: payload,
          datePosted: new Date()
        }
      };
    case blogActions.UPDATE_CONTENT_STATE:
      return {
        ...state,
        post: {
          ...state.post,
          content: payload
        }
      };
    case blogActions.UPDATE_TITLE_STATE:
      return {
        ...state,
        post: {
          ...state.post,
          title: payload
        }
      };
    case blogActions.UPDATE_SUMMARY_STATE:
      return {
        ...state,
        post: {
          ...state.post,
          summary: payload
        }
      };
    case blogActions.SUBMIT_POST:
      showToast("Post Submitted.");
      return {
        ...state
      };
    case blogActions.SUBMIT_REVIEW:
      showToast("Review Submitted. It will be published soon!");
      return {
        ...state
      };
    default:
      return { ...state };
  }
}
