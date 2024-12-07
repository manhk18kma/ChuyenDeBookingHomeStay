export const validateEmailOrUsername = (emailOrUsername: string): string | null => {
    if (!emailOrUsername) {
        return 'Vui lòng nhập email hoặc tên đăng nhập.';
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const usernameRegex = /^[a-zA-Z0-9_]{3,}$/;

    if (!emailRegex.test(emailOrUsername) && !usernameRegex.test(emailOrUsername)) {
        return 'Email hoặc tên đăng nhập không hợp lệ.';
    }
    return null;
};

export const validatePassword = (password: string): string | null => {
    if (!password) {
        return 'Vui lòng nhập mật khẩu.';
    }
    if (password.length < 6 || !/\d/.test(password)) {
        return 'Mật khẩu phải có ít nhất 6 ký tự và chứa ít nhất 1 chữ số.';
    }
    return null;
};
