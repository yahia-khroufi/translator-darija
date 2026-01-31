// popup.js
console.log("Popup.js chargé et en attente de messages...");

// 1. ÉCOUTEUR GLOBAL
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    console.log("Message reçu dans le Side Panel :", request);

    if (request.type === "TEXT_SELECTED") {
        const inputField = document.getElementById('userMessage');
        if (inputField) {
            inputField.value = request.text;
            inputField.focus();
            // Petit flash bleu pour confirmer la réception
            inputField.style.backgroundColor = "#e0e7ff";
            setTimeout(() => inputField.style.backgroundColor = "white", 300);
        }
    }
    return true; 
});

// 2. LOGIQUE DU FORMULAIRE
document.addEventListener('DOMContentLoaded', () => {
    const chatForm = document.getElementById('chatForm');
    const chatOutput = document.getElementById('chatOutput');
    const inputField = document.getElementById('userMessage');

    const appendMessage = (text, sender) => {
        const div = document.createElement('div');
        div.className = `bubble ${sender}`;
        div.textContent = text;
        chatOutput.appendChild(div);
        chatOutput.scrollTop = chatOutput.scrollHeight;
    };

    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const text = inputField.value.trim();
        if (!text) return;

        appendMessage(text, 'user');
        inputField.value = '';

        try {
            const response = await fetch('http://localhost:8080/TranslateService/api/translate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ inputText: text })
            });

            const data = await response.json();
            appendMessage(data.translatedText || "Erreur de traduction", 'bot');
        } catch (error) {
            appendMessage("Erreur : Le serveur Java ne répond pas.", 'bot');
        }
    });
});