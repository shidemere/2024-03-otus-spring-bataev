// Получение параметра ID из URL
function getBookIdFromURL() {
    const url = window.location.pathname; // Получаем путь из URL
    const id = url.substring(url.lastIndexOf('/') + 1); // Извлекаем ID после последнего "/"
    return id;
}

// Функция для загрузки авторов, жанров и данных книги
async function loadPageData() {
    const bookId = getBookIdFromURL();

    try {
        // Загружаем авторов, жанры и книгу параллельно
        const [authorsResponse, genresResponse, bookResponse] = await Promise.all([
            fetch('/api/v1/author'),
            fetch('/api/v1/genre'),
            fetch(`/api/v1/book/${bookId}`)
        ]);

        if (!authorsResponse.ok || !genresResponse.ok || !bookResponse.ok) {
            throw new Error('Failed to fetch data');
        }

        const authors = await authorsResponse.json();
        const genres = await genresResponse.json();
        const book = await bookResponse.json();

        // Заполняем выпадающие списки
        populateSelect('book-author-input', authors, 'fullName');
        populateSelect('book-genre-input', genres, 'name');

        // После загрузки авторов и жанров, заполняем форму данными книги
        populateEditForm(book);
    } catch (error) {
        console.error('Error loading data:', error);
    }
}

// Заполняем форму данными книги
function populateEditForm(book) {
    document.getElementById('id-input').value = book.id;
    document.getElementById('book-title-input').value = book.title;
    document.getElementById('book-author-input').value = book.author.id;
    document.getElementById('book-genre-input').value = book.genre.id;
}

// Заполняем выпадающий список (select) значениями
function populateSelect(selectId, items) {
    const selectElement = document.getElementById(selectId);
    selectElement.innerHTML = ''; // Очищаем текущие опции
    items.forEach(item => {
        const option = document.createElement('option');
        option.value = item.id;

        // Используем fullName для авторов и name для жанров
        option.textContent = item.fullName ? item.fullName : item.name;

        selectElement.appendChild(option);
    });
}

// Сохранение изменений книги
async function saveBookData(event) {
    event.preventDefault(); // Предотвращаем перезагрузку страницы
    const bookId = getBookIdFromURL();
    const title = document.getElementById('book-title-input').value;
    const authorId = document.getElementById('book-author-input').value;
    const genreId = document.getElementById('book-genre-input').value;

    const updatedBook = {
        id: bookId,
        title: title,
        authorId: authorId,
        genreId: genreId
    };

    try {
        const response = await fetch(`/api/v1/book/${bookId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedBook)
        });

        if (!response.ok) {
            throw new Error('Failed to save book data');
        }

        // Перенаправляем на страницу списка после успешного сохранения
        window.location.href = '/list';
    } catch (error) {
        console.error('Error saving book data:', error);
    }
}

// Привязываем событие сохранения формы
document.getElementById('edit-form').addEventListener('submit', saveBookData);

// Подгружаем данные при загрузке страницы
document.addEventListener('DOMContentLoaded', loadPageData);

// Обработка кнопки Cancel для перенаправления на страницу /list
document.querySelector('.cancel-button').addEventListener('click', (event) => {
    event.preventDefault(); // Останавливаем переход по умолчанию
    window.location.href = '/list'; // Перенаправляем на страницу списка
});
