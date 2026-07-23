// api.js - Centralized API calls and session expiration redirects
const API_BASE = '/users';
const BOOK_API = '/books';
const BORROW_API = '/borrow';

function showAlert(message, type = 'success') {
  const container = document.getElementById('alertContainer');
  if (!container) return;
  
  const alertDiv = document.createElement('div');
  alertDiv.className = `alert alert-${type} alert-dismissible fade show shadow`;
  alertDiv.role = 'alert';
  alertDiv.innerHTML = `
    ${message}
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  `;
  container.appendChild(alertDiv);
  setTimeout(() => {
    alertDiv.classList.remove('show');
    setTimeout(() => alertDiv.remove(), 150);
  }, 4000);
}

async function apiRequest(url, method = 'GET', body = null) {
  const token = localStorage.getItem("token") || "";
  const headers = {
    'Content-Type': 'application/json'
  };
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  
  const config = { method, headers };
  if (body) {
    config.body = JSON.stringify(body);
  }

  try {
    const response = await fetch(url, config);
    if (response.status === 204) return null;

    const data = await response.text();
    let jsonData = null;
    try {
      jsonData = data ? JSON.parse(data) : null;
    } catch(e) {
      jsonData = data;
    }

    if (!response.ok) {
      if (response.status === 401 || response.status === 403) {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        showAlert("Session expired or unauthorized. Redirecting to login...", "danger");
        setTimeout(() => {
          window.location.href = "index.html";
        }, 1500);
        return null;
      }
      const errorMsg = (jsonData && jsonData.message) ? jsonData.message : (jsonData || "Request failed");
      throw new Error(errorMsg);
    }

    return jsonData;
  } catch (err) {
    showAlert(err.message, "danger");
    throw err;
  }
}

function escapeHtml(str) {
  if (str === null || str === undefined) return "";
  if (typeof str !== 'string') return str.toString();
  return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
}
