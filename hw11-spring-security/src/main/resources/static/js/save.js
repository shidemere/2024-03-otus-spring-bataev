// Функция для загрузки жанров
async function loadGenres() {
    try {
        const response = await fetch('/api/v1/genre');
        if (!response.ok) {
            throw new Error('Failed to fetch genres');
        }
        const genres = await response.json();
        const genreSelect = document.getElementById('book-genre-input');

        genreSelect.innerHTML = '<option value="" disabled selected>Select a genre</option>';

        genres.forEach(genre => {
            const option = document.createElement('option');
            option.value = genre.id;
            option.textContent = genre.name;
            genreSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error fetching genres:', error);
    }
}

// Функция для загрузки авторов
async function loadAuthors() {
    try {
        const response = await fetch('/api/v1/author');
        if (!response.ok) {
            throw new Error('Failed to fetch authors');
        }
        const authors = await response.json();
        const authorSelect = document.getElementById('book-author-input');

        authorSelect.innerHTML = '<option value="" disabled selected>Select an author</option>';

        authors.forEach(author => {
            const option = document.createElement('option');
            option.value = author.id;
            option.textContent = author.fullName;
            authorSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error fetching authors:', error);
    }
}

// Функция для отображения уведомления с произвольным сообщением
function showNotification(message, isError = false) {
    const notification = document.getElementById('notification');
    notification.textContent = message;  // Устанавливаем текст уведомления

    // Если это ошибка, то изменяем стиль уведомления
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

// Запуск загрузки данных при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    loadGenres();
    loadAuthors();
});

// Функция для обработки отправки формы (добавление книги)
document.getElementById('add-book-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const title = document.getElementById('book-title-input').value;
    const authorId = document.getElementById('book-author-input').value;
    const genreId = document.getElementById('book-genre-input').value;

    const newBook = {
        title,
        authorId,
        genreId
    };

    try {
        const response = await fetch('/api/v1/book', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newBook)
        });

        if (response.ok) {
            // Показываем уведомление после успешного добавления
            showNotification('Book was added');

            // Очищаем форму для добавления новой книги
            document.getElementById('add-book-form').reset();
        } else if (response.status === 403) {
            // Показываем уведомление об ошибке 403
            showNotification('Недостаточно прав для добавления', true);
        } else {
            // Обработка других ошибок
            console.error('Failed to add the book. Status code:', response.status);
            showNotification('Произошла ошибка при добавлении книги', true);
        }
    } catch (error) {
        console.error('Error:', error);
        showNotification('Произошла ошибка при добавлении книги', true);
    }
});
