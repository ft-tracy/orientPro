import React, { useState, useEffect, useContext } from "react";
import { Modal, Button, Form, Spinner, Alert } from "react-bootstrap";
import UserServices from "../../services/UserServices";
import { useNavigate } from "react-router-dom";
import { ThemeContext } from "../../contexts/ThemeContext";

const Settings = () => {
  const navigate = useNavigate();
  const { darkMode, toggleDarkMode } = useContext(ThemeContext);
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserData = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await UserServices.getUserData();
        setUserData(response.data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, []);

  const handleClose = () => {
    navigate("/dashboard");
  };

  const handleResetPassword = () => {
    navigate("/resetpassword");
  };

  if (loading) {
    return (
      <Modal show={true} onHide={handleClose} centered>
        <Modal.Header closeButton>
          <Modal.Title>Settings</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="d-flex justify-content-center">
            <Spinner animation="border" />
          </div>
        </Modal.Body>
      </Modal>
    );
  }

  if (error) {
    return (
      <Modal show={true} onHide={handleClose} centered>
        <Modal.Header closeButton>
          <Modal.Title>Settings</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Alert variant="danger">Error: {error}</Alert>
        </Modal.Body>
      </Modal>
    );
  }

  return (
    <Modal show={true} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Settings</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div>
          <div className="titleContainer">Profile</div>
          <div className="settings-container">
            <strong>First Name:</strong> <span>{userData?.firstName}</span>
          </div>
          <div className="settings-container">
            <strong>Last Name:</strong> <span>{userData?.lastName}</span>
          </div>
          <div className="settings-container">
            <strong>Email:</strong> <span>{userData?.email}</span>
          </div>
          <div className="settings-container">
            <strong>Role:</strong> <span>{userData?.role}</span>
          </div>
        </div>

        <hr />
        <div style={{ marginBottom: "20px" }}>
          <Button
            style={{
              width: "100%",
              textAlign: "center",
              backgroundColor: "white",
              color: "black",
              border: "none",
            }}
            onClick={handleResetPassword}
          >
            Reset Password
          </Button>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button className="custom-button" onClick={handleClose}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default Settings;
