const API_URL = "http://localhost:8080/cosmic-learning/subject";

export async function fetchAllSubjects() {
  const response = await fetch(API_URL, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  });

  if (response.status === 200) {
    return await response.json();
  } else if (response.status === 403 || response.status === 401) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Unexpected error");
  }
}

export async function fetchSubjectById(subjectId) {
  const response = await fetch(`${API_URL}/${subjectId}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  });

  if (response.ok) {
    return await response.json();
  } else if (response.status === 404) {
    return { error: "Subject not found" };
  } else {
    return Promise.reject("Failed to fetch subject");
  }
}

export async function createSubject(subject, jwtToken) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(subject),
  });

  if (response.status === 201) {
    return await response.json();
  } else if (response.status === 400) {
    return await response.json();
  } else if (response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Failed to create subject.");
  }
}

export async function updateSubject(subject, jwtToken) {
  const response = await fetch(`${API_URL}/${subject.subjectId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(subject),
  });

  if (response.status === 204) {
    return { message: "Subject updated successfully" };
  } else if (response.status === 400) {
    return await response.json();
  } else if (response.status === 403) {
    return { error: "Unauthorized" };
  } else if (response.status === 404) {
    return { error: "Subject not found" };
  } else if (response.status === 409) {
    return { error: "ID mismatch" };
  } else {
    return Promise.reject("Failed to update subject.");
  }
}

export async function deleteSubjectById(subjectId, jwtToken) {
  const response = await fetch(`${API_URL}/${subjectId}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${jwtToken}`,
    },
  });

  if (response.status === 204) {
    return { message: "Subject deleted successfully" };
  } else if (response.status === 404) {
    return { error: "Subject not found" };
  } else if (response.status === 400) {
    return await response.json();
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Failed to delete subject.");
  }
}
