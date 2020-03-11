import { isEqual } from 'lodash';

export const getUrlParameter = (name, search) => {
  name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
  const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
  const results = regex.exec(search);
  return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
};

export const getSortState = (location, itemsPerPage) => {
  const pageParam = getUrlParameter('page', location.search);
  const sortParam = getUrlParameter('sort', location.search);
  let sort = 'id';
  let order = 'asc';
  let activePage = 1;
  if (pageParam !== '' && !isNaN(parseInt(pageParam, 10))) {
    activePage = parseInt(pageParam, 10);
  }
  if (sortParam !== '') {
    sort = sortParam.split(',')[0];
    order = sortParam.split(',')[1];
  }
  return { itemsPerPage, sort, order, activePage };
};

export const getPaginationItemsNumber = (totalItems, itemsPerPage) => {
  const division = Math.floor(totalItems / itemsPerPage);
  const modulo = totalItems % itemsPerPage;
  return division + (modulo !== 0 ? 1 : 0);
};

export const loadMoreDataWhenScrolled = (currentData, incomingData, links) => {
  let data = null;
  if (links.first === links.last && incomingData.length) {
    data = incomingData;
  } else {
    if (currentData.length === incomingData.length) {
      if (links.prev === undefined) {
        data = incomingData;
      } else if (!isEqual(currentData, incomingData)) {
        data = [...currentData, ...incomingData];
      }
    } else {
      data = [...currentData, ...incomingData];
    }
  }
  return data;
};


export const parseHeaderForLinks = (header) => {
  if (header.length === 0) {
    throw new Error('input must not be of zero length');
  }

  // Split parts by comma
  const parts: string[] = header.split(',');
  const links: any = {};

  // Parse each part into a named link
  parts.forEach(p => {
    const section: string[] = p.split(';');

    if (section.length !== 2) {
      throw new Error('section could not be split on ";"');
    }

    const url: string = section[0].replace(/<(.*)>/, '$1').trim();
    const queryString: any = {};

    url.replace(new RegExp('([^?=&]+)(=([^&]*))?', 'g'), ($0, $1, $2, $3) => (queryString[$1] = $3));

    let page: any = queryString.page;

    if (typeof page === 'string') {
      page = parseInt(page, 10);
    }

    const name: string = section[1].replace(/rel="(.*)"/, '$1').trim();
    links[name] = page;
  });
  return links;
};
