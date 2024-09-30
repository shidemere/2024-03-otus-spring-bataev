// Функция для загрузки списка книг
async function loadBooks() {
    try {
        const response = await fetch('/api/v1/book');
        if (!response.ok) {
            throw new Error('Failed to fetch books');
        }

        const books = await response.json();
        populateBookSelect(books);
    } catch (error) {
        console.error('Error fetching books:', error);
    }
}

// Функция для заполнения селектора книг
function populateBookSelect(books) {
    const bookSelect = document.getElementById('book-select-input');
    bookSelect.innerHTML = '<option value="" disabled selected>Select a book</option>'; // Очищаем перед добавлением

    books.forEach(book => {
        const option = document.createElement('option');
        option.value = book.id;
        option.textContent = book.title;
        bookSelect.appendChild(option);
    });
}

// Функция для отображения уведомления
function showNotification() {
    const notification = document.getElementById('notification');
    notification.classList.add('show');

    // Скрыть уведомление через 3 секунды
    setTimeout(() => {
        notification.classList.remove('show');
        notification.classList.add('hide');

        // После анимации скрытия, убираем уведомление
        setTimeout(() => {
            notification.classList.remove('hide');
        }, 500);
    }, 3000);
}

// Обработчик отправки формы для добавления комментария
document.getElementById('add-comment-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const text = document.getElementById('comment-text-input').value;
    const bookId = document.getElementById('book-select-input').value;

    // Формируем объект с минимальными данными для передачи
    const newComment = {
        text,
        book: {
            id: bookId, // Передаём только ID книги
        }
    };

    try {
        const response = await fetch('/api/v1/comment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newComment) // Отправляем минимальный объект с ID книги
        });

        if (response.ok) {
            // Показываем уведомление после успешного добавления
            showNotification();

            // Очищаем форму для добавления нового комментария
            document.getElementById('add-comment-form').reset();
        } else {
            console.error('Failed to add the comment');
        }
    } catch (error) {
        console.error('Error:', error);
    }
});

// Загружаем список книг после загрузки страницы
document.addEventListener('DOMContentLoaded', loadBooks);
