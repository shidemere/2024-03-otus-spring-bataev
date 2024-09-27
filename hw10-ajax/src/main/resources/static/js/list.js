// Функция для получения данных о книгах с сервера
async function fetchBooks() {
    try {
        const response = await fetch('api/v1/book');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const books = await response.json();
        populateBookTable(books);
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }
}

// Функция для заполнения таблицы книгами
function populateBookTable(books) {
    const tableBody = document.getElementById('book-table-body');
    tableBody.innerHTML = '';  // Очищаем таблицу перед заполнением

    books.forEach(book => {
        const row = document.createElement('tr');

        // Столбцы с данными о книге
        row.innerHTML = `
            <td>${book.id}</td>
            <td>${book.title}</td>
            <td>${book.author.fullName}</td>
            <td>${book.genre.name}</td>
            <td><a href="/edit/${book.id}">Edit</a></td>
            <td><a href="#" class="delete-link" data-book-id="${book.id}">Delete</a></td>
            <td><a href="comment-list/${book.id}">View Comments</a></td>
        `;

        tableBody.appendChild(row);
    });

    // Добавляем обработчик клика для ссылок удаления
    const deleteLinks = document.querySelectorAll('.delete-link');
    deleteLinks.forEach(link => {
        link.addEventListener('click', async (e) => {
            e.preventDefault(); // Останавливаем действие по умолчанию для ссылки
            const bookId = e.target.dataset.bookId;
            await deleteBook(bookId);
            fetchBooks(); // Обновляем таблицу после удаления
        });
    });
}

// Функция для удаления книги
async function deleteBook(id) {
    try {
        const response = await fetch(`api/v1/book/${id}`, { method: 'DELETE' });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        console.log(`Book with ID ${id} deleted successfully`);
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }
}

// Запуск функции для получения книг после загрузки страницы
document.addEventListener('DOMContentLoaded', fetchBooks);
