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
            showNotification();

            // Очищаем форму для добавления нового автора
            document.getElementById('add-author-form').reset();
        } else {
            console.error('Failed to add the author');
        }
    } catch (error) {
        console.error('Error:', error);
    }
});
