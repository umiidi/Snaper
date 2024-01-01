import { fetchData } from './apiCall.js';
import UnauthorizedError from '../errors/Unauthorized.js';

export async function getComments(postId) {
    const response = await fetchData(`/comment/${postId}`, "GET", null);
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

export async function addComment(postId, data) {
    const response = await fetchData(`/comment/${postId}`, "POST", data);
    if (!response.ok) {
        if (response.status === 401) {
            throw new UnauthorizedError("un authorized")
        } else {
            throw new Error('Xəta baş verdi!');
        }
    }
}