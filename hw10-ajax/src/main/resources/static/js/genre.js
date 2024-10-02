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

// Обработчик отправки формы
document.getElementById('add-genre-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const name = document.getElementById('genre-name-input').value;

    const newGenre = {
        name
    };

    try {
        const response = await fetch('/api/v1/genre', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newGenre)
        });

        if (response.ok) {
            // Показываем уведомление после успешного добавления
            showNotification();

            // Очищаем форму для добавления нового жанра
            document.getElementById('add-genre-form').reset();
        } else {
            console.error('Failed to add the genre');
        }
    } catch (error) {
        console.error('Error:', error);
    }
});
