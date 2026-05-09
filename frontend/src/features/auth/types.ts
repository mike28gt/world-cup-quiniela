export type RegisterRequest = {
  displayName: string;
  email: string;
  password: string;
  confirmPassword: string;
};

export type AuthResponse = {
  userId: string;
  accessToken: string;
};