import {
  Alert,
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Divider,
  Snackbar,
  TextField,
  Typography,
  CircularProgress,
} from '@mui/material';

import GoogleIcon from '@mui/icons-material/Google';
import { OAuthConfig } from '../configurations/configuration';
import { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { getToken, setToken } from '../services/localStorageService';

const Login1: React.FC = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [snackBarOpen, setSnackBarOpen] = useState<boolean>(false);
  const [snackBarMessage, setSnackBarMessage] = useState<string>('');
  const [snackType, setSnackType] = useState<'success' | 'error'>('error');
  const [loading, setLoading] = useState<boolean>(false);

  const handleCloseSnackBar = (
    event?: React.SyntheticEvent | Event,
    reason?: string
  ) => {
    if (reason === 'clickaway') {
      return;
    }
    setSnackBarOpen(false);
  };

  const showError = (message: string) => {
    setSnackType('error');
    setSnackBarMessage(message);
    setSnackBarOpen(true);
  };

  const showSuccess = (message: string) => {
    setSnackType('success');
    setSnackBarMessage(message);
    setSnackBarOpen(true);
  };

  const handleContinueWithGoogle = () => {
    const callbackUrl = OAuthConfig.redirectUri;
    const authUrl = OAuthConfig.authUri;
    const googleClientId = OAuthConfig.clientId;

    const targetUrl = `${authUrl}?redirect_uri=${encodeURIComponent(
      callbackUrl
    )}&response_type=code&client_id=${googleClientId}&scope=openid%20email%20profile`;

    window.location.href = targetUrl;
  };

  const handleLogin = useCallback(
    async (event: React.FormEvent<HTMLFormElement>) => {
      event.preventDefault();
      setLoading(true); // Start loading

      const data = {
        username: username,
        password: password,
      };

      try {
        const response = await fetch(
          `http://localhost:8080/identity/auth/token`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
          }
        );

        const result = await response.json();
        if (result.code !== 1000) throw new Error(result.message);

        setToken(result.result?.token);
        navigate('/');
      } catch (error) {
        showError((error as Error).message);
      } finally {
        setLoading(false); // Stop loading
      }
    },
    [username, password, navigate]
  );

  useEffect(() => {
    const accessToken = getToken();
    if (accessToken) {
      navigate('/');
    }
  }, [navigate]);

  return (
    <>
      <Snackbar
        open={snackBarOpen}
        onClose={handleCloseSnackBar}
        autoHideDuration={6000}
        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
      >
        <Alert
          onClose={handleCloseSnackBar}
          severity={snackType}
          variant="filled"
          sx={{ width: '100%' }}
        >
          {snackBarMessage}
        </Alert>
      </Snackbar>
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        height="100vh"
        bgcolor={'#f0f2f5'}
      >
        <Card
          sx={{
            minWidth: 250,
            maxWidth: 400,
            boxShadow: 4,
            borderRadius: 4,
            padding: 4,
          }}
        >
          <CardContent>
            <Typography variant="h5" component="h1" gutterBottom>
              Welcome to Devtetia
            </Typography>
            <Box component="form" onSubmit={handleLogin} sx={{ mt: 2 }}>
              <TextField
                label="Username"
                variant="outlined"
                fullWidth
                margin="normal"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              <TextField
                label="Password"
                type="password"
                variant="outlined"
                fullWidth
                margin="normal"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <Button
                type="submit"
                variant="contained"
                color="primary"
                size="large"
                fullWidth
                disabled={loading} // Disable button when loading
                sx={{ mt: 2 }} // Optional margin on top
              >
                {loading ? <CircularProgress size={24} /> : 'Login'}
              </Button>
            </Box>
          </CardContent>
          <CardActions>
            <Box display="flex" flexDirection="column" width="100%" gap="25px">
              <Button
                type="button"
                variant="contained"
                color="secondary"
                size="large"
                onClick={handleContinueWithGoogle}
                fullWidth
                sx={{ gap: '10px' }}
              >
                <GoogleIcon />
                Continue with Google
              </Button>
              <Divider />
              <Button variant="contained" color="success" size="large">
                Create an account
              </Button>
            </Box>
          </CardActions>
        </Card>
      </Box>
    </>
  );
};

export default Login1;
