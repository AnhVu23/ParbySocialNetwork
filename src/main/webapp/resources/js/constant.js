const fixedUrl = "http://10.114.34.134:8080/ParbySocialNetwork/rest/";

const getCurrentUserInfoUrl = fixedUrl + "users/home";

const json = (res) => {
    return res.json();
}

const fetchData = (url, httpMethod, callback) => {
    fetch(url, {
        header: {
            'Content-Type': 'application/json',
            'authorization': localStorage.getItem("token")
        },
        method: httpMethod
    })
    .then(json)
    .then((data) => {
        if (data.hasOwnProperty('error')) {
            alert(data.error);
        } else {
            console.log(data);

            callback(data);
        }
    }).catch((error) => {
        console.log('error: ' + error);
        callback({});
    });
}
