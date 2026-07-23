// borrow.js - Borrow history and book returning operations
let currentUser = null;

async function initBorrowPage() {
  currentUser = checkAuth();
  if (!currentUser) return;

  if (currentUser.role === 'LIBRARIAN') {
    document.getElementById("historyTabTitle").innerText = "All Borrow Records";
  } else {
    document.getElementById("historyTabTitle").innerText = "My Borrow History";
  }
  loadBorrowHistory();
}

async function loadBorrowHistory() {
  try {
    const url = currentUser.role === 'LIBRARIAN' ? `${BORROW_API}/records` : `${BORROW_API}/history/${currentUser.id}`;
    const records = await apiRequest(url);
    const tbody = document.getElementById("historyTableBody");
    if (!tbody) return;
    tbody.innerHTML = "";

    records.forEach(rec => {
      const tr = document.createElement("tr");
      const fineText = rec.fine > 0 ? `<strong class="text-danger">₹${rec.fine}</strong>` : "₹0.0";
      
      let action = "-";
      if (currentUser.role === 'LIBRARIAN' && !rec.returnDate) {
        action = `<button class="btn btn-sm btn-outline-warning" onclick="returnBook(${rec.id})">Mark Returned</button>`;
      } else if (!rec.returnDate) {
        action = `<span class="text-warning">Borrowed</span>`;
      } else {
        action = `<span class="text-success">Returned</span>`;
      }

      tr.innerHTML = `
        <td>${rec.id}</td>
        <td><strong>${escapeHtml(rec.bookTitle)}</strong></td>
        <td>${escapeHtml(rec.userName)}</td>
        <td>${rec.borrowDate}</td>
        <td>${rec.dueDate}</td>
        <td>${rec.returnDate || '<span class="text-muted">Not returned</span>'}</td>
        <td>${fineText}</td>
        <td>${action}</td>
      `;
      tbody.appendChild(tr);
    });
  } catch(err) {}
}

async function returnBook(borrowId) {
  try {
    await apiRequest(`${BORROW_API}/return/${borrowId}`, 'POST');
    showAlert("Book returned successfully!");
    loadBorrowHistory();
  } catch(err) {}
}
