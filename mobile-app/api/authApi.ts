// src/api/authApi.ts
import { LoginData, AuthResponse } from "../types/authTypes";

const API_URL = "https://example.com/api"; // Thay bằng URL thật của bạn

export const loginApi = async (data: LoginData): Promise<AuthResponse> => {
  const response = await fetch(`${API_URL}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error("Đăng nhập thất bại");
  }

  return response.json();
};
