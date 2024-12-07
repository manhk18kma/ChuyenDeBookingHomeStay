// src/services/auth/tokenService.ts
import { jwtDecode } from 'jwt-decode';
import { DecodedToken } from '../../types/authTypes';

// Hàm giải mã token và trả về thông tin người dùng
export const decodeToken = (token: string): DecodedToken | null => {
    try {
        // Giải mã token và trả về kiểu DecodedToken
        const decoded = jwtDecode(token) as DecodedToken;
        return decoded;
    } catch (error) {
        console.error('Failed to decode token:', error);
        return null;
    }
};

// Hàm kiểm tra xem token có còn hiệu lực hay không
export const isTokenExpired = (token: string): boolean => {
    const decoded = decodeToken(token);
    if (!decoded) return true;

    const currentTime = Math.floor(Date.now() / 1000);
    return decoded.exp < currentTime;
};

export const getUserIdFromToken = (token: string): string | null => {
    const decoded = decodeToken(token);
    return decoded ? decoded.userId : null; // Trả về userId hoặc null nếu không giải mã được
};

export const hasRole = (token: string, role: string): boolean => {
    const decoded = decodeToken(token);
    if (!decoded) return false;

    return decoded.roles.includes(role);
};

export const hasAuthority = (token: string, authority: string): boolean => {
    const decoded = decodeToken(token);
    if (!decoded) return false;

    return decoded.authorities.includes(authority); // Kiểm tra xem authorities có chứa authority yêu cầu hay không
};
