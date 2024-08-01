import React from "react";
import "../styles/Header.css";
import { Navbar, Nav, Container } from "react-bootstrap";
import OpLogo from "../assets/course-imges/oplogo.jpg";

const HomeHeader = () => {
  return (
    <div>
      <Navbar collapseOnSelect expand="lg" fixed="top" className={`navbar`}>
        <Container>
          <Navbar.Brand>
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
              <Nav.Link href="/login">Courses</Nav.Link>
              <div className="navbar-divider">|</div>
              <Nav.Link href="/login">Quizzes</Nav.Link>
              <div className="navbar-divider">|</div>
              <Nav.Link href="/login">Certificates</Nav.Link>
            </Nav>

            <Nav className="ml-3">
              <Nav.Link href="/login" style={{ color: "white" }}>
                Login
              </Nav.Link>
              <div className="navbar-divider">|</div>
              <Nav.Link href="/signup" style={{ color: "white" }}>
                Signup
              </Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </div>
  );
};

export default HomeHeader;
