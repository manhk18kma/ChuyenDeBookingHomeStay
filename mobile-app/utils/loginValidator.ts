// src/validators/loginValidator.ts

export interface LoginFormValues {
  email: string;
  password: string;
}

export const validateLoginForm = (values: LoginFormValues) => {
  const errors: { email?: string; password?: string } = {};

  // Kiểm tra email
  if (!values.email) {
    errors.email = "Email là bắt buộc.";
  } else if (!/\S+@\S+\.\S+/.test(values.email)) {
    errors.email = "Email không hợp lệ.";
  }

  // Kiểm tra mật khẩu
  if (!values.password) {
    errors.password = "Mật khẩu là bắt buộc.";
  } else if (values.password.length < 6) {
    errors.password = "Mật khẩu phải có ít nhất 6 ký tự.";
  }

  return errors;
};
