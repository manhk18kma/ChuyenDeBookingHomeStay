import React, { useState, useEffect } from 'react';
import { Button, Typography, Snackbar, CircularProgress } from '@mui/material';
import styles from '../../styles/auth/InitChangePassword.module.css';
import { InitChangePasswordRequest } from '../../types/authTypes';
import {
    ErrorResponse,
    ErrorResponseMultipleField,
    ResponseData,
} from '../../types/commonTypes';
import { handleInitChangePassword } from '../../services/auth/authenticationService';
import { validateEmailOrUsername } from '../../validators/validators';

const InitChangePassword: React.FC = () => {
    const [email, setEmail] = useState<string>('');
    const [emailError, setEmailError] = useState<string | null>(null);
    const [message, setMessage] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(false); // Thêm state cho việc xử lý
    const [countdown, setCountdown] = useState<number | null>(null);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [buttonMess, setButtonMess] = useState<string>('Gửi Yêu Cầu Khôi Phục');

    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setEmail(e.target.value);
        setEmailError(null);
    };

    const handleEmailBlur = () => {
        const error = validateEmailOrUsername(email);
        setEmailError(error);
    };

    const handleEmailInput = () => {
        setEmailError(null);
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const requestData: InitChangePasswordRequest = { email };
        setLoading(true); // Bắt đầu xử lý
        setButtonMess('Gửi Lại Yêu Cầu Khôi Phục');
        try {
            const response: ResponseData<any> = await handleInitChangePassword(
                requestData
            );
            setMessage(response.message);
            setCountdown(180); // Bắt đầu đếm ngược 3 phút
            setSnackbarOpen(true); // Mở Snackbar
            console.log('Init change password ', response);
        } catch (error) {
            if (error instanceof Error) {
                setEmailError(null);
                try {
                    const errorResponse: ErrorResponse | ErrorResponseMultipleField =
                        JSON.parse(error.message);
                    console.log(errorResponse);
                    if (errorResponse.type === 'SINGLE') {
                        const singleError = errorResponse as ErrorResponse;
                        if (singleError.status === 2 || singleError.status === 8) {
                            setEmailError(singleError.message);
                        } else {
                            setMessage(singleError.message);
                        }
                    } else if (errorResponse.type === 'MULTIPLE') {
                        Object.entries(errorResponse.message).forEach(([key, value]) => {
                            if (key === 'email') {
                                setEmailError(value);
                            }
                        });
                    }
                } catch {
                    setMessage('Đã có lỗi xảy ra.');
                }
            } else {
                setMessage('Đã có lỗi xảy ra.');
            }
        } finally {
            setLoading(false); // Kết thúc xử lý
        }
    };

    // Hàm để bắt đầu đếm ngược
    useEffect(() => {
        let timer: NodeJS.Timeout | undefined;
        if (countdown !== null && countdown > 0) {
            timer = setInterval(() => {
                setCountdown((prev) => (prev ? prev - 1 : null));
            }, 1000);
        } else if (countdown === 0) {
            setCountdown(null);
        }
        return () => clearInterval(timer);
    }, [countdown]);

    const handleSnackbarClose = () => {
        setSnackbarOpen(false);
    };

    return (
        <div className={styles.container}>
            <h2>Quên Mật Khẩu</h2>
            <form onSubmit={handleSubmit} className={styles.form}>
                <label htmlFor="email">Email:</label>
                <input
                    id="email"
                    type="email"
                    placeholder="Nhập email của bạn"
                    value={email}
                    onChange={handleEmailChange}
                    onBlur={handleEmailBlur}
                    onInput={handleEmailInput}
                    onFocus={handleEmailInput}
                    className={`${styles.input} ${emailError ? styles.inputError : ''}`}
                    required
                />
                {emailError && <p className={styles.error}>{emailError}</p>}
                {/* Chỉ hiển thị một trong hai phần tử này */}
                <div className={styles.buttonContainer}>
                    {loading ? (
                        <CircularProgress />
                    ) : (
                        <button
                            type="submit"
                            className={styles.submitButton}
                            disabled={loading}
                        >
                            {buttonMess}
                        </button>
                    )}
                </div>
            </form>
            {message && !loading && <p className={styles.message}>{message}</p>}
            {countdown !== null && !loading && (
                <Typography className={styles.typography}>
                    Chỉ còn {Math.floor(countdown / 60)}:
                    {String(countdown % 60).padStart(2, '0')} trước khi yêu cầu hết hạn
                </Typography>
            )}
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={handleSnackbarClose}
                message="Yêu cầu đã được gửi! Chờ đợi thời gian..."
            />
        </div>
    );
};

export default InitChangePassword;
