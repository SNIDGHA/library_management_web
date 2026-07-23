// auth.js - Session guards, login, register, and logout logic

function checkAuth(requireRole = null) {
  const token = localStorage.getItem("token");
  const userStr = localStorage.getItem("user");
  
  if (!token || !userStr) {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "index.html";
    return null;
  }

  const user = JSON.parse(userStr);
  
  if (requireRole && user.role !== requireRole) {
    showAlert("Unauthorized access to this section", "danger");
    setTimeout(() => {
      window.location.href = "dashboard.html";
    }, 1500);
    return null;
  }

  // Populate nav links and greetings if navbar is present
  const userGreeting = document.getElementById("userGreeting");
  if (userGreeting) {
    userGreeting.innerText = `Hello, ${user.name} (${user.role})`;
  }

  const librarianNavLink = document.getElementById("librarianNavLink");
  if (librarianNavLink && user.role === 'LIBRARIAN') {
    librarianNavLink.classList.remove("d-none");
  }

  return user;
}

async function handleLogin(e) {
  e.preventDefault();
  const email = document.getElementById("loginEmail").value;
  const password = document.getElementById("loginPassword").value;

  try {
    const response = await apiRequest(`${API_BASE}/login`, 'POST', { email, password });
    if (response && response.token) {
      localStorage.setItem("token", response.token);
      localStorage.setItem("user", JSON.stringify(response.user));
      showAlert("Login successful! Redirecting...");
      setTimeout(() => {
        window.location.href = "dashboard.html";
      }, 1000);
    }
  } catch (err) {}
}

async function handleRegister(e) {
  e.preventDefault();
  const name = document.getElementById("registerName").value;
  const email = document.getElementById("registerEmail").value;
  const password = document.getElementById("registerPassword").value;
  const role = document.getElementById("registerRole").value;

  try {
    await apiRequest(`${API_BASE}/register`, 'POST', { name, email, password, role });
    showAlert("Registration successful! Redirecting to login...");
    setTimeout(() => {
      window.location.href = "index.html";
    }, 1500);
  } catch (err) {}
}

function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("user");
  window.location.href = "index.html";
}
