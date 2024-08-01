import React, { createContext, useContext, useEffect, useState } from "react";
import UserServices from "../services/UserServices";
import AuthService from "../services/AuthService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      UserServices.getUserData()
        .then((response) => {
          setUser(response.data);
          setIsAuthenticated(true);
          console.log("Fetched user data:", response.data);
        })
        .catch((error) => {
          console.error("Error fetching user data:", error);
        });
    } else {
      setLoading(false);
    }
  }, []);

  const login = async (email, password) => {
    try {
      const response = await AuthService.login(email, password);
      console.log("Login Response:", response);
      localStorage.setItem("token", response.token);
      const userDataResponse = await UserServices.getUserData();
      console.log("User Data Response:", userDataResponse);
      setUser(userDataResponse.data);
      setIsAuthenticated(true);
      return response;
    } catch (error) {
      console.error("Login error:", error);
      throw error;
    }
  };

  const signup = async (details) => {
    try {
      const response = await AuthService.signup(
        details.firstName,
        details.lastName,
        details.email,
        details.password,
        details.confirmPassword
      );
      console.log("Signup Response:", response);
      localStorage.setItem("token", response.token);
      const userDataResponse = await UserServices.getUserData();
      console.log("User Data Response:", userDataResponse);
      setUser(userDataResponse.data);
      setIsAuthenticated(true);
      return response;
    } catch (error) {
      console.error("Signup error:", error);
      throw error;
    }
  };

  const logout = () => {
    AuthService.logout();
    setIsAuthenticated(false);
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{ user, isAuthenticated, login, signup, logout, loading }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};

export { AuthContext };
