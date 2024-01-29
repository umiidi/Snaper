import { getPost, removePost, updatePost } from '../api/post.js';
import { getComments, addComment, removeComment } from '../api/comment.js';
import { like, unlike } from '../api/like.js';
import { isAccessTokenExpired } from '../scripts/token.js';
import AccessDeniedError from '../errors/AccessDenied.js';
import UnauthorizedError from '../errors/Unauthorized.js';
import NotFoundError from '../errors/NotFound.js';

const spinnerPost = document.getElementById("spinnerPost")
const spinnerComments = document.getElementById("spinnerComments")

function renderPost(data) {
    var postContainer = document.getElementById("postbox");
    let postHTML =
        `<div class="post-header">
            <div class="author-image">
                <img src="assets/images/snaper-logo.jpg" alt="Profile Image">
            </div>
            <div class="post-author-info">
                <h4>${data.name} ${data.surname !== null ? data.surname : ""}</h4>
                <p>@${data.username}</p>
            </div>
            <div class="options-dropdown"id="postOptionsDropdown">
                <i class="fa-solid fa-ellipsis" onclick="postOption()"></i>
                <div class="options-dropdown-content">
                    <button class="options-button">Test</button>
                    ${data.access ? `<button class="options-button" data-toggle="modal" data-target="#UpdatePostManualModal">Edit</button> ` : ""}
                    ${data.access ? `<button class="options-button" onclick="deletePostClick(${data.id})">Delete</button> ` : ""}
                </div>
            </div>
        </div>

        <div class="post-content">${data.content}</div>
        <div class="post-footer">
            <div class="post-date">
                <span>${getTime(data.createdAt)} <b>·</b> ${getDate(data.createdAt)}</span>
            </div>
            <hr class="footer-hr">
            <div class="actions">
                <div>
                <i id="like" class="${data.liked ? "fa-solid liked" : "fa-regular"}  fa-heart" onclick="like(${data.id})"></i>
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
        </div> 
        
        <div class="modal" id="UpdatePostManualModal" tabindex="-1" role="dialog" aria-labelledby="UpdatePostManualModalLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-body">
                        <div class="textarea-container">
                            <textarea id="snapText" class="border-bottom" oninput=updateCount(this)
                                placeholder="Whats happening?"> ${data.content}</textarea>
                        </div>
                        <div class="footer">
                            <div class="icons">
                                <i class="fa-regular fa-face-smile"></i>
                                <i class="fa-regular fa-image"></i>
                                <i class="fa-solid fa-calendar-days"></i>
                            </div>
                            <div>
                                <span id="charCount" class="count"></span>
                            </div>
                            <div>
                                <button id="snapButton" class="snap-button" onclick=editPostClick(this)>Update</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>`

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
                        <div class="options-dropdown" id="commentOptionsDropdownBy${data[i].id}">
                            <i class="fa-solid fa-ellipsis" onclick="commentOption(${data[i].id})"></i>
                            <div class="options-dropdown-content">
                                <button class="comment-options-button">Test</button>
                                ${data[i].access ? `<button class="comment-options-button" 
                                    onclick="deleteCommentClick(${data[i].id})"> Delete </button>` : ""}
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
        await initPost(postId)
        initComments(postId)
    } catch (error) {
        console.log("error: ", error)
        if (error instanceof UnauthorizedError) {
            window.location.href = 'index.html';
        }
        else if (error instanceof NotFoundError) {
            // todo: custom not found page ???
            window.location.href = 'error.html';
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
        spinnerComments.style.display = "flex"
        document.getElementById("comments").innerHTML = spinnerComments.outerHTML
        await addComment(postId, data)
        initComments(postId)
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

export async function editPost(button) {
    var textAreaContainer = button.parentElement.parentElement.previousElementSibling
    var content = textAreaContainer.querySelector("#snapText").value
    if (content === '') return
    button.disabled = true
    var id = getPostId()
    var data = {
        content: content
    };
    try {
        spinnerPost.style.display = "flex"
        document.getElementById("postbox").innerHTML = spinnerPost.outerHTML
        await updatePost(id, data)
        initPost(id)
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

export async function deletePost(postId) {
    try {
        await removePost(postId)
        window.location.href = 'home.html';
    } catch (error) {
        if (error instanceof UnauthorizedError) {
            window.location.href = 'index.html';
        }
        else if (error instanceof AccessDeniedError) {
            alert(error.message)
        }
        else {
            console.log("error: ", error)
            window.location.href = 'error.html';
        }
    }
}

export async function deleteComment(commentId) {
    try {
        spinnerComments.style.display = "flex"
        document.getElementById("comments").innerHTML = spinnerComments.outerHTML
        await removeComment(commentId)
        initComments(getPostId())
    } catch (error) {
        if (error instanceof UnauthorizedError) {
            window.location.href = 'index.html';
        }
        else if (error instanceof AccessDeniedError) {
            alert(error.message)
        }
        else {
            console.log("error: ", error)
            window.location.href = 'error.html';
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

function getTime(dateTimeString) {
    const dateTime = new Date(dateTimeString);
    return dateTime.toLocaleTimeString('en-US', { hour: 'numeric', minute: 'numeric', hour12: false });
}

function getDate(dateTimeString) {
    const dateTime = new Date(dateTimeString);

    const day = dateTime.getDate();
    const monthIndex = dateTime.getMonth();
    const year = dateTime.getFullYear();

    const monthNames = [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];

    return `${day} ${monthNames[monthIndex]} ${year}`
}