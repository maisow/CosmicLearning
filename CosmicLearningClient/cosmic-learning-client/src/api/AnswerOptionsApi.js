const API_URL = "http://localhost:8080/cosmic-learning/answer-options";

export async function fetchAnswerOptionsByQuestion(questionId, jwtToken) {
  const response = await fetch(`${API_URL}/${questionId}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
  });

  if (response.status === 200) {
    return await response.json();
  } else if (response.status === 404) {
    return { error: "No answer options found" };
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Unexpected error");
  }
}

export async function createAnswerOption(answerOptionData, jwtToken) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(answerOptionData),
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

export async function updateAnswerOption(answerOptionData, jwtToken) {
  const response = await fetch(
    `${API_URL}/${answerOptionData.answerOptionId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify(answerOptionData),
    }
  );

  if (response.status === 204) {
    return { message: "Updated successfully" };
  } else if (response.status === 400) {
    return await response.json();
  } else if (response.status === 404) {
    return { error: "Answer option not found" };
  } else if (response.status === 409) {
    return { error: "ID mismatch in path and payload" };
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Unexpected error");
  }
}

// Delete an answer option by ID
export async function deleteAnswerOptionById(answerOptionId, jwtToken) {
  const response = await fetch(`${API_URL}/${answerOptionId}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${jwtToken}`,
    },
  });

  if (response.status === 204) {
    return { message: "Deleted successfully" };
  } else if (response.status === 404) {
    return { error: "Answer option not found" };
  } else if (response.status === 400) {
    return await response.json();
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Unexpected error");
  }
}
