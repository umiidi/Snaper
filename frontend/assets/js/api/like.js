import { fetchData } from './apiCall.js';
import UnauthorizedError from '../errors/Unauthorized.js';

export async function like(id) {
    const response = await fetchData(`/like/${id}`, "POST", null);
    if (!response.ok) {
        if (response.status === 401) {
            throw new UnauthorizedError("un authorized")
        } else {
            throw new Error('Xəta baş verdi!');
        }
    }
}

export async function unlike(id) {
    const response = await fetchData(`/like/${id}`, "DELETE", null);
    if (!response.ok) {
        if (response.status === 401) {
            throw new UnauthorizedError("un authorized")
        } else {
            throw new Error('Xəta baş verdi!');
        }
    }
}