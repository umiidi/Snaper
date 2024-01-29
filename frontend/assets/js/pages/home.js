import { getPosts } from '../api/post.js';
import { like, unlike } from '../api/like.js';
import { isAccessTokenExpired } from '../scripts/token.js';
import UnauthorizedError from '../errors/Unauthorized.js';

function renderPost(data) {
    var postContainer = document.getElementById("posts");
    let postHTML = ''
    for (let i = 0; i < data.length; i++) {
        postHTML +=
            `<div class="post-box border-bottom">
                <div class="author-image" style="display: flex;">
                    <img src="assets/images/snaper-logo.jpg" alt="Profile Image">
                </div>
                <div class="post-main">
                    <div class="post-header">

                        <div class="post-author-info">
                            <h4>${data[i].name !== null ? data[i].name : ""}</h4>
                            <p>@${data[i].username}</p>
                        </div>

                        <div class="post-head-right">
                            <div class="time-ago">${timeago(data[i].createdAt)}</div>
                            <div class="options-dropdown" id="optionsDropdown">
                                <i class="fa-solid fa-ellipsis" id="optionsIcon"></i>
                                <div class="options-dropdown-content">
                                    <button class="options-button" onclick="editOption()">Edit</button>
                                    <button class="options-button" onclick="deleteOption()">Delete</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <span class="post-content">${data[i].content}</span>
                    <div class="post-footer">
                        <div class="actions">
                            <div>
                                <i id="like${data[i].id}" class="${data[i].liked ? "fa-solid liked" : "fa-regular"}  fa-heart" onclick="like(${data[i].id})"></i>
                            </div>
                            <div>
                                <i class="fa-regular fa-comment"></i>
                            </div>
                            <div>
                                <i class="fa-solid fa-chart-simple"></i>
                                <span>${data[i].views}</span>
                            </div>
                            <div>
                                <a href="/post.html?id=${data[i].id}" style="color: black;">
                                    <i class="fa-solid fa-eye"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div> `
    }
    postContainer.innerHTML = postHTML;
}

document.addEventListener('DOMContentLoaded', async function () {
    if (isAccessTokenExpired()) {
        localStorage.clear();
        window.location.href = 'index.html';
    }
    try {
        const snaps = await getPosts();
        document.getElementById("spinner").style.display = "none"
        renderPost(snaps)
    } catch (error) {
        if (error instanceof UnauthorizedError) {
            window.location.href = 'index.html';
        }
        else {
            window.location.href = 'error.html';
        }
    }
});

export async function likePost(postId) {
    try {
        await like(postId)
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

export async function unlikePost(postId) {
    try {
        await unlike(postId)
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

function timeago(timeAtString) {
    var pastDateTime = new Date(timeAtString);
    var currentDateTime = new Date();
    var timeDifference = currentDateTime - pastDateTime;
    var seconds = Math.floor(timeDifference / 1000);
    var minutes = Math.floor(seconds / 60);
    var hours = Math.floor(minutes / 60);
    var days = Math.floor(hours / 24);
    var months = Math.floor(days / 30);
    var years = Math.floor(days / 365);

    if (years > 0) {
        return years + " year" + (years === 1 ? "" : "s") + " ago";
    } else if (months > 0) {
        return months + " month" + (months === 1 ? "" : "s") + " ago";
    } else if (days > 0) {
        return days + " day" + (days === 1 ? "" : "s") + " ago";
    } else if (hours > 0) {
        return hours + " hour" + (hours === 1 ? "" : "s") + " ago";
    } else if (minutes > 0) {
        return minutes + " minute" + (minutes === 1 ? "" : "s") + " ago";
    } else {
        return seconds + " second" + (seconds === 1 ? "" : "s") + " ago";
    }
}