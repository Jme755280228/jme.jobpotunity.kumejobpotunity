document.addEventListener('DOMContentLoaded', () => {
    const mobileToggleBtn = document.getElementById('mobile-toggle-btn');
    const mainNav = document.getElementById('main-nav-menu');

    if (mobileToggleBtn && mainNav) {
        mobileToggleBtn.addEventListener('click', () => {
            mainNav.classList.toggle('active');
        });
    }
});
