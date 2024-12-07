import React, { useState } from 'react';
import styles from '../../styles/auth/Login.module.css';
import LogoKMA from '../../assets/img/LogoKMA.jpg';
import logoGoogle from '../../assets/img/LogoGoogle.jpeg';
import { Link, useNavigate } from 'react-router-dom';
import { LoginRequest, LoginResponse } from '../../types/authTypes';
import {
    ErrorResponse,
    ErrorResponseMultipleField,
    ResponseData,
} from '../../types/commonTypes';
import { handleLogin } from '../../services/auth/authenticationService';
import { validateEmailOrUsername, validatePassword } from '../../validators/validators'; // Import validators
import { handleLoginWithGoogle } from '../../services/auth/outh2Service';

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | any>(null);
    const [emailError, setEmailError] = useState<string | null>(null);
    const [passwordError, setPasswordError] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setEmail(e.target.value);
        setEmailError(null);
    };

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value);
        setPasswordError(null);
    };

    const handleEmailBlur = () => {
        const error = validateEmailOrUsername(email);
        setEmailError(error);
    };

    const handlePasswordBlur = () => {
        const error = validatePassword(password);
        setPasswordError(error);
    };

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        setEmailError(null);
        setPasswordError(null);

        const emailValidationError = validateEmailOrUsername(email);
        const passwordValidationError = validatePassword(password);

        if (emailValidationError || passwordValidationError) {
            setEmailError(emailValidationError);
            setPasswordError(passwordValidationError);
            return;
        }

        const requestData: LoginRequest = { usernameOrEmail: email, password };

        try {
            const response: ResponseData<LoginResponse> = await handleLogin(requestData);
            console.log('Đăng nhập thành công:', response);
            navigate('/home');
        } catch (error) {
            if (error instanceof Error) {
                setEmailError(null);
                setPasswordError(null);
                try {
                    const errorResponse: ErrorResponse | ErrorResponseMultipleField =
                        JSON.parse(error.message);
                    console.log(errorResponse);
                    if (errorResponse.type === 'SINGLE') {
                        const singleError = errorResponse as ErrorResponse;
                        if (singleError.status === 2 || singleError.status === 8) {
                            setEmailError(singleError.message);
                        } else {
                            setError(singleError.message);
                        }
                    } else if (errorResponse.type === 'MULTIPLE') {
                        Object.entries(errorResponse.message).forEach(([key, value]) => {
                            if (key === 'usernameOrEmail') {
                                setEmailError(value);
                            } else if (key === 'password') {
                                setPasswordError(value);
                            }
                        });
                    }
                } catch {
                    setError('Đã có lỗi xảy ra.');
                }
            } else {
                setError('Đã có lỗi xảy ra.');
            }
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.logo}>
                <img src={LogoKMA} alt="Logo KMA" />
            </div>
            <h2>Đăng Nhập</h2>
            <form onSubmit={handleSubmit}>
                <label>Email:</label>
                <input
                    type="text"
                    placeholder="Nhập email của bạn"
                    value={email}
                    onChange={handleEmailChange}
                    onBlur={handleEmailBlur}
                    required
                />
                {emailError && <p className={styles.error}>{emailError}</p>}

                <label>Mật khẩu:</label>
                <input
                    type="password"
                    placeholder="Nhập mật khẩu của bạn"
                    value={password}
                    onChange={handlePasswordChange}
                    onBlur={handlePasswordBlur}
                    required
                />
                {passwordError && <p className={styles.error}>{passwordError}</p>}

                {error && <p className={styles.error}>{error}</p>}

                <button type="submit">Đăng Nhập</button>
                <button
                    className={styles.googleButton}
                    type="button"
                    onClick={handleLoginWithGoogle}
                >
                    <img
                        src={logoGoogle}
                        alt="Google Logo"
                        style={{ width: '20px', marginRight: '10px' }}
                    />
                    Đăng Nhập bằng Google
                </button>
            </form>
            <div className={styles.forgotPassword}>
                <Link to="/forgot-password">Quên mật khẩu?</Link>
            </div>
        </div>
    );
};

export default Login;
