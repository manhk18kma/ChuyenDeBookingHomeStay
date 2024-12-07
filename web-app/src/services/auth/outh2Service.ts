import { OAuthConfig } from '../../configurations/configuration';
import { ExchangeTokenRequest, LoginResponse } from '../../types/authTypes';
import { ResponseData } from '../../types/commonTypes';
import { decodeToken } from './tokenService';

export const handleLoginWithGoogle = async () => {
    const callbackUrl = OAuthConfig.redirectUri;
    const authUrl = OAuthConfig.authUri;
    const googleClientId = OAuthConfig.clientId;

    const targetUrl = `${authUrl}?redirect_uri=${encodeURIComponent(
        callbackUrl
    )}&response_type=code&client_id=${googleClientId}&scope=openid%20email%20profile`;
    window.location.href = targetUrl;
};

const EXCHANGE_TOKEN_URL = `${process.env.REACT_APP_API_BASE_URL}/api/v1/auth/outbound/authentication?code=`;

export const handleExchangeToken = async (
    request: ExchangeTokenRequest
): Promise<ResponseData<LoginResponse>> => {
    try {
        const response = await fetch(EXCHANGE_TOKEN_URL + request.code, {
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
            console.log('handleExchangeToken : Decoded Token:', decodedToken);
        } else {
            console.error('Invalid data format:', data);
            throw new Error('Invalid data received from server');
        }

        return data;
    } catch (error) {
        console.error('Error during exchange token:', error);
        // Trả về thông báo lỗi chi tiết
        throw new Error(error instanceof Error ? error.message : 'An error occurred');
    }
};
