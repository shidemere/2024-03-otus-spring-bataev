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

// Функция для отображения уведомления с произвольным сообщением
function showNotification(message, isError = false) {
    const notification = document.getElementById('notification');
    notification.textContent = message;  // Устанавливаем текст уведомления

    // Если это ошибка, изменяем стиль уведомления
    if (isError) {
        notification.style.backgroundColor = '#dc3545'; // Красный цвет для ошибок
    } else {
        notification.style.backgroundColor = '#28a745'; // Зеленый цвет для успеха
    }

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
        bookId
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
            showNotification('Comment was added');

            // Очищаем форму для добавления нового комментария
            document.getElementById('add-comment-form').reset();
        } else if (response.status === 403) {
            // Показываем уведомление об ошибке 403
            showNotification('Недостаточно прав для добавления', true);
        } else {
            // Обработка других ошибок
            console.error('Failed to add the comment. Status code:', response.status);
            showNotification('Произошла ошибка при добавлении комментария', true);
        }
    } catch (error) {
        console.error('Error:', error);
        showNotification('Произошла ошибка при добавлении комментария', true);
    }
});

// Загружаем список книг после загрузки страницы
document.addEventListener('DOMContentLoaded', loadBooks);
