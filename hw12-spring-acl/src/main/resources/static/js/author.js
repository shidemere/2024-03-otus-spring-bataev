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

// Обработчик отправки формы
document.getElementById('add-author-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const fullName = document.getElementById('author-title-input').value;

    const newAuthor = {
        fullName
    };

    try {
        const response = await fetch('/api/v1/author', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newAuthor)
        });

        if (response.ok) {
            // Показываем уведомление после успешного добавления
            showNotification('Author was added');

            // Очищаем форму для добавления нового автора
            document.getElementById('add-author-form').reset();
        } else if (response.status === 403) {
            // Показываем уведомление об ошибке 403
            showNotification('Недостаточно прав для добавления', true);
        } else {
            // Обработка других ошибок
            console.error('Failed to add the author. Status code:', response.status);
            showNotification('Произошла ошибка при добавлении автора', true);
        }
    } catch (error) {
        console.error('Error:', error);
        showNotification('Произошла ошибка при добавлении автора', true);
    }
});
