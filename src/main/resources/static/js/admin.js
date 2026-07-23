// admin.js - Admin operations
let currentUser = null;

async function initAdminPage() {
  currentUser = checkAuth('LIBRARIAN');
  if (!currentUser) return;

  loadUsers();
}

async function loadUsers() {
  try {
    const users = await apiRequest('/users');
    const tbody = document.getElementById("usersTableBody");
    if (!tbody) return;
    tbody.innerHTML = "";

    users.forEach(u => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${u.id}</td>
        <td>${escapeHtml(u.name)}</td>
        <td>${escapeHtml(u.email)}</td>
        <td><span class="badge bg-secondary">${u.role}</span></td>
      `;
      tbody.appendChild(tr);
    });
  } catch(err) {}
}
