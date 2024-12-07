// App.tsx
import React from "react";
import { AuthProvider } from "./context/AuthContext";
import RootNavigator from "./navigation/navigation";

export default function App() {
  return (
    <AuthProvider>
      <RootNavigator />
    </AuthProvider>
  );
}
