// src/types/authTypes.ts

export interface LoginRequest {
    usernameOrEmail: string;
    password: string;
}

export interface LoginResponse {
    accessToken: string;
    refreshToken: string;
}

export interface RefreshTokenRequest {
    refreshToken: string;
}

export interface InitChangePasswordRequest {
    email: string;
}

export interface ChangePasswordRequest {
    token: string;
    password: string;
    confirmPassword: any;
}

export interface DecodedToken {
    exp: number;
    iat: number;
    sub: string;
    scope: string[];
    roles: string[];
    authorities: string[];
    userId: string;
}

export interface ExchangeTokenRequest {
    code: string;
}
