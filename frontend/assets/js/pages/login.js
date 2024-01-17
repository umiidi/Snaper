import { signin } from '../api/auth.js';
import IllegalArgumentError from '../errors/IllegalArgument.js';

const errorTxt = document.getElementById("errortxt")

document.addEventListener('DOMContentLoaded', async function () {
    if (localStorage.getItem("accessToken")) {
        window.location.href = 'home.html';
    }
});

document.getElementById("submit").addEventListener("click", async (e) => {
    e.preventDefault()
    errorTxt.style.display = "none";
    const username = document.getElementById('username').value;
    const password = document.getElementById('pass').value;
    var data = {
        username: username,
        password: password
    };
    try {
        await signin(data)
        window.location.href = 'home.html';
    } catch (error) {
        if (error instanceof IllegalArgumentError) {
            errorTxt.textContent = error.message
            errorTxt.style.display = "block";
        } else {
            console.log("error: ", error)
            window.location.href = 'error.html';
        }
    }
})