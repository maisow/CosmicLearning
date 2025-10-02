const API_URL = "http://localhost:8080/cosmic-learning/review";

export async function fetchReviewsByProfessorId(professorId) {
  const response = await fetch(`${API_URL}/${professorId}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  });

  if (response.ok) {
    return await response.json();
  } else if (response.status === 404) {
    return { error: "No reviews found." };
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Something went wrong:(");
  }
}

export async function createReview(review, jwtToken) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(review),
  });

  if (response.status === 201) {
    return await response.json();
  } else if (response.status === 400) {
    return await response.json();
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Something went wrong:(");
  }
}

export async function updateReview(review, jwtToken) {
  const response = await fetch(`${API_URL}/${review.reviewId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify(review),
  });

  if (response.status === 204) {
    return { message: "Review updated successfully." };
  } else if (response.status === 400) {
    return await response.json();
  } else if (response.status === 404) {
    return { error: "Review not found." };
  } else if (response.status === 409) {
    return { error: "error." };
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized update." };
  } else {
    return Promise.reject("Something went wrong");
  }
}

export async function deleteReviewById(reviewId, jwtToken) {
  const response = await fetch(`${API_URL}/${reviewId}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${jwtToken}`,
    },
  });

  if (response.status === 204) {
    return { message: "Review deleted successfully." };
  } else if (response.status === 404) {
    return { error: "Review not found." };
  } else if (response.status === 401 || response.status === 403) {
    return { error: "Unauthorized" };
  } else {
    return Promise.reject("Something went wrong:(");
  }
}
