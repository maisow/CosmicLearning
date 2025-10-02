const API_URL = "http://localhost:8080/cosmic-learning/user";

export async function fetchProfileById(id) {
  const response = await fetch(`${API_URL}/${id}`);
  if (response.status === 200) {
    return await response.json();
  } else {
    throw new Error("Not found");
  }
}

export async function createProfile(user, jwtToken) {
  const options = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(user),
  };

  const response = await fetch(API_URL, options);

  if (response.status === 201) {
    return response.json();
  } else if (response.status === 400) {
    return response.json();
  } else if (response.status === 403) {
    return ["Unauthorized Create"];
  } else {
    return Promise.reject();
  }
}

export async function updateProfile(user, jwtToken) {
  const options = {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(user),
  };

  const response = await fetch(`${API_URL}/${user.userId}`, options);

  if (response.status === 400) {
    return await response.json();
  } else if (response.status === 403) {
    return ["Unauthorized Update"];
  } else if (response.status === 404) {
    return ["Not found"];
  } else if (response.ok) {
    const text = await response.text();
    return text ? JSON.parse(text) : {};
  } else {
    return Promise.reject("Unexpected error");
  }
}

export async function fetchAllUsers() {
  const response = await fetch(API_URL, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  });

  if (response.status === 200) {
    return await response.json();
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Somethin Went Wrong!");
  }
}
