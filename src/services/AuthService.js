import axios from "axios";

const API_URL = "https://orientproservice-1.onrender.com/api";

class AuthService {
  async signup(firstName, lastName, email, password, confirmPassword) {
    const payload = {
      firstName,
      lastName,
      email,
      password,
      confirmPassword,
    };

    console.log("Signup Payload:", payload);

    try {
      const response = await axios.post(`${API_URL}/account/signup`, payload);
      localStorage.setItem("token", response.data.token);
      return response.data;
    } catch (error) {
      console.error(
        "Signup API Error:",
        error.response ? error.response.data : error.message
      );
      throw new Error(
        error.response ? error.response.data.message : error.message
      );
    }
  }

  async login(email, password) {
    try {
      const response = await axios.post(`${API_URL}/account/login`, {
        email,
        password,
      });
      console.log("Login Response:", response.data);
      localStorage.setItem("token", response.data.token);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response ? error.response.data.message : error.message
      );
    }
  }

  async resetPassword(email, newPassword, confirmPassword) {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        `${API_URL}/account/resetpassword`,
        {
          email,
          newPassword,
          confirmPassword,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      return response.data;
    } catch (error) {
      throw new Error(
        error.response ? error.response.data.message : error.message
      );
    }
  }

  //Forgot password
  async sendOTP(email) {
    try {
      const response = await axios.post(`${API_URL}/account/SendOTP`, {
        email,
      });
      return response.data;
    } catch (error) {
      throw new Error(
        error.response ? error.resonse.data.message : error.message
      );
    }
  }

  //Forgot password
  async verifyOTP(email, otp) {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        `${API_URL}/account/VerifyOTP`,
        {
          email,
          otp,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      return response.data;
    } catch (error) {
      throw new Error(
        error.response ? error.response.data.message : error.message
      );
    }
  }

  logout() {
    localStorage.removeItem("token");
    console.log("Logout successful");
  }
}

/* eslint-disable-next-line import/no-anonymous-default-export */
export default new AuthService();
