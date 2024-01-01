import { getPost } from '../api/post.js';
import { getComments, addComment } from '../api/comment.js';
import { like, unlike } from '../api/like.js';
import { isAccessTokenExpired } from '../scripts/token.js';
import UnauthorizedError from '../errors/Unauthorized.js';


const spinnerPost = document.getElementById("spinnerPost")
const spinnerComments = document.getElementById("spinnerComments")

// <span>12:54 PM <b>·</b> Dec 6 2023</span>
function renderPost(data) {
    var postContainer = document.getElementById("postbox");
    let postHTML =
        ` <div class="post-header">
            <div class="author-image">
                <img src="assets/images/snaper-logo.jpg" alt="Profile Image">
            </div>
            <div class="post-author-info">
                <h4>${data.name}</h4>
                <p>@${data.username}</p>
            </div>
            <div class="options-dropdown" id="optionsDropdown">
                <i class="fa-solid fa-ellipsis" id="optionsIcon"></i>
                <div class="options-dropdown-content">
                    <button class="options-button" onclick="editOption()">Edit</button>
                    <button class="options-button" onclick="deleteOption()">Delete</button>
                </div>
            </div>
            </div>
            <div class="post-content">${data.content}</div>
            <div class="post-footer">
            <div class="post-date">
                <span>${data.createdAt.split("T")[1]} <b>·</b> ${data.createdAt.split("T")[0]}</span>
            </div>
            <hr class="footer-hr">
            <div class="actions">
                <div>
                 <i id="like" class="${data.liked ? "fa-solid" : "fa-regular"}  fa-heart" onclick="like(${data.id})"></i>
                    <span>${data.numberOfLikes}</span>
                </div>
                <div>
                    <i class="fa-regular fa-comment"></i>
                    <span>${data.numberOfComments}</span>
                </div>
                <div>
                    <i class="fa-solid fa-chart-simple"></i>
                    <span>${data.views}</span>
                </div>
                <div>
                    <i class="fa-regular fa-paper-plane"></i>
                </div>
            </div>
            <hr class="footer-hr">
           </div> `

    postContainer.innerHTML = postHTML;
}

function renderComments(data) {
    var commentContainer = document.getElementById("comments");
    let commentHTML = ''
    for (let i = 0; i < data.length; i++) {
        data[i] = data[i][0]
        commentHTML +=
            ` <div class="comment">
                <div class="comment-authorimage">
                    <img src="assets/images/snaper-logo.jpg" alt="Profile Image">
                </div>
                <div class="comment-body">
                    <div class="comment-head">
                        <h5>${data[i].name} </h5>
                        <p>@${data[i].username}</p>
                        <span><b>·</b> ${timeago(data[i].commentedAt)}</span>
                        <div class="comment-options" id="commentOptionsDropdown">
                            <i class="fa-solid fa-ellipsis" id="commentOptionsIcon"></i>
                            <div class="comment-options-content">
                                <button class="comment-options-button"
                                    onclick="editOption()">Edit</button>
                                <button class="comment-options-button"
                                    onclick="deleteOption()">Delete</button>
                            </div>
                        </div>
                    </div>
                    <div class="comment-content">${data[i].content}</div>
                </div>
               </div>`
    }
    commentContainer.innerHTML = commentHTML;
}

document.addEventListener('DOMContentLoaded', async function () {
    if (isAccessTokenExpired()) {
        localStorage.clear();
        window.location.href = 'index.html';
    }
    const postId = getPostId()
    try {
        initPost(postId)
        initComments(postId)
    } catch (error) {
        if (error instanceof UnauthorizedError) {
            window.location.href = 'index.html';
        }
        else {
            window.location.href = 'error.html';
        }
    }
});

async function initPost(postId) {
    const post = await getPost(postId);
    spinnerPost.style.display = "none"
    renderPost(post)
}

async function initComments(postId) {
    const comments = await getComments(postId);
    spinnerComments.style.display = "none"
    renderComments(comments)
}

export async function addCommentToPost(button) {
    var textAreaContainer = button.parentElement.parentElement.previousElementSibling
    var content = textAreaContainer.querySelector("#commentText").value
    if (content === '') return
    button.disabled = true
    var data = {
        content: content
    };
    try {
        const postId = getPostId()
        await addComment(postId, data)
        spinnerComments.style.display = "block"
        initComments(postId)
    } catch (error) {
        if (error instanceof UnauthorizedError) {
            window.location.href = 'index.html';
        }
        else {
            console.log("error: ", error)
            // window.location.href = 'error.html';
        }
    }
}

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
            // window.location.href = 'error.html';
        }
    }
}

function getPostId() {
    const urlParams = new URLSearchParams(window.location.search);
    const postId = urlParams.get('id');
    if (postId === null || postId === "") {
        window.location.href = 'error.html';
    } else {
        return postId
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
    var months = Math.floor(days / 30); // Ortalama bir ay 30 gün olarak kabul edildi.
    var years = Math.floor(days / 365); // Ortalama bir yıl 365 gün olarak kabul edildi.
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