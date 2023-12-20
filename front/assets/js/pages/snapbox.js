import { addPost } from '../api/post.js';
import UnauthorizedError from '../errors/Unauthorized.js';

export async function addSnap(button) {
    var textAreaContainer = button.parentElement.parentElement.previousElementSibling
    var content = textAreaContainer.querySelector("#snapText").value
    if (content === '') return
    button.disabled = true
    var data = {
        content: content
    };
    try {
        await addPost(data)
        window.location.href = 'home.html';
    } catch (error) {
        if (error instanceof UnauthorizedError) {
            window.location.href = 'index.html';
        }
        else {
            console.log("error: ", error)
            window.location.href = 'error.html';
        }
    }
}