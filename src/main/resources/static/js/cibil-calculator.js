document.addEventListener('DOMContentLoaded', () => {
    // Get references to all input sliders and their value displays
    const paymentHistorySlider = document.getElementById('paymentHistory');
    const paymentHistoryValue = document.getElementById('paymentHistoryValue');
    const creditUtilizationSlider = document.getElementById('creditUtilization');
    const creditUtilizationValue = document.getElementById('creditUtilizationValue');
    const creditHistoryLengthSlider = document.getElementById('creditHistoryLength');
    const creditHistoryLengthValue = document.getElementById('creditHistoryLengthValue');
    const newCreditInquiriesSlider = document.getElementById('newCreditInquiries');
    const newCreditInquiriesValue = document.getElementById('newCreditInquiriesValue');

    // Get references to the calculate button and result display elements
    const calculateButton = document.getElementById('calculateCibil');
    const cibilScoreDisplay = document.getElementById('cibilScoreDisplay');
    const scoreEmoji = document.getElementById('scoreEmoji');
    const scoreMessage = document.getElementById('scoreMessage');
    const scoreDisplayContainer = document.querySelector('.score-display');

    // Function to update the displayed value next to the slider
    const updateSliderValue = (slider, valueDisplay, unit = '') => {
        valueDisplay.textContent = slider.value + unit;
    };

    // Add event listeners to update slider values dynamically
    paymentHistorySlider.addEventListener('input', () => updateSliderValue(paymentHistorySlider, paymentHistoryValue, '%'));
    creditUtilizationSlider.addEventListener('input', () => updateSliderValue(creditUtilizationSlider, creditUtilizationValue, '%'));
    creditHistoryLengthSlider.addEventListener('input', () => updateSliderValue(creditHistoryLengthSlider, creditHistoryLengthValue, ' years'));
    newCreditInquiriesSlider.addEventListener('input', () => updateSliderValue(newCreditInquiriesSlider, newCreditInquiriesValue, ''));

    // Initialize slider values on page load
    updateSliderValue(paymentHistorySlider, paymentHistoryValue, '%');
    updateSliderValue(creditUtilizationSlider, creditUtilizationValue, '%');
    updateSliderValue(creditHistoryLengthSlider, creditHistoryLengthValue, '%');
    updateSliderValue(newCreditInquiriesSlider, newCreditInquiriesValue, '');

    // Function to calculate the simulated CIBIL score
    const calculateSimulatedCibilScore = () => {
        // Get values from sliders
        const paymentHistory = parseInt(paymentHistorySlider.value); // 0-100
        const creditUtilization = parseInt(creditUtilizationSlider.value); // 0-100
        const creditHistoryLength = parseInt(creditHistoryLengthSlider.value); // 0-30
        const newCreditInquiries = parseInt(newCreditInquiriesSlider.value); // 0-10

        // --- Simple Simulation Logic ---
        // Base score starts in a good range
        let score = 650;

        // Payment History (most impactful)
        // Higher percentage of on-time payments, higher score
        score += (paymentHistory - 75) * 2; // Adjust based on deviation from 75%

        // Credit Utilization (lower is better)
        // Lower utilization (e.g., 30% is good), higher score
        score -= (creditUtilization - 30) * 1.5; // Penalize for higher utilization

        // Credit History Length (longer is better)
        // Longer history, higher score
        score += (creditHistoryLength - 5) * 5; // Reward for more years

        // New Credit Inquiries (fewer is better)
        // More inquiries, lower score
        score -= (newCreditInquiries - 1) * 10; // Penalize for more inquiries

        // Ensure score stays within CIBIL range (300-900)
        score = Math.max(300, Math.min(900, score));

        return Math.round(score); // Return a rounded integer score
    };

    // Function to update the score display and message
    const updateScoreDisplay = (score) => {
        cibilScoreDisplay.textContent = score;
        scoreDisplayContainer.classList.add('animate'); // Add class to trigger animation

        let message = '';
        let emoji = '';

        if (score >= 750) {
            message = "Fantastic! You're a credit superstar! 🎉";
            emoji = '🌟';
        } else if (score >= 650) {
            message = "Great job! Your credit health is looking good! 👍";
            emoji = '😊';
        } else if (score >= 550) {
            message = "You're on the right track! Keep building that credit! 💪";
            emoji = '🤔';
        } else {
            message = "Time to focus on your credit! Small steps make a big difference! 📈";
            emoji = '🧐';
        }
        scoreMessage.textContent = message;
        scoreEmoji.textContent = emoji;

        // Remove animation class after a short delay to allow re-triggering
        setTimeout(() => {
            scoreDisplayContainer.classList.remove('animate');
        }, 1000);
    };

    // Event listener for the calculate button
    calculateButton.addEventListener('click', () => {
        const simulatedScore = calculateSimulatedCibilScore();
        updateScoreDisplay(simulatedScore);
    });

    // Optional: Calculate initial score on page load
    updateScoreDisplay(calculateSimulatedCibilScore());
});
