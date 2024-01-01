import { fetchData } from './apiCall.js';
import UnauthorizedError from '../errors/Unauthorized.js';

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
    } else {
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