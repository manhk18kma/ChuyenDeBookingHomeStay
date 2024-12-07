// src/navigation/RootStackParamList.ts
import { RouteProp } from "@react-navigation/native";
import { StackNavigationProp } from "@react-navigation/stack";

export type RootStackParamList = {
  //   Login: undefined;
  Login: {
    email: string;
  };
  Register: {
    email: string;
  };
  Test: {
    name: number;
    age: string;
  };
};

// Types cho các props của navigation và route
export type NavigationProps<T extends keyof RootStackParamList> =
  StackNavigationProp<RootStackParamList, T>;

export type RouteProps<T extends keyof RootStackParamList> = RouteProp<
  RootStackParamList,
  T
>;
