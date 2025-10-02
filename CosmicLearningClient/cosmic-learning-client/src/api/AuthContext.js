const API_URL = "http://localhost:8080";

export async function fetchProfileById(id) {
  const response = await fetch(`${API_URL}/${id}`);
  if (response.status === 200) {
    return await response.json();
  } else {
    throw new Error("Not found");
  }
}

export async function authenticate(credentials) {
  const options = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify(credentials),
  };

  const response = await fetch(`${API_URL}/authenticate`, options);

  if (response.status === 200) {
    const json = await response.json();
    const user = tokenToUser(json.jwt_token);
    return {
      token: json.jwt_token,
      user,
    };
  } else {
    return Promise.reject();
  }
}

export async function refreshToken() {
  const token = localStorage.getItem("jwt_token");

  const options = {
    method: "POST",
    headers: {
      Accept: "application/json",
      Authorization: `Bearer ${token}`,
    },
  };

  const response = await fetch(`${API_URL}/refresh-token`, options);
  if (response.status === 200) {
    const json = await response.json();
    return tokenToUser(json.jwt_token);
  } else {
    return Promise.reject();
  }
}

export async function register(username, password, roles) {
  const options = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify({ username, password, roles }),
  };

  const response = await fetch(`${API_URL}/create-account`, options);

  if (response.status === 201) {
    return Promise.resolve();
  } else if (response.status === 400) {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

const tokenToUser = (token) => {
  localStorage.setItem("jwt_token", token);
  const segments = token.split(".");
  const payload = segments[1];
  const user = JSON.parse(atob(payload));
  user.authorities = user.authorities.split(",");
  user.hasAnyAuthority = function (...authorities) {
    for (const authority of authorities) {
      if (this.authorities.includes(authority)) {
        return true;
      }
    }
    return false;
  };

  return user;
};

export default { authenticate, refreshToken, register };
