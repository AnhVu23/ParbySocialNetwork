/*  ***************************************************
                Fetch post from other user
***************************************************  */


const container = document.getElementById("container"); //The whole container with grey background that contains all posts

const currentUserInfo = (() => {
    if (localStorage.getItem("token")) {
        let request = new XMLHttpRequest();
        request.open("GET", getCurrentUserInfoUrl);
        request.setRequestHeader("authorization", localStorage.getItem("token"));
        request.send();
    
        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                return request.response;
            }
        }
    }
})();

const fetchPostOfOtherUsersFromServer = () => {

    let url = fixedUrl + "images/get/all";

    fetchData(url, "GET", (data) => {
        if (typeof data !== "undefined" && Object.keys(data).length > 0) {
            container.innerHTML = '';
            Object.keys(data).forEach(post => displayFetchedPost(post));
            mapToggleBtn();
        }
    });
}

const displayFetchedPost = (post) => {

    console.log("Displaying fetched img");

    const likes = fetchLikesOfImg(post["image"]["imageId"]);

    let commentsHtml = parseCommentsOfImg(post["image"]["imageId"]);

    const postHtml =
                    '<div class="post">\
                        <div class="post-header">\
                            <div>\
                                <img src="resources/img/woman-ava-1.jpeg" alt="avatar" class="post-avatar">\
                            </div>\
                            <div class="post-user-info">\
                                <a href="profilePage.html"><strong>' + post["uploader"]["username"] + '</strong></a>\
                                <div>Helsinki</div>\
                            </div>\
                            <div class="post-option-section">\
                                <button class="post-option-btn"><i class="fa fa-ellipsis-h post-option-btn-icon" aria-hidden="true"></i></button>\
                                <div class="dropdown hidden">\
                                    <button class="post-delete-btn"><i class="fa fa-trash-o" aria-hidden="true"></i> Delete post</button><br>\
                                </div>\
                            </div>\
                        </div>\
                        <div class="post-img-content">\
                            <img src="' + post["image"]["imgData"] + '" alt="image-content">\
                        </div>\
                        <div class="interactions">\
                            <button class="bookmark-btn"><i class="fa fa-bookmark-o" aria-hidden="true"></i></button>\
                            <button class="comment-btn"><i class="fa fa-commenting-o" aria-hidden="true"></i></button>\
                            <button class="like-btn">\
                                <span>' + Object.keys(likes).length + '</span><i class="fa fa-heart-o" aria-hidden="true"></i>\
                            </button>\
                        </div>\
                        <div class="time">\
                            <div>7 hours ago</div>\
                        </div>\
                        <div class="caption">\
                            <div>' + post["image"]["caption"] + '</div>\
                        </div>\
                        <div class="comment-section hidden">\
                            ' + commentsHtml +
                        '</div>\
                        <div class="comment-input-section hidden">\
                            <textarea name="comment" class="comment-input" cols="30" rows="10" placeholder="Add a comment..."></textarea>\
                        </div>\
                    </div>';

    container.innerHTML = postHtml + container.innerHTML;
    adjustLikeBtnIfUserHasLikedImg(post);
    mapFunctionalBtn(post);
}

const fetchLikesOfImg = (imgId) => {

    const url = fixedUrl + "likes/images/" + imgId;

    const likes = fetchData(url, "GET", (data) => {
        return data
    });

    return likes;
};

const adjustLikeBtnIfUserHasLikedImg = (post) => {
    if (localStorage.getItem["token"]) {
        const getLikesFromImgUrl = fixedUrl + "likes/images/" + post["imageId"];

        const likeBtnCollection = document.getElementsByClassName("like-btn");
        const likeBtn = likeBtnCollection[0];
        
        const likesOfImg = fetchData(getLikesFromImgUrl, "GET", (data) => {    //Get likes of img
            return data;
        });

        console.log(likesOfImg);
        
        for (var likeOfImg of likesOfImg) {
            if (likeOfImg["uploader"]["userId"] === currentUserInfo["userid"]) {       //Check if the user has liked the img
                likeBtn.classList.add("pressed");
                break;
            }
        }
    }
};

const fetchCommentsOfImg = (imgId) => {
    const url = fixedUrl + "comments/images/" + imgId;

    let request = new XMLHttpRequest();
    request.open("GET", url);
    request.send();

    request.onreadystatechange = () => {
        if (request.readyState === 4) {
            return request.response;
        }
    };
};

const parseCommentsOfImg = (imgId) => {
    const comments = fetchCommentsOfImg(imgId);

    console.log(comments);

    let commentsHtml = '';

    if (typeOf(comments) !== 'undefined') { // Check if there is/are comment(s)
        comments.forEach(comment => {
            const getUsernameFromUserIdUrl = fixedUrl + "users/getUser/" + comment["userId"];
            let commentatorUsername;
    
            fetchData(getUsernameFromUserIdUrl, "GET", (data) => {
                commentatorUsername = data.username;
            });
    
            commentsHtml += '<div class="comment"><strong>' + commentatorUsername + '</strong><span> ' + comment["content"] + '</span></div>'
        });
    
        return commentsHtml;
    }
};


/*  ***************************************************
                    Like Img
***************************************************  */


let mapLikeBtn = (post) => {
    const likeBtnCollection = document.getElementsByClassName("like-btn");
    const likeBtn = likeBtnCollection[0];

    if (!localStorage.getItem("token")) {
        likeBtn.disabled = true;
    }

    likeBtn.addEventListener("click", (event) => {
        event.stopPropagation();

        const commitLikeUrl = fixedUrl + "likes/images/" + post["imageUrl"];

        fetchData(commitLikeUrl, "POST", (data) => { //Server will autocheck if the user has liked the img or not, then decide to like or unlike
            likeBtn.classList.toggle("pressed");

            let realtimeLikes = fetchLikesOfImg(post["imageId"]);

            likeBtn.getElementsByTagName("span")[0].innerHTML = Object.keys(realtimeLikes).length;
        });
    });
};


/*  ***************************************************
                    Post comment
***************************************************  */


const mapPostCommentWhenPressEnter = (post) => {
    const commentTextAreaCollection = document.getElementsByClassName("comment-input-section");
    const commentTextArea = commentTextAreaCollection[0];

    const commentSectionCollection = document.getElementsByClassName("comment-section");
    const commentSection = commentSectionCollection[0];

    commentTextArea.addEventListener("keydown", (event) => {
        event.stopPropagation();

        if(event.which === 13 && commentTextArea.value.trim().length > 0) {

            const postCommentUrl = fixedUrl + "comments/images/" + post["imageId"];

            fetchData(postCommentUrl, "POST", (data) => { // Post comment to database

                commentSection.innerHTML = '';

                let realtimeCommentsHtml = parseCommentsOfImg(data["imageId"]); // fetch all comments of the image from database

                commentSection.innerHTML = realtimeCommentsHtml;
                commentTextArea.value = "";
                commentTextArea.blur();
            });
        }
    });
};


/*  ***************************************************
                    Delete post
***************************************************  */


let mapDeletePostBtn = (post) => {

    const deleteImgUrl = fixedUrl + "images/" + post["imageId"];

    let postNode = document.getElementsByClassName("post")[0];
    let deleteBtn = document.getElementsByClassName("post-delete-btn")[0];

    deleteBtn.addEventListener("click", (event) => {

        event.stopPropagation();

        fetchData(deleteImgUrl, "DELETE", (data) => {
            if (data["httpError"] === 200) {
                postNode.parentNode.removeChild(postNode);
            }
        });
    });
};


/*  ***************************************************
                    Map functional Btn
***************************************************  */


const mapFunctionalBtn = (post) => {
    mapLikeBtn(post);
    mapPostCommentWhenPressEnter(post);
    mapDeletePostBtn(post);
}


/*  ***************************************************
                Display modal post form
***************************************************  */


const modal = document.getElementById("modal-container");
const inputCaption = document.getElementById("input-caption");

const inputImg = document.getElementById("uploadImg");

const albumSelector = document.getElementById("album");

const closeBtn = document.getElementsByClassName("close")[0];

let previewImg = document.getElementById("modal-input").getElementsByTagName("img")[0];

let uploadImg;

if (!localStorage.getItem("token")) {
    inputImg.disabled = true;
}

inputImg.addEventListener("change", (event) => {

    uploadImg = event.target.files[0];

    let reader = new FileReader();

    // Closure to capture the file information.
    reader.onload = (event) => {
        previewImg.src = event.target.result;
    };

    // Read in the image file as a data URL.
    reader.readAsDataURL(uploadImg);

    modal.style.display = "block";
});

function closeModal() {
    modal.style.display = "none";
    inputCaption.value = "";
    inputImg.value = "";
    previewImg.src = "";
}

closeBtn.onclick = function() {
    closeModal();
};

window.onclick = function(event) {
    if (event.target === modal) {
        closeModal();
    }
};


/*  ***************************************************
                    Post image btn
***************************************************  */


const postBtn = document.getElementById("post-btn");

const uploadImgToServer = () => {

    let formData = new FormData();
    formData.append('file', uploadImg, uploadImg.name);
    formData.append('caption', inputCaption.value);
    formData.append('album', albumSelector.value);

    const uploadImgUrl = fixedUrl + "images/upload";

    let request = new XMLHttpRequest();
    request.open("POST", uploadImgUrl);
    request.setRequestHeader("authorization", localStorage.getItem("token"));
    request.send(formData);

    request.onreadystatechange = () => {
        if (request.readyState === 4) {
            return request.response;
        }
    }
};

postBtn.addEventListener("click", () => {

    const post = uploadImgToServer();

    const postHtml =
                    '<div class="post">\
                        <div class="post-header">\
                            <div>\
                                <img src="resources/img/woman-ava-1.jpeg" alt="avatar" class="post-avatar">\
                            </div>\
                            <div class="post-user-info">\
                                <a href="profilePage.html"><strong>' + currentUserInfo["username"] + '</strong></a>\
                                <div>Helsinki</div>\
                            </div>\
                            <div class="post-option-section">\
                                <button class="post-option-btn"><i class="fa fa-ellipsis-h post-option-btn-icon" aria-hidden="true"></i></button>\
                                <div class="dropdown hidden">\
                                    <button class="post-delete-btn"><i class="fa fa-trash-o" aria-hidden="true"></i> Delete post</button><br>\
                                </div>\
                            </div>\
                        </div>\
                        <div class="post-img-content">\
                            <img src="' + previewImg.src + '" alt="image-content">\
                        </div>\
                        <div class="interactions">\
                            <button class="bookmark-btn"><i class="fa fa-bookmark-o" aria-hidden="true"></i></button>\
                            <button class="comment-btn"><i class="fa fa-commenting-o" aria-hidden="true"></i></button>\
                            <button class="like-btn">\
                                <span>0</span><i class="fa fa-heart-o" aria-hidden="true"></i>\
                            </button>\
                        </div>\
                        <div class="time">\
                            <div>7 hours ago</div>\
                        </div>\
                        <div class="caption">\
                            <div>' + inputCaption.value + '</div>\
                        </div>\
                        <div class="comment-section hidden">\
                        </div>\
                        <div class="comment-input-section hidden">\
                            <textarea name="comment" class="comment-input" cols="30" rows="10" placeholder="Add a comment..."></textarea>\
                        </div>\
                    </div>'

    container.innerHTML = postHtml + container.innerHTML;

    closeModal();
    mapFunctionalBtn(post);
    mapToggleBtn();
});


/*  ***************************************************
                Show comment
***************************************************  */


let mapCommentBtn = () => {
    let commentBtnArray = [...document.getElementsByClassName("comment-btn")];

    commentBtnArray.forEach(element => {
        element.addEventListener("click", () => {
            let post = element.parentNode.parentNode;
            let commentSection = post.getElementsByClassName("comment-section")[0];
            let commentInputSection = post.getElementsByClassName("comment-input-section")[0];

            commentSection.classList.toggle("hidden");

            if (localStorage.getItem("token")) {
                commentInputSection.classList.toggle("hidden");
            }
        });
    });
};


/*  ***************************************************
            Display delete post dropdown
***************************************************  */


let mapPostOptionBtn = () => {
    let optionBtnArray = [...document.getElementsByClassName("post-option-btn")];
    let dropdownArray = [...document.getElementsByClassName("dropdown")];

    optionBtnArray.forEach(element => {
        let dropdown = dropdownArray[optionBtnArray.indexOf(element)];

        let notHoverOnOptionBtn = true;
        let notHoverOnDropdown = true;

        element.addEventListener("click", () => {
            dropdown.classList.toggle("hidden");
        });

        //Check if the mouse is hovering on the dropdown or not

        dropdown.addEventListener("mouseover", () => {
            notHoverOnDropdown = false;
        });
        dropdown.addEventListener("mouseleave", () => {
            notHoverOnDropdown = true;
        });

        //Check if the mouse is hovering on the option btn or not

        element.addEventListener("mouseover", () => {
            notHoverOnOptionBtn = false;
        });
        element.addEventListener("mouseleave", () => {
            notHoverOnOptionBtn = true;
        });

        //If the mouse is clicked
        document.addEventListener("mouseup", () => {
            if(notHoverOnDropdown && notHoverOnOptionBtn) { //If the mouse is not hovered on the dropdown
                dropdown.classList.add("hidden");
            }
        });
        document.addEventListener("keyup", (event) => {
            if(event.which == 27) { //If ESC btn is pressed
                dropdown.classList.add("hidden");
            }
        });
    });
};


/*  ***************************************************
                    Remap all buttons
***************************************************  */


const mapToggleBtn = () => {
    mapCommentBtn();
    mapPostOptionBtn();
};

window.onload = () => {
    console.log("Windows has beeen loaded");
    fetchPostOfOtherUsersFromServer();
    mapToggleBtn();
};
