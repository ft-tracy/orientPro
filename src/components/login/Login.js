import React, { Component } from "react";
import { Link } from "react-router-dom";
import {
  Container,
  Row,
  Col,
  Button,
  Form,
  FormControl,
  Modal,
} from "react-bootstrap";
import "../../styles/Module.css";

import { withNavigate } from "../../hoc/withNavigate";
import { AuthContext } from "../../contexts/AuthContext";

class Login extends Component {
  static contextType = AuthContext;

  constructor(props) {
    super(props);
    this.state = { email: "", password: "", error: null };
  }

  onChange = (e) => {
    this.setState({ [e.target.name]: e.target.value });
  };

  validateEmail(email) {
    // Regular expression for validating email address
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  onLoginClick = async () => {
    const { email, password } = this.state;

    if (!email || !password) {
      this.setState({ error: "Please fill in all the fields to proceed!" });
      return;
    }

    if (!this.validateEmail(email)) {
      this.setState({
        error: "Invalid email format. ",
      });
      return;
    }

    try {
      const { login } = this.context;
      const response = await login(email, password);
      const redirectUrl = localStorage.getItem("redirectAfterLogin");
      localStorage.removeItem("redirectAfterLogin");
      if (response.isFirstLogin) {
        this.props.navigate("/resetpassword", { email });
      } else if (redirectUrl) {
        this.props.navigate(redirectUrl);
      } else {
        this.props.navigate("/dashboard");
      }
    } catch (error) {
      this.setState({ error: error.message });
    }
  };

  onClick = () => {
    this.props.navigate("/");
  };

  onKeyDownHandler = (e) => {
    if (e.keyCode === 13) {
      this.onLoginClick();
    }
  };

  render() {
    return (
      <Modal show={true} onHide={this.onClick} centered>
        <Modal.Header closeButton>
          <Modal.Title>Login</Modal.Title>
        </Modal.Header>
        <Modal.Body onKeyDown={this.onKeyDownHandler}>
          <Container>
            <Row className="justify-content-center">
              <Col md="4">
                <Form>
                  <br />
                  <div className={"inputContainer"}>
                    <Form.Group controlId="emailId">
                      <Form.Label>Email Address</Form.Label>
                      <Form.Control
                        type="email"
                        name="email"
                        placeholder="Enter email adddress"
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
                </Form>
                <div style={{ float: "right" }}>
                  <Link to={"/forgotpassword"}>Forgot Password?</Link>
                </div>

                <br />
                <br />
                <div className={"inputContainer"}>
                  <Button onClick={this.onLoginClick} className={"inputButton"}>
                    Login
                  </Button>
                </div>
              </Col>
              {this.state.error && (
                <p className="center" style={{ color: "red" }}>
                  {this.state.error}
                </p>
              )}

              <br />
              <div className="center">
                Don't have an account? <Link to={"/signup"}>Sign Up</Link>
              </div>
            </Row>
          </Container>
        </Modal.Body>
      </Modal>
    );
  }
}

export default withNavigate(Login);
