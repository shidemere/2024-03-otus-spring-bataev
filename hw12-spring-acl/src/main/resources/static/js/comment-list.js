// Функция для получения параметра bookId из URL пути
function getBookIdFromUrl() {
    const urlParts = window.location.pathname.split('/');
    return urlParts[urlParts.length - 1]; // bookId будет последним элементом в URL
}

// Функция для загрузки комментариев
async function loadComments() {
    const bookId = getBookIdFromUrl();

    try {
        const response = await fetch(`/api/v1/comment/${bookId}`);
        if (!response.ok) {
            throw new Error('Failed to fetch comments');
        }

        const comments = await response.json();
        populateComments(comments);
    } catch (error) {
        console.error('Error fetching comments:', error);
    }
}

// Функция для отображения комментариев на странице
function populateComments(comments) {
    const commentsList = document.getElementById('comments-list');
    commentsList.innerHTML = ''; // Очищаем список перед добавлением

    comments.forEach(comment => {
        const commentDiv = document.createElement('div');
        commentDiv.classList.add('comment');

        const commentText = document.createElement('p');
        commentText.textContent = comment.text;

        commentDiv.appendChild(commentText);
        commentsList.appendChild(commentDiv);
    });

    if (comments.length === 0) {
        commentsList.innerHTML = '<p>No comments available for this book.</p>';
    }
}

// Загружаем комментарии после загрузки страницы
document.addEventListener('DOMContentLoaded', loadComments);
