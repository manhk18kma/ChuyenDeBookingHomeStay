import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { Box, CircularProgress, Typography } from '@mui/material';
import { ExchangeTokenRequest, LoginResponse } from '../../types/authTypes';
import { handleExchangeToken } from '../../services/auth/outh2Service';
import { ResponseData } from '../../types/commonTypes';

const GoogleLogin = () => {
    const navigate = useNavigate();

    useEffect(() => {
        console.log(window.location.href);
        const authCodeRegex = /code=([^&]+)/;

        const authenticateUser = async () => {
            const authCodeRegex = /code=([^&]+)/;
            const isMatch = window.location.href.match(authCodeRegex);
            if (isMatch) {
                const authCode = isMatch[1];
                const request: ExchangeTokenRequest = {
                    code: authCode,
                };
                try {
                    const response: ResponseData<LoginResponse> =
                        await handleExchangeToken(request);
                    console.log('Đăng nhập thành công:', response);
                    navigate('/home');
                } catch (error) {
                    console.error('Lỗi khi đăng nhập:', error);
                }
            }
        };

        authenticateUser();
    }, [navigate]);

    return (
        <>
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '30px',
                    justifyContent: 'center',
                    alignItems: 'center',
                    height: '100vh',
                }}
            >
                <CircularProgress></CircularProgress>
                <Typography>Authenticating...</Typography>
            </Box>
        </>
    );
};

export default GoogleLogin;
