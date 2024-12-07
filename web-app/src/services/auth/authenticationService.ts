import { removeToken } from '../localStorageService';
import {
    ChangePasswordRequest,
    InitChangePasswordRequest,
    LoginRequest,
    LoginResponse,
    RefreshTokenRequest,
} from '../../types/authTypes';
import { ResponseData } from '../../types/commonTypes';
import { decodeToken, isTokenExpired } from './tokenService';

const LOGIN_URL = `${process.env.REACT_APP_API_BASE_URL}/api/v1/auth/login`;
const REFRESH_TOKEN_URL = `${process.env.REACT_APP_API_BASE_URL}/api/v1/auth/tokens-refresh`;
const INIT_CHANGE_PASSWORD_URL = `${process.env.REACT_APP_API_BASE_URL}/api/v1/auth/init-change-password`;
const CHANGE_PASSWORD_URL = `${process.env.REACT_APP_API_BASE_URL}/api/v1/auth/change-password`;

const AUTH_TOKEN_KEY = 'accessToken';
const REFRESH_TOKEN_KEY = 'refreshToken';
export const logOut = () => {
    removeToken();
};

export const handleLogin = async (
    request: LoginRequest
): Promise<ResponseData<LoginResponse>> => {
    try {
        const response = await fetch(LOGIN_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(JSON.stringify(errorData));
        }

        const data: ResponseData<LoginResponse> = await response.json();

        // if (data.status !== 0) {
        //     throw new Error(data.message || 'Đăng nhập không thành công');
        // }

        if (data.data) {
            localStorage.setItem('accessToken', data.data.accessToken);
            localStorage.setItem('refreshToken', data.data.refreshToken);

            const decodedToken = decodeToken(data.data.accessToken);
            console.log('handleLogin : Decoded Token:', decodedToken);
        } else {
            throw new Error('Dữ liệu không hợp lệ');
        }

        return data;
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : 'Đã có lỗi xảy ra');
    }
};

export const handleRefreshToken = async (
    request: RefreshTokenRequest
): Promise<ResponseData<LoginResponse>> => {
    try {
        const response = await fetch(REFRESH_TOKEN_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request),
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Refresh Token Error:', errorData);
            throw new Error(errorData.message || 'Failed to refresh token');
        }

        const data: ResponseData<LoginResponse> = await response.json();

        if (data.data) {
            localStorage.setItem('accessToken', data.data.accessToken);
            localStorage.setItem('refreshToken', data.data.refreshToken);

            const decodedToken = decodeToken(data.data.accessToken);
            console.log('handleRefreshToken : Decoded Token:', decodedToken);
        } else {
            console.error('Invalid data format:', data);
            throw new Error('Invalid data received from server');
        }

        return data;
    } catch (error) {
        console.error('Error during refresh token:', error);
        throw new Error(error instanceof Error ? error.message : 'An error occurred');
    }
};

export const handelIsAuthenticated = async (): Promise<boolean> => {
    const accessToken = localStorage.getItem(AUTH_TOKEN_KEY) || '';
    const refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY) || '';

    if (!accessToken && !refreshToken) {
        return false;
    } else if (accessToken && isTokenExpired(accessToken) && refreshToken) {
        try {
            await handleRefreshToken({ refreshToken });
            return true;
        } catch (error) {
            return false;
        }
    } else if (isTokenExpired(accessToken) && isTokenExpired(refreshToken)) {
        return false;
    }

    return true;
};

export const handleInitChangePassword = async (
    request: InitChangePasswordRequest
): Promise<ResponseData<any>> => {
    try {
        const response = await fetch(INIT_CHANGE_PASSWORD_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request),
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(JSON.stringify(errorData));
        }
        const data: ResponseData<any> = await response.json();
        return data;
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : 'Đã có lỗi xảy ra');
    }
};

export const handleChangePassword = async (
    request: ChangePasswordRequest
): Promise<ResponseData<any>> => {
    try {
        const response = await fetch(CHANGE_PASSWORD_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request),
        });
        if (!response.ok) {
            const errorData = await response.json();
            console.log('handleChangePassword', errorData);
            throw new Error(JSON.stringify(errorData));
        }
        const data: ResponseData<any> = await response.json();
        return data;
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : 'Đã có lỗi xảy ra');
    }
};
