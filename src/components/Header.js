import React, { useContext, useEffect, useState } from "react";
import "../styles/Header.css";
import { Navbar, Nav, Container, Dropdown } from "react-bootstrap";
import OpLogo from "../assets/course-imges/oplogo.jpg";
import { AuthContext } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";

const colors = ["green", "purple", "blue", "orange", "pink"];

const Header = () => {
  const navigate = useNavigate();
  const { user, isAuthenticated, logout } = useContext(AuthContext);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!isAuthenticated) {
      setError("User is not authenticated");
    } else {
      setError(null);
    }
  }, [isAuthenticated]);

  if (error) {
    return <p>{error}</p>;
  }

  if (!user) {
    return <p>User not found</p>;
  }

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const color = colors[0];
  const userInitials =
    user.firstName.charAt(0).toUpperCase() +
    user.lastName.charAt(0).toUpperCase();

  return (
    <div>
      <Navbar collapseOnSelect expand="lg" fixed="top" className={`navbar`}>
        <Container>
          <Navbar.Brand href="/dashboard">
            <img
              src={OpLogo}
              width="50"
              height="50"
              className="d-inline-block align-top"
              alt="OrientPro logo"
            />
          </Navbar.Brand>

          <Navbar.Toggle aria-controls="responsive-navbar-nav" />
          <Navbar.Collapse id="responsive-navbar-nav">
            <Nav className="mx-auto">
              <Nav.Item>
                <Dropdown alignRight="true">
                  <Dropdown.Toggle id="courses-dropdown">
                    <span>Courses</span>
                  </Dropdown.Toggle>

                  <Dropdown.Menu>
                    <Dropdown.Item href="/availablecourses">
                      Available courses
                    </Dropdown.Item>
                    <Dropdown.Item href="/enrolledcourses">
                      Enrolled courses
                    </Dropdown.Item>
                    <Dropdown.Item href="/completedcourses">
                      Completed courses
                    </Dropdown.Item>
                  </Dropdown.Menu>
                </Dropdown>
              </Nav.Item>
              <div className="navbar-divider">|</div>
              <Nav.Link href="/quizzes">Quizzes</Nav.Link>
              <div className="navbar-divider">|</div>
              <Nav.Link href="/certificates">Certificates</Nav.Link>
            </Nav>

            <Nav className="ml-3">
              <Nav.Item>
                <Dropdown alignRight="true">
                  <Dropdown.Toggle
                    variant="success"
                    id="dropdown-basic"
                    className="user-icon"
                    style={{ backgroundColor: color }}
                  >
                    <span className="user-letter">{userInitials}</span>
                  </Dropdown.Toggle>

                  <Dropdown.Menu>
                    <Dropdown.Item href="/settings">Settings</Dropdown.Item>
                    <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
                  </Dropdown.Menu>
                </Dropdown>
              </Nav.Item>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </div>
  );
};

export default Header;
