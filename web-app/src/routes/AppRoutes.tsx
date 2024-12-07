import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import ForgotPassword from '../pages/auth/InitChangePassword';
import Login from '../pages/auth/Login';
import ChangePassword from '../pages/auth/ChangePassword';
import Test from '../pages/auth/Test';
import InitChangePassword from '../pages/auth/InitChangePassword';
import UpdateHomestayImage from '../test/UpdateHomestayImage';
import MapComponent from '../test/MapComponent';
import GoogleLogin from '../pages/auth/GoogleLogin';

const AppRoutes: React.FC = () => {
    return (
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/outbound-auth" element={<GoogleLogin />} />

            <Route path="/init-change-password" element={<InitChangePassword />} />
            <Route path="/change-password" element={<ChangePassword />} />
            <Route path="/home" element={<Test />} />

            <Route path="/test" element={<MapComponent />} />

            {/* Đặt trang mặc định */}
            {/* <Route path="*" element={<Navigate to="/home" />} /> */}
        </Routes>
    );
};

export default AppRoutes;
