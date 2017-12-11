const signUpBtn = document.getElementsByClassName("signup")[0].getElementsByTagName("input")[0];

signUpBtn.addEventListener("click", (event) => {

    event.preventDefault();

    const email = document.getElementById("emailsignup").value;
    const rawUsername = document.getElementById("usernamesignup").value;
    const username = rawUsername.replace(' ', '_');
    const rawFullName = document.getElementById("fullnamesignup").value;
    const fullName = rawFullName.replace(' ', '_');
    const password = document.getElementById("passwordsignup").value;

    const signUpUrl = fixedUrl + 'users/signup?email=' + email + '&&password=' + password + '&&fullname=' + fullName + '&&username=' + username;

    fetchData(signUpUrl, "POST", (data) => {
        if (data.authorization) {
            console.log(data);
            localStorage.setItem("token", data.authorization);  //save token to use
            localStorage.setItem("didLogIn", true); //save login state

            //TODO: Navigate to main page
            window.location.href = "home.html";
        } else {
            const alert = document.getElementById("register").getElementsByClassName("red")[0];
            alert.classList.remove("hidden");
        }
    });
});