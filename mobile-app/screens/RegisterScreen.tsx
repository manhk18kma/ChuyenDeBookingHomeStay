// src/screens/RegisterScreen.tsx
import React, { useState } from "react";
import { View, Text, TextInput, Button, Alert } from "react-native";
import { NavigationProps, RouteProps } from "../navigation/RootStackParamList"; // Import RouteProps để dùng type
import { useNavigation } from "@react-navigation/native";

type Props = {
  route: RouteProps<"Register">;
};

const RegisterScreen: React.FC<Props> = ({ route }) => {
  const [email, setEmail] = useState(route.params?.email || ""); // Sử dụng email từ params nếu có
  const [password, setPassword] = useState("");
  const navigation = useNavigation<NavigationProps<"Register">>();

  const handleRegister = () => {
    // Xử lý logic đăng ký
    Alert.alert("Thông báo", "Đăng ký thành công");
  };

  return (
    <View style={{ padding: 16 }}>
      <Text>Email:</Text>
      <TextInput
        style={{ borderWidth: 1, padding: 8, marginVertical: 10 }}
        value={email}
        onChangeText={setEmail}
        keyboardType="email-address"
      />
      <Text>Password:</Text>
      <TextInput
        style={{ borderWidth: 1, padding: 8, marginVertical: 10 }}
        value={password}
        onChangeText={setPassword}
        secureTextEntry
      />
      <Button title="Đăng ký" onPress={handleRegister} />
      <Button
        title="Đăng nhập"
        onPress={() => navigation.navigate("Login", { email: email })}
      />
    </View>
  );
};

export default RegisterScreen;
