import { signup } from '../api/auth.js';
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
    const name = document.getElementById('name').value.trim();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('pass').value.trim();
    const repass = document.getElementById('repass').value;
    if (name === "" || username === "" || password === "") {
        errorTxt.textContent = "enter all information!"
        errorTxt.style.display = "block";
        return
    }
    if (password != repass) {
        errorTxt.textContent = "passwords do not match"
        errorTxt.style.display = "block";
        return
    }
    var data = {
        name: name,
        username: username,
        password: password
    };
    try {
        await signup(data)
        window.location.href = 'index.html';
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