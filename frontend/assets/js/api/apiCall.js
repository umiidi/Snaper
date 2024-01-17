const url = "http://localhost:8080/api/v1"

export async function fetchData(path, method, data) {
    const requestOptions = {
        method: method,
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem("accessToken"),
            'Content-Type': 'application/json',
        }
    };
    if (data !== null) {
        requestOptions.body = JSON.stringify(data);
    }
    return await fetch(`${url}${path}`, requestOptions);
}