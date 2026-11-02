document.addEventListener("DOMContentLoaded", function () {

    console.log("LOGIN JS ĐÃ ĐƯỢC THỰC THI");

    // ==========================================
    // 1. LẤY CÁC PHẦN TỬ HTML
    // ==========================================

    const loginForm = document.getElementById("login-form");
    const loginButton = document.getElementById("btn-login");

    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    const errorBox = document.getElementById("login-error");
    const errorMessage = document.getElementById("login-error-message");

    const successBox = document.getElementById("login-success");
    const successMessage = document.getElementById("login-success-message");

    const toggleButton = document.getElementById("toggle-pwd-btn");
    const eyeIcon = document.getElementById("eye-icon");


    // ==========================================
    // 2. KIỂM TRA HTML
    // ==========================================

    if (
        !loginForm ||
        !loginButton ||
        !usernameInput ||
        !passwordInput ||
        !errorBox ||
        !errorMessage ||
        !successBox ||
        !successMessage
    ) {
        console.error("Không tìm thấy đầy đủ phần tử HTML đăng nhập.");
        return;
    }


    // ==========================================
    // 3. HIỆN / ẨN MẬT KHẨU
    // ==========================================

    if (toggleButton && eyeIcon) {

        toggleButton.addEventListener("click", function () {

            const isPassword = passwordInput.type === "password";

            passwordInput.type = isPassword ? "text" : "password";

            eyeIcon.classList.toggle("fa-eye", !isPassword);
            eyeIcon.classList.toggle("fa-eye-slash", isPassword);

        });

    }


    // ==========================================
    // 4. XỬ LÝ ĐĂNG NHẬP JWT
    // ==========================================

    loginForm.addEventListener("submit", async function (event) {

        // Ngăn form gửi request GET/POST theo cách HTML mặc định
        event.preventDefault();

        hideMessages();

        const username = usernameInput.value.trim();
        const password = passwordInput.value;

        const selectedRoleElement = document.querySelector(
            'input[name="role"]:checked'
        );

        // Kiểm tra vai trò
        if (!selectedRoleElement) {
            showError("Vui lòng chọn vai trò đăng nhập.");
            return;
        }

        const selectedRole = selectedRoleElement.value;

        // Kiểm tra dữ liệu
        if (!username || !password) {
            showError("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            return;
        }

        // Khóa nút đăng nhập
        loginButton.disabled = true;

        loginButton.innerHTML =
            '<i class="fas fa-spinner fa-spin"></i> Đang đăng nhập...';

        try {

            console.log("Đang gửi request POST /api/auth/login");

            // ==================================
            // GỌI API ĐĂNG NHẬP
            // ==================================

            const response = await fetch("/api/auth/login", {

                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },

                body: JSON.stringify({
                    username: username,
                    password: password
                })

            });

            console.log("HTTP Status:", response.status);

            // Đọc nội dung trả về
            const responseText = await response.text();

            let data;

            try {
                data = responseText ? JSON.parse(responseText) : {};
            } catch (parseError) {
                console.error("Response không phải JSON:", responseText);
                throw new Error("Dữ liệu trả về từ máy chủ không hợp lệ.");
            }


            // ==================================
            // KIỂM TRA KẾT QUẢ API
            // ==================================

            if (!response.ok) {

                throw new Error(
                    data.detail ||
                    data.message ||
                    data.error ||
                    "Tên đăng nhập hoặc mật khẩu không đúng."
                );

            }


            // Kiểm tra JWT
            if (!data.accessToken) {
                throw new Error("Server không trả về JWT Token.");
            }


            // Kiểm tra vai trò
            if (data.role !== selectedRole) {

                throw new Error(
                    "Tài khoản không thuộc nhóm vai trò bạn đã chọn."
                );

            }


            // Kiểm tra vai trò hợp lệ
            if (data.role !== "ADMIN" && data.role !== "STUDENT") {
                throw new Error("Vai trò tài khoản không hợp lệ.");
            }


            // ==================================
            // 5. LƯU THÔNG TIN ĐĂNG NHẬP
            // ==================================

            localStorage.setItem("accessToken", data.accessToken);

            localStorage.setItem(
                "tokenType",
                data.tokenType || "Bearer"
            );

            localStorage.setItem(
                "expiresIn",
                String(data.expiresIn || "")
            );

            localStorage.setItem(
                "userId",
                String(data.userId || "")
            );

            localStorage.setItem(
                "username",
                data.username || ""
            );

            localStorage.setItem(
                "fullName",
                data.fullName || ""
            );

            localStorage.setItem(
                "role",
                data.role
            );


            console.log("ĐĂNG NHẬP JWT THÀNH CÔNG");
            console.log("Username:", data.username);
            console.log("Role:", data.role);


            // ==================================
            // 6. HIỂN THỊ THÔNG BÁO
            // ==================================

            showSuccess("Đăng nhập thành công!");


            // ==================================
            // 7. CHUYỂN TRANG THEO VAI TRÒ
            // ==================================

            // ==========================================
// 7. CHUYỂN TRANG THEO VAI TRÒ
// ==========================================

            console.log("BƯỚC 1: Đăng nhập thành công");
            console.log("BƯỚC 2: Role =", data.role);
            console.log("BƯỚC 3: Token đã lưu =", !!localStorage.getItem("accessToken"));

            if (data.role === "ADMIN") {

                console.log("BƯỚC 4: Chuyển đến /admin/dashboard");

                window.location.assign("/admin/dashboard");

            } else if (data.role === "STUDENT") {

                console.log("BƯỚC 4: Chuyển đến /student/dashboard");

                window.location.assign("/student/dashboard");

            } else {

                console.error("Không xác định được vai trò:", data.role);

            }


        } catch (error) {

            console.error("Lỗi đăng nhập:", error);

            // Xóa thông tin đăng nhập cũ nếu thất bại
            localStorage.removeItem("accessToken");
            localStorage.removeItem("tokenType");
            localStorage.removeItem("expiresIn");
            localStorage.removeItem("userId");
            localStorage.removeItem("username");
            localStorage.removeItem("fullName");
            localStorage.removeItem("role");

            showError(error.message || "Đăng nhập thất bại.");

        } finally {

            loginButton.disabled = false;

            loginButton.innerHTML =
                '<i class="fas fa-sign-in-alt"></i> <span>Đăng nhập</span>';

        }

    });


    // ==========================================
    // 8. HIỂN THỊ LỖI
    // ==========================================

    function showError(message) {

        errorMessage.textContent = message;

        errorBox.style.display = "flex";

        successBox.style.display = "none";

    }


    // ==========================================
    // 9. HIỂN THỊ THÀNH CÔNG
    // ==========================================

    function showSuccess(message) {

        successMessage.textContent = message;

        successBox.style.display = "flex";

        errorBox.style.display = "none";

    }


    // ==========================================
    // 10. ẨN THÔNG BÁO
    // ==========================================

    function hideMessages() {

        errorBox.style.display = "none";

        successBox.style.display = "none";

    }

});