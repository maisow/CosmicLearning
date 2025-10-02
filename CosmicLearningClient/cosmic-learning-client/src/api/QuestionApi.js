const API_URL = "http://localhost:8080/cosmic-learning/question";

export async function fetchByProfessorId(id) {
  const response = await fetch(`${API_URL}/by-professor/${id}`);
  if (response.status === 200) {
    return await response.json();
  } else {
    throw new Error("Not found");
  }
}

export async function fetchBySubjectId(id) {
  const response = await fetch(`${API_URL}/by-subject/${id}`);
  if (response.status === 200) {
    return await response.json();
  } else {
    throw new Error("Not found");
  }
}

export async function fetchQuestionById(questionId) {
  const response = await fetch(`${API_URL}/${questionId}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  });

  if (response.ok) {
    return await response.json();
  } else if (response.status === 404) {
    return { error: "Question not found" };
  } else {
    return Promise.reject("Failed to fetch question");
  }
}

export async function updateQuestion(question, jwtToken) {
  const options = {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(question),
  };

  const response = await fetch(`${API_URL}/${question.questionId}`, options);

  if (response.status === 204) {
    return { message: "Updated successfully" };
  } else if (response.status === 400) {
    return await response.json();
  } else if (response.status === 403 || response.status === 401) {
    return { error: "Unauthorized Update" };
  } else if (response.status === 404) {
    return { error: "Not found" };
  } else {
    return Promise.reject("Unexpected error");
  }
}

export async function createQuestion(question, jwtToken) {
  const options = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(question),
  };

  const response = await fetch(API_URL, options);

  if (response.status === 201) {
    return response.json();
  } else if (response.status === 400) {
    return response.json();
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Unexpected error");
  }
}

export async function deleteQuestionById(id, jwtToken) {
  const options = {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${jwtToken}`,
    },
  };

  const response = await fetch(`${API_URL}/${id}`, options);

  if (response.status === 204) {
    return { message: "Deleted successfully" };
  } else if (response.status === 404) {
    return { error: "Not found" };
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Unexpected error");
  }
}
