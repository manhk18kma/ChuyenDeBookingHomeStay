import React, { useState } from 'react';
import styles from '../../styles/auth/ChangePassword.module.css';
import { useLocation, useNavigate } from 'react-router-dom';
import { CircularProgress, Snackbar } from '@mui/material';

const ChangePassword: React.FC = () => {
    const location = useLocation();
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const token = new URLSearchParams(location.search).get('token');
    const [loading, setLoading] = useState<boolean>(false);
    const [snackbarOpen, setSnackbarOpen] = useState(false);

    const navigate = useNavigate();

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setLoading(true);
        setSnackbarOpen(true);
        navigate('/home');

        // const requestData: ChangePasswordRequest = {
        //     password: password,
        //     confirmPassword: confirmPassword,
        //     token: token || '',
        // };
        // try {
        //     const response: ResponseData<any> = await handleChangePassword(requestData);
        //     console.log('Change password ', response);
        // } catch (error) {
        //     // if (error instanceof Error) {
        //     //     setEmailError(null);
        //     //     try {
        //     //         const errorResponse: ErrorResponse | ErrorResponseMultipleField =
        //     //             JSON.parse(error.message);
        //     //         console.log(errorResponse);
        //     //         if (errorResponse.type === 'SINGLE') {
        //     //             const singleError = errorResponse as ErrorResponse;
        //     //             if (singleError.status === 2 || singleError.status === 8) {
        //     //                 setEmailError(singleError.message);
        //     //             } else {
        //     //                 setMessage(singleError.message);
        //     //             }
        //     //         } else if (errorResponse.type === 'MULTIPLE') {
        //     //             Object.entries(errorResponse.message).forEach(([key, value]) => {
        //     //                 if (key === 'email') {
        //     //                     setEmailError(value);
        //     //                 }
        //     //             });
        //     //         }
        //     //     } catch {
        //     //         setMessage('Đã có lỗi xảy ra.');
        //     //     }
        //     // } else {
        //     //     setMessage('Đã có lỗi xảy ra.');
        //     // }
        // } finally {
        //     setLoading(false); // Kết thúc xử lý
        // }
    };

    return (
        <div className={styles.container}>
            <h2>Thay Đổi Mật Khẩu</h2>
            <form onSubmit={handleSubmit}>
                <label>Mật khẩu mới:</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Nhập mật khẩu mới"
                    required
                />

                <label>Xác nhận mật khẩu:</label>
                <input
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    placeholder="Xác nhận mật khẩu mới"
                    required
                />
                <div className={styles.buttonContainer}>
                    {loading ? (
                        <CircularProgress />
                    ) : (
                        <button
                            type="submit"
                            className={styles.submitButton}
                            disabled={loading}
                        >
                            Thay Đổi Mật Khẩu{' '}
                        </button>
                    )}
                </div>
            </form>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={() => setSnackbarOpen(false)}
                message="Thay đổi mật khẩu thành công"
            />
        </div>
    );
};

export default ChangePassword;
