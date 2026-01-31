// content.js
console.log("Content Script injecté et prêt.");

document.addEventListener('mouseup', () => {
    const selectedText = window.getSelection().toString().trim();
    
    if (selectedText.length > 0) {
        console.log("Texte sélectionné détecté :", selectedText);
        
        // Envoi au Side Panel
        chrome.runtime.sendMessage({ 
            type: "TEXT_SELECTED", 
            text: selectedText 
        }, (response) => {
            if (chrome.runtime.lastError) {
                console.log("Le Side Panel n'est pas encore ouvert.");
            }
        });
    }
});