const url = "https://snaper-app.onrender.com/api/v1"

export async function fetchData(path, method, data) {
    const requestOptions = {
        method: method,
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem("accessToken"),
            'Content-Type': 'application/json',
        }
    };
    console.log("token:", requestOptions.headers.Authorization)
    if (data !== null) {
        requestOptions.body = JSON.stringify(data);
    }
    return await fetch(`${url}${path}`, requestOptions);
}
