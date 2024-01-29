import { fetchData } from './apiCall.js';
import AccessDeniedError from '../errors/AccessDenied.js';
import UnauthorizedError from '../errors/Unauthorized.js';
import NotFoundError from '../errors/NotFound.js';

export async function getPosts() {
  const response = await fetchData("/post", "GET", null);
  if (!response.ok) {
    if (response.status === 401) {
      throw new UnauthorizedError("un authorized")
    } else {
      throw new Error('Xəta baş verdi!');
    }
  }
  const data = await response.json();
  return data.data;
}

export async function getPost(id) {
  const response = await fetchData(`/post/${id}`, "GET", null);
  if (!response.ok) {
    if (response.status === 401) {
      throw new UnauthorizedError("un authorized")
    }
    else if (response.status === 409) {
      throw new NotFoundError(`Post not found with id: ${id}`,)
    }
    else {
      throw new Error('Xəta baş verdi!');
    }
  }
  const data = await response.json();
  return data.data;
}

export async function addPost(data) {
  const response = await fetchData("/post", "POST", data);
  if (!response.ok) {
    if (response.status === 401) {
      throw new UnauthorizedError("un authorized")
    } else {
      throw new Error('Xəta baş verdi!');
    }
  }
}

export async function updatePost(id, data) {
  const response = await fetchData(`/post/${id}`, "PUT", data);
  if (!response.ok) {
    if (response.status === 401) {
      throw new UnauthorizedError("un authorized")
    }
    else if (response.status === 403) {
      throw new AccessDeniedError("can't delete a post");
    }
    else {
      console.log(response)
      throw new Error('Xəta baş verdi!');
    }
  }
}

export async function removePost(id) {
  const response = await fetchData(`/post/${id}`, "DELETE", null);
  if (!response.ok) {
    if (response.status === 401) {
      throw new UnauthorizedError("un authorized")
    }
    else if (response.status === 403) {
      throw new AccessDeniedError("can't delete a post");
    }
    else {
      throw new Error('Xəta baş verdi!');
    }
  }
}