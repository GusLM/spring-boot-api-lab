document.addEventListener('DOMContentLoaded', () => {
    const nameInput = document.getElementById('nameInput');
    const searchButton = document.getElementById('searchButton');
    const resultsContainer = document.getElementById('resultsContainer');
    const messageContainer = document.getElementById('messageContainer');

    const API_URL = 'http://localhost:8080/customers';

    searchButton.addEventListener('click', () => {
        const name = nameInput.value.trim();
        if (name) {
            fetchCustomers(name);
        } else {
            showMessage('Por favor, digite um nome para buscar.', 'error');
        }
    });

    // Permitir busca ao pressionar Enter
    nameInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            searchButton.click();
        }
    });

    async function fetchCustomers(name) {
        clearResults();
        showMessage('Buscando clientes...', 'loading');

        try {
            const response = await fetch(`${API_URL}?name=${encodeURIComponent(name)}`);

            if (response.status === 200) {
                const data = await response.json();
                handleResponse(data);
            } else {
                showMessage(`Erro na requisição: Status ${response.status}`, 'error');
            }
        } catch (error) {
            console.error('Erro ao consumir API:', error);
            showMessage('Não foi possível conectar à API. Verifique se o servidor local está rodando.', 'error');
        }
    }

    function handleResponse(data) {
        const customers = data.content || [];

        if (customers.length === 0) {
            showMessage('Nenhum cliente encontrado.', 'error');
            return;
        }

        hideMessage();
        customers.forEach(customer => {
            createCard(customer);
        });
    }

    function createCard(customer) {
        const card = document.createElement('div');
        card.className = 'card';

        card.innerHTML = `
            <h3>${customer.firstName} ${customer.lastName}</h3>
            <p><span class="label">Email:</span> ${customer.email}</p>
            <span class="id" title="Public ID">${customer.publicId}</span>
        `;

        resultsContainer.appendChild(card);
    }

    function showMessage(text, type) {
        messageContainer.textContent = text;
        messageContainer.className = `message-container ${type}`;
        messageContainer.style.display = 'block';
    }

    function hideMessage() {
        messageContainer.style.display = 'none';
    }

    function clearResults() {
        resultsContainer.innerHTML = '';
        hideMessage();
    }
});
