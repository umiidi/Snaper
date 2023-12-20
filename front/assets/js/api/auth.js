import IllegalArgumentError from '../errors/IllegalArgument.js';
import { fetchData } from './apiCall.js';

export async function signin(data) {
    await fetchData("/auth/sign-in", "POST", data)
        .then(response => {
            if (!response.ok) {
                if (response.status === 401) {
                    throw new IllegalArgumentError('username or password incorrect');
                } else {
                    throw new Error('Xəta baş verdi!');
                }
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('accessToken', data.data.accessToken);
        })
}

export async function signup(data) {
    await fetchData("/auth/sign-up", "POST", data)
        .then(response => {
            if (!response.ok) {
                if (response.status === 409) {
                    throw new IllegalArgumentError('username is already in use');
                } else {
                    throw new Error('Xəta baş verdi!');
                }
            }
        })

}