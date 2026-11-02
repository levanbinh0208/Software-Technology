/**
 * Login Page Scripts
 * Hệ thống Quản lý Nội trú
 */

(function () {
    'use strict';

    // ===== 1. Toggle hiện/ẩn mật khẩu =====
    const togglePwdBtn = document.getElementById('toggle-pwd-btn');
    const passwordInput = document.getElementById('password');
    const eyeIcon = document.getElementById('eye-icon');

    if (togglePwdBtn && passwordInput && eyeIcon) {
        togglePwdBtn.addEventListener('click', function () {
            const isPassword = passwordInput.type === 'password';
            passwordInput.type = isPassword ? 'text' : 'password';
            eyeIcon.classList.toggle('fa-eye', !isPassword);
            eyeIcon.classList.toggle('fa-eye-slash', isPassword);
        });
    }

    // ===== 2. Tự động focus vào ô tên đăng nhập =====
    window.addEventListener('DOMContentLoaded', function () {
        const usernameInput = document.getElementById('username');
        if (usernameInput) {
            usernameInput.focus();
        }
    });

    // ===== 3. Validate form trước khi submit =====
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', function (e) {
            const username = document.getElementById('username').value.trim();
            const password = passwordInput ? passwordInput.value : '';

            if (!username || !password) {
                e.preventDefault();
                showToast('Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.', 'error');
                return;
            }

            // Hiệu ứng loading khi submit
            const submitBtn = loginForm.querySelector('.btn-login');
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
            }
        });
    }

    // ===== 4. Helper: Hiển thị toast (nếu fragment toast chưa xử lý) =====
    function showToast(message, type = 'info') {
        // Kiểm tra nếu đã có hàm showToast global từ fragment
        if (typeof window.showToast === 'function') {
            window.showToast(message, type);
            return;
        }
        // Fallback: dùng alert
        alert(message);
    }

    // ===== 5. Hiệu ứng hover cho role selector =====
    const roleOptions = document.querySelectorAll('.role-option input[type="radio"]');
    roleOptions.forEach(radio => {
        radio.addEventListener('change', function () {
            // Có thể thêm logic thay đổi placeholder theo role
            const usernameInput = document.getElementById('username');
            if (usernameInput) {
                usernameInput.placeholder = this.value === 'HocSinh'
                    ? 'Nhập mã học sinh'
                    : 'Nhập tên đăng nhập';
            }
        });
    });

})();