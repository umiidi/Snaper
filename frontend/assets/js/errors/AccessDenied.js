class AccessDeniedError extends Error {
    constructor(message) {
        super(message);
        this.name = "AccessDeniedError";
    }
}

export default AccessDeniedError;