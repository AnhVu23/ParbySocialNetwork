const signInBtn = document.getElementsByClassName("login")[0].getElementsByTagName("input")[0];

signInBtn.addEventListener("click", (event) => {

    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const signInUrl = fixedUrl + 'users/signin?email=' + email + '&&password=' + password;

    fetchData(signInUrl, "POST", (data) => {
        if (data.authorization) {
            localStorage.setItem("token", data.authorization);  //save token to use
            localStorage.setItem("didLogIn", true); //save login state
    
            //TODO: Navigate to main page
            window.location.href = "home.html";
        } else {
            const alert = document.getElementById("login").getElementsByClassName("red")[0];
            alert.classList.remove("hidden");
        }
    });
});