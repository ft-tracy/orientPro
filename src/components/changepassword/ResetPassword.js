import { Component } from "react";
import AuthService from "../../services/AuthService";
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
import "../../styles/App.css";

class ResetPassword extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      newPassword: "",
      confirmPassword: "",
      error: null,
      success: null,
    };
  }

  onChange = (e) => {
    this.setState({ [e.target.name]: e.target.value });
  };

  onResetPasswordClick = async () => {
    const { email, newPassword, confirmPassword } = this.state;

    if (!email || !newPassword || !confirmPassword) {
      this.setState({ error: "Please fill in all the fields to proceed!" });
      return;
    }

    if (newPassword !== confirmPassword) {
      this.setState({ error: "Passwords do not match", success: null });
      return;
    }

    try {
      await AuthService.resetPassword(email, newPassword, confirmPassword);
      this.setState({
        error: null,
        success:
          "Password reset successful. You can now log in with your new password.",
      });
      this.props.navigate("/login");
    } catch (error) {
      this.setState({ error: error.message });
    }
  };

  onClick = () => {
    this.props.navigate("/");
  };

  onKeyDownHandler = (e) => {
    if (e.keyCode === 13) {
      this.onResetPasswordClick();
    }
  };

  render() {
    return (
      <>
        <Modal show={true} onHide={this.onClick} centered>
          <Modal.Header closeButton>
            <Modal.Title>Reset Password</Modal.Title>
          </Modal.Header>
          <Modal.Body onKeyDown={this.onKeyDownHandler}>
            <Container>
              <Row className="justify-content-center">
                <Col md="6">
                  <Form>
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
                      </Form.Group>
                    </div>

                    <br />
                    <div className={"inputContainer"}>
                      <Form.Group controlId="newPasswordId">
                        <Form.Label>New Password</Form.Label>
                        <Form.Control
                          type="password"
                          name="newPassword"
                          placeholder="Enter new password"
                          value={this.state.newPassword}
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

                    <br />
                    <div className={"inputContainer"}>
                      <Button
                        onClick={this.onResetPasswordClick}
                        className={"inputButton"}
                      >
                        Update
                      </Button>
                    </div>
                  </Form>
                </Col>
                {this.state.error && (
                  <p className="center" style={{ color: "red" }}>
                    {this.state.error}
                  </p>
                )}
                {this.state.success && (
                  <p className="success">{this.state.success}</p>
                )}
              </Row>
            </Container>
          </Modal.Body>
        </Modal>
      </>
    );
  }
}

export default withNavigate(ResetPassword);
