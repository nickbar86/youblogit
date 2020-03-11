export const initState = {
  posts: [],
  links: {
    last: 0
  },
  post: {
    title: "",
    summary: "",
    constent: null
  }
};

export const resetState = {
  posts: [],
  pagination: {
    itemsPerPage: 20,
    sort: "datePosted",
    order: "desc",
    activePage: 0
  },
  post: {}
};
