document.addEventListener('DOMContentLoaded', function() {
    const greetingElement = document.querySelector('.greeting');

    if (greetingElement) {
        const now = new Date();
        const hour = now.getHours();
        let greetingText = "Hello";

        if (hour < 12) {
            greetingText = "Good morning";
        } else if (hour < 18) {
            greetingText = "Good afternoon";
        } else {
            greetingText = "Good evening";
        }

        greetingElement.innerHTML = `🚀 ${greetingText}! Welcome to Your Financial Adventure! 🌟`;
    }
});
