/*  ***************************************************
                Select Elements
***************************************************  */

const addAlbumModal = document.getElementById("modal-addAlbum");
const inputAlbumName = document.getElementById("albumName");
const inputAlbum = document.getElementById("addAlbum");
const closeAlbum = document.getElementById("closeAlbumModal");
let albumCover = document.getElementById("modal-input").getElementsByTagName("img")[0];


/*  ***************************************************
                Create Album
***************************************************  */

inputAlbum.addEventListener("change", (event) => {

    console.log("hello");

    var file = event.target.files[0];

    var reader = new FileReader();

    // Closure to capture the file information.
    reader.onload = (event) => {
        albumCover.src = event.target.result;
    };

    // Read in the image file as a data URL.
    reader.readAsDataURL(file);

    addAlbumModal.style.display = "block";
});


/*  ***************************************************
                Close Album Modal
***************************************************  */

closeAlbum.onclick = function() {
    addAlbumModal.style.display = "none";
}

function closeModal() {
    addAlbumModal.style.display = "none";
    inputAlbumName.value = "";
    inputAlbum.value = "";
    albumCover.src = "";
}

window.onclick = function(event) {
    if (event.target === addAlbumModal) {
        closeModal();
    }
}
/*  ***************************************************
                    New Album Added To Screen
***************************************************  */


const createBtn = document.getElementById("create");
const displayAlbum = document.getElementById("displayAlbum");

createBtn.addEventListener("click", () => {

    const postHtml ='<div class="media">\
                      <a target="_blank" href="photoPage.html">\
                        <img src="'+ albumCover.src + '" alt="avatar">\
                      </a>\
                      <h2><span>' + inputAlbumName.value + '</span></h2>\
                    </div>'


    displayAlbum.innerHTML = postHtml + displayAlbum.innerHTML;
    closeModal();
});
