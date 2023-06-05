
const BASE_URL = "http://oauth2-server.com:8082"
const AUTH_PATH = "/oauth2/authorize";
const TOKEN_PATH = "/oauth2/token"
const SCOPES = "tag.read certificate.write user.read";
const CLIENT_ID = "certificates-ui-client";
const REDIRECT_URI = "http://127.0.0.1:8080/certificates";
const CLIENT_SECRET = 'secret';

export const AUTHORIZE_ENDPOINT = `${BASE_URL}${AUTH_PATH}?response_type=code&client_id=${CLIENT_ID}&scope=${SCOPES}&redirect_uri=${REDIRECT_URI}`;
export { BASE_URL, SCOPES, CLIENT_ID, REDIRECT_URI, CLIENT_SECRET, AUTH_PATH, TOKEN_PATH }

