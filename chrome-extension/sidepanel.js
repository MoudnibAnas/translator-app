// sidepanel.js

console.log('Side panel script loaded');

// Éléments DOM
const appFrame = document.getElementById('appFrame');
const loading = document.getElementById('loading');
const errorMessage = document.getElementById('errorMessage');

// Cacher le loading après un délai
setTimeout(() => {
    loading.style.display = 'none';
}, 3000);

// Vérifier si l'iframe est chargée
appFrame.addEventListener('load', () => {
    console.log('Iframe loaded');
    loading.style.display = 'none';
    
    // Envoyer un message à l'app React
    try {
        appFrame.contentWindow.postMessage({
            type: 'FROM_EXTENSION',
            message: 'Extension ready'
        }, 'http://localhost:3000');
    } catch (error) {
        console.log('Could not post message to iframe:', error);
    }
});

// Écouter les messages du service worker
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    console.log('Message received in side panel:', message);
    
    if (message.action === 'translateText' && message.text) {
        // Envoyer le texte à l'app React
        try {
            appFrame.contentWindow.postMessage({
                type: 'TRANSLATE_TEXT',
                text: message.text
            }, 'http://localhost:3000');
            console.log('Text sent to React app:', message.text);
        } catch (error) {
            console.error('Error sending text to React app:', error);
            errorMessage.style.display = 'block';
            errorMessage.textContent = 'Error: Could not send text to translator. Please refresh.';
        }
    }
    
    sendResponse({ success: true });
    return true;
});

// Écouter les messages de l'app React
window.addEventListener('message', (event) => {
    // Vérifier l'origine pour la sécurité
    if (event.origin !== 'http://localhost:3000') {
        return;
    }
    
    console.log('Message from React app:', event.data);
    
    if (event.data.type === 'APP_READY') {
        console.log('React app is ready');
        loading.style.display = 'none';
    }
    
    if (event.data.type === 'ERROR') {
        errorMessage.style.display = 'block';
        errorMessage.textContent = event.data.message;
    }
});

// Gérer les erreurs de l'iframe
appFrame.addEventListener('error', () => {
    console.error('Iframe failed to load');
    loading.style.display = 'none';
    errorMessage.style.display = 'block';
});