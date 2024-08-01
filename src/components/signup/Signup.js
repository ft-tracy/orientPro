import React, { Component } from "react";
import { Link } from "react-router-dom";
import { withNavigate } from "../../hoc/withNavigate";
import {
  Container,
  Button,
  Row,
  Col,
  Form,
  FormControl,
  Modal,
} from "react-bootstrap";

import "../../styles/Module.css";
import { AuthContext } from "../../contexts/AuthContext";

class Signup extends Component {
  static contextType = AuthContext;

  constructor(props) {
    super(props);
    this.state = {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      confirmPassword: "",
      error: null,
      success: null,
    };
  }

  onChange = (e) => {
    this.setState({ [e.target.name]: e.target.value });
  };

  validateEmail(email) {
    // Regular expression for validating email address
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  onSignupClick = async () => {
    const { firstName, lastName, email, password, confirmPassword } =
      this.state;

    if (!firstName || !lastName || !email || !password || !confirmPassword) {
      this.setState({ error: "Please fill in all the fields to proceed!" });
      return;
    }

    if (!this.validateEmail(email)) {
      this.setState({
        error: "Invalid email format. ",
      });
      return;
    }

    if (password !== confirmPassword) {
      this.setState({ error: "Passwords do not match", success: null });
      return;
    }

    try {
      const { signup } = this.context;
      await signup({
        firstName,
        lastName,
        email,
        password,
        confirmPassword,
      });
      const redirectUrl = localStorage.getItem("redirectAfterLogin");
      localStorage.removeItem("redirectAfterLogin");
      if (redirectUrl) {
        this.props.navigate(redirectUrl);
      } else {
        this.setState({
          error: null,
          success: "Signup successful.",
        });
        this.props.navigate("/dashboard");
      }
    } catch (error) {
      this.setState({ error: error.message, success: null });
    }
  };

  onClick = () => {
    this.props.navigate("/");
  };

  onKeyDownHandler = (e) => {
    if (e.keyCode === 13) {
      this.onSignupClick();
    }
  };

  render() {
    return (
      <>
        <Modal show={true} onHide={this.onClick} centered>
          <Modal.Header closeButton>
            <Modal.Title>Sign Up</Modal.Title>
          </Modal.Header>
          <Modal.Body onKeyDown={this.onKeyDownHandler}>
            <Container>
              <Row className="justify-content-center">
                <Col md="6">
                  <Form>
                    <br />
                    <div className={"inputContainer"}>
                      <Form.Group controlId="firstNameId">
                        <Form.Label>First Name</Form.Label>
                        <Form.Control
                          type="text"
                          name="firstName"
                          placeholder="Enter first name"
                          value={this.state.firstName}
                          onChange={this.onChange}
                          className={"inputBox"}
                        />
                        <FormControl.Feedback type="invalid"></FormControl.Feedback>
                      </Form.Group>
                    </div>

                    <br />
                    <div className={"inputContainer"}>
                      <Form.Group controlId="lastNameId">
                        <Form.Label>Last Name</Form.Label>
                        <Form.Control
                          type="text"
                          name="lastName"
                          placeholder="Enter last name"
                          value={this.state.lastName}
                          onChange={this.onChange}
                          className={"inputBox"}
                        />
                        <FormControl.Feedback type="invalid"></FormControl.Feedback>
                      </Form.Group>
                    </div>

                    <br />
                    <div className={"inputContainer"}>
                      <Form.Group controlId="emailId">
                        <Form.Label>Email Address</Form.Label>
                        <Form.Control
                          type="email"
                          name="email"
                          placeholder="Enter email address"
                          value={this.state.email}
                          onChange={this.onChange}
                          className={"inputBox"}
                        />
                        <FormControl.Feedback type="invalid"></FormControl.Feedback>
                      </Form.Group>
                    </div>

                    <br />
                    <div className={"inputContainer"}>
                      <Form.Group controlId="passwordId">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                          type="password"
                          name="password"
                          placeholder="Enter password"
                          value={this.state.password}
                          onChange={this.onChange}
                          className={"inputBox"}
                        />
                        <FormControl.Feedback type="invalid"></FormControl.Feedback>
                      </Form.Group>
                    </div>

                    <br />
                    <div className={"inputContainer"}>
                      <Form.Group controlId="confirmPasswordId">
                        <Form.Label>Confirm Password</Form.Label>
                        <Form.Control
                          type="password"
                          name="confirmPassword"
                          placeholder="Confirm new password"
                          value={this.state.confirmPassword}
                          onChange={this.onChange}
                          className={"inputBox"}
                        />
                        <FormControl.Feedback type="invalid"></FormControl.Feedback>
                      </Form.Group>
                    </div>
                  </Form>

                  <br />
                  <div className={"inputContainer"}>
                    <Button
                      onClick={this.onSignupClick}
                      className={"inputButton"}
                    >
                      Sign Up
                    </Button>
                  </div>
                </Col>
                {this.state.error && (
                  <p className="center" style={{ color: "red" }}>
                    {this.state.error}
                  </p>
                )}
                {this.state.success && (
                  <p className="success">{this.state.success}</p>
                )}

                <br />
                <div className="center">
                  Already have an account? <Link to={"/login"}>Login</Link>
                </div>
              </Row>
            </Container>
          </Modal.Body>
        </Modal>
      </>
    );
  }
}

export default withNavigate(Signup);
