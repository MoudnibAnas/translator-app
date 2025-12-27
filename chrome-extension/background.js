// background.js - Service Worker pour l'extension

console.log('Tarjmaan Translator background service worker loaded');

// Initialisation de l'extension
chrome.runtime.onInstalled.addListener(() => {
  console.log('Extension installed or updated');
  
  // Configurer le side panel
  chrome.sidePanel.setPanelBehavior({ openPanelOnActionClick: true })
    .then(() => {
      console.log('Side panel configured successfully');
    })
    .catch((error) => {
      console.error('Error configuring side panel:', error);
    });
  
  // Créer le menu contextuel
  try {
    chrome.contextMenus.create({
      id: "translateSelection",
      title: "Translate to Darija",
      contexts: ["selection"]
    });
    console.log('Context menu created successfully');
  } catch (error) {
    console.error('Error creating context menu:', error);
  }
});

// Gérer les clics sur le menu contextuel
chrome.contextMenus.onClicked.addListener((info, tab) => {
  console.log('Context menu clicked:', info.menuItemId);
  
  if (info.menuItemId === "translateSelection" && info.selectionText) {
    const selectedText = info.selectionText.trim();
    console.log('Selected text:', selectedText);
    
    // Ouvrir le side panel
    chrome.sidePanel.open({ windowId: tab.windowId })
      .then(() => {
        console.log('Side panel opened');
        
        // Envoyer le texte au side panel après un court délai
        setTimeout(() => {
          chrome.runtime.sendMessage({
            action: 'translateText',
            text: selectedText
          }).catch(error => {
            console.log('Could not send message to side panel:', error);
          });
        }, 500);
      })
      .catch(error => {
        console.error('Error opening side panel:', error);
      });
  }
});

// Gérer les messages depuis le side panel
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  console.log('Message received in background:', message);
  
  if (message.action === 'getSettings') {
    // Retourner les paramètres
    chrome.storage.local.get(['apiUrl'], (result) => {
      sendResponse({
        apiUrl: result.apiUrl || 'http://localhost:8080',
        reactAppUrl: 'http://localhost:3000'
      });
    });
    return true; // Indique que sendResponse sera appelé de manière asynchrone
  }
  
  if (message.action === 'saveSettings') {
    // Sauvegarder les paramètres
    chrome.storage.local.set({
      apiUrl: message.apiUrl
    }, () => {
      sendResponse({ success: true });
    });
    return true;
  }
});