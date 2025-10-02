const API_URL = "http://localhost:8080/cosmic-learning/response";

export async function fetchUserResponse(
  userId,
  questionId,
  answerOptionId,
  jwtToken
) {
  const response = await fetch(
    `${API_URL}/${userId}/${questionId}/${answerOptionId}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    }
  );

  if (response.status === 200) {
    return await response.json();
  } else if (response.status === 404) {
    return { error: "Response not found" };
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Unexpected error");
  }
}

export async function saveResponse(responseData, jwtToken) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(responseData),
  });

  if (response.status === 201) {
    return await response.json();
  } else if (response.status === 400) {
    return await response.json();
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Unexpected error");
  }
}

export async function isAnswerCorrect(answerOptionId, jwtToken) {
  const response = await fetch(`${API_URL}/is-correct/${answerOptionId}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
  });

  if (response.status === 200) {
    return await response.json();
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Unexpected error");
  }
}
