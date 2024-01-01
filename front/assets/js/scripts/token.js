export function isAccessTokenExpired() {
    const accessToken = localStorage.getItem("accessToken")
    if (!accessToken) return true;
    const token = parseJwt(localStorage.getItem("accessToken"))
    const currentTime = new Date()
    const expirationTime = new Date(token.exp * 1000)
    console.log(expirationTime)
    return currentTime > expirationTime
}

function parseJwt(token) {
    var base64Url = token.split('.')[1];
    var payload = atob(base64Url)
    return JSON.parse(payload);
}