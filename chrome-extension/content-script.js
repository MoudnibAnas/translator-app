// Inject floating button on web pages
const button = document.createElement('button');
button.innerHTML = '🌐 Translate';
button.style.cssText = `
    position: fixed;
    bottom: 20px;
    right: 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
    border-radius: 25px;
    padding: 10px 20px;
    font-size: 14px;
    font-weight: bold;
    cursor: pointer;
    z-index: 999999;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
`;
button.addEventListener('click', () => {
    chrome.runtime.sendMessage({ action: 'openSidePanel' });
});
document.body.appendChild(button);