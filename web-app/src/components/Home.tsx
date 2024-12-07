import { useEffect, useState, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { getToken } from '../services/localStorageService';
import Header from './header/Header';
import {
  Alert,
  Box,
  Button,
  Card,
  CircularProgress,
  Snackbar,
  TextField,
  Typography,
} from '@mui/material';
import { SnackbarCloseReason } from '@mui/material/Snackbar'; // Import SnackbarCloseReason type

interface UserDetails {
  username: string;
  firstName: string;
  lastName: string;
  dob: string;
  roles?: { name: string }[];
  noPassword?: boolean;
}

export default function Home() {
  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState<UserDetails | null>(null);
  const [password, setPassword] = useState<string>('');
  const [snackBarOpen, setSnackBarOpen] = useState<boolean>(false);
  const [snackBarMessage, setSnackBarMessage] = useState<string>('');
  const [snackType, setSnackType] = useState<'error' | 'success'>('error');

  // Update the handleCloseSnackBar function to match Snackbar onClose expected types
  const handleCloseSnackBar = (
    event: Event | React.SyntheticEvent<any> | undefined,
    reason?: SnackbarCloseReason
  ) => {
    // Prevent closing when clicking away
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

  const getUserDetails = async (accessToken: string | null) => {
    const response = await fetch(
      'http://localhost:8080/identity/users/my-info',
      {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );

    const data = await response.json();
    setUserDetails(data.result);
  };

  const addPassword = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const body = {
      password: password,
    };

    fetch('http://localhost:8080/identity/users/create-password', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${getToken()}`,
      },
      body: JSON.stringify(body),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.code !== 1000) throw new Error(data.message);
        getUserDetails(getToken());
        showSuccess(data.message);
      })
      .catch((error) => {
        showError(error.message);
      });
  };

  useEffect(() => {
    const accessToken = getToken();

    if (!accessToken) {
      navigate('/login');
      return;
    }

    getUserDetails(accessToken);
  }, [navigate]);

  return (
    <>
      <Header />
      <Snackbar
        open={snackBarOpen}
        onClose={handleCloseSnackBar} // This should now work correctly
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
      {userDetails ? (
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
              minWidth: 400,
              maxWidth: 500,
              boxShadow: 4,
              borderRadius: 2,
              padding: 4,
            }}
          >
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                width: '100%', // Ensure content takes full width
              }}
            >
              <p>Welcome back to Devteria, {userDetails.username}</p>
              <h1 className="name">{`${userDetails.firstName} ${userDetails.lastName}`}</h1>
              <p className="email">{userDetails.dob}</p>
              <ul>
                User's roles:
                {userDetails.roles?.map((item, index) => (
                  <li className="email" key={index}>
                    {item.name}
                  </li>
                ))}
              </ul>
              {userDetails.noPassword && (
                <Box
                  component="form"
                  onSubmit={addPassword}
                  sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '10px',
                    width: '100%',
                  }}
                >
                  <Typography>Do you want to create a password?</Typography>
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
                  >
                    Create password
                  </Button>
                </Box>
              )}
            </Box>
          </Card>
        </Box>
      ) : (
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
          <CircularProgress />
          <Typography>Loading ...</Typography>
        </Box>
      )}
    </>
  );
}
