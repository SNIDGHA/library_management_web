// books.js - Book operations
let currentUser = null;

async function initBooksPage() {
  currentUser = checkAuth();
  if (!currentUser) return;

  if (currentUser.role === 'LIBRARIAN') {
    const wrapper = document.getElementById("addBookBtnWrapper");
    if (wrapper) wrapper.classList.remove("d-none");
  }
  loadBooks();
}

async function loadBooks(searchQuery = "") {
  try {
    const url = searchQuery ? `${BOOK_API}?search=${encodeURIComponent(searchQuery)}` : BOOK_API;
    const books = await apiRequest(url);
    const tbody = document.getElementById("booksTableBody");
    if (!tbody) return;
    tbody.innerHTML = "";
    
    books.forEach(book => {
      const tr = document.createElement("tr");
      
      let actionButtons = "";
      if (currentUser.role === 'LIBRARIAN') {
        actionButtons = `
          <button class="btn btn-sm btn-outline-secondary me-1" onclick="openEditModal(${book.id}, '${escapeHtml(book.title)}', '${escapeHtml(book.author)}', '${escapeHtml(book.isbn)}', ${book.available})">Edit</button>
          <button class="btn btn-sm btn-outline-danger" onclick="deleteBook(${book.id})">Delete</button>
        `;
      } else {
        actionButtons = book.available 
          ? `<button class="btn btn-sm btn-success" onclick="borrowBook(${book.id})">Borrow</button>`
          : `<button class="btn btn-sm btn-secondary" disabled>Unavailable</button>`;
      }

      tr.innerHTML = `
        <td>${book.id}</td>
        <td><strong>${escapeHtml(book.title)}</strong></td>
        <td>${escapeHtml(book.author)}</td>
        <td><code>${escapeHtml(book.isbn)}</code></td>
        <td>
          <span class="badge ${book.available ? 'badge-available' : 'badge-borrowed'}">
            ${book.available ? 'Available' : 'Borrowed'}
          </span>
        </td>
        <td>${actionButtons}</td>
      `;
      tbody.appendChild(tr);
    });
  } catch (err) {}
}

async function handleAddBook(e) {
  e.preventDefault();
  const title = document.getElementById("addBookTitle").value;
  const author = document.getElementById("addBookAuthor").value;
  const isbn = document.getElementById("addBookIsbn").value;

  try {
    await apiRequest(BOOK_API, 'POST', { title, author, isbn });
    showAlert("Book added successfully!");
    
    // Close modal
    const modalEl = document.getElementById('addBookModal');
    const modal = bootstrap.Modal.getInstance(modalEl);
    if (modal) modal.hide();
    
    document.getElementById("addBookForm").reset();
    loadBooks();
  } catch (err) {}
}

function openEditModal(id, title, author, isbn, available) {
  document.getElementById("editBookId").value = id;
  document.getElementById("editBookTitle").value = title;
  document.getElementById("editBookAuthor").value = author;
  document.getElementById("editBookIsbn").value = isbn;
  document.getElementById("editBookAvailable").checked = available;
  
  const modalEl = document.getElementById('editBookModal');
  const modal = new bootstrap.Modal(modalEl);
  modal.show();
}

async function handleEditBook(e) {
  e.preventDefault();
  const id = document.getElementById("editBookId").value;
  const title = document.getElementById("editBookTitle").value;
  const author = document.getElementById("editBookAuthor").value;
  const isbn = document.getElementById("editBookIsbn").value;
  const available = document.getElementById("editBookAvailable").checked;

  try {
    await apiRequest(`${BOOK_API}/${id}`, 'PUT', { title, author, isbn, available });
    showAlert("Book updated successfully!");
    
    // Close modal
    const modalEl = document.getElementById('editBookModal');
    const modal = bootstrap.Modal.getInstance(modalEl);
    if (modal) modal.hide();
    
    loadBooks();
  } catch(err) {}
}

async function deleteBook(id) {
  if (confirm("Are you sure you want to delete this book?")) {
    try {
      await apiRequest(`${BOOK_API}/${id}`, 'DELETE');
      showAlert("Book deleted successfully!");
      loadBooks();
    } catch(err) {}
  }
}

async function borrowBook(bookId) {
  try {
    await apiRequest(`${BORROW_API}/${currentUser.id}/${bookId}`, 'POST');
    showAlert("Book borrowed successfully!");
    loadBooks();
  } catch(err) {}
}

function handleSearch(e) {
  e.preventDefault();
  const query = document.getElementById("searchInput").value;
  loadBooks(query);
}

function clearSearch() {
  document.getElementById("searchInput").value = "";
  loadBooks();
}
