import { Component } from "react";
import AuthService from "../../services/AuthService";
import { withNavigate } from "../../hoc/withNavigate";
import { Container, Button, Row, Col, Form, Modal } from "react-bootstrap";
import "../../styles/Module.css";
import "../../styles/App.css";

class ForgotPassword extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      otp: "",
      emailError: "",
      otpError: "",
      success: null,
      showOtpInput: false,
    };
  }

  onChange = (e) => {
    this.setState({ [e.target.name]: e.target.value });
  };

  onSendOtpClick = async () => {
    const { email } = this.state;

    if (!email) {
      this.setState({ emailError: "Please enter your email address!" });
      return;
    }

    try {
      await AuthService.sendOTP(email);
      this.setState({
        emailError: null,
        success: "OTP has been sent to your email address",
        showOtpInput: true,
      });
    } catch (error) {
      this.setState({ emailError: error.message, success: null });
    }
  };

  onSubmitClick = async () => {
    const { email, otp } = this.state;

    if (!otp) {
      this.setState({ otpError: "Please enter the OTP!" });
    }

    try {
      await AuthService.verifyOTP(email, otp);
      this.setState({
        otpError: null,
        success: "OTP verified successfully. You can now reset your password",
      });
      this.props.navigate("/resetpassword");
    } catch (error) {
      this.setState({ otpError: error.message });
    }
  };

  onClick = () => {
    this.props.navigate("/");
  };

  //Enter key button functions
  onKeyDownHandler = (e) => {
    if (e.keyCode === 13) {
      if (e.target.name === "email") {
        this.onSendOtpClick();
      } else if (e.target.name === "otp") {
        this.onSubmitClick();
      }
    }
  };

  render() {
    /* eslint-disable-next-line no-unused-vars */
    const { email, otp, emailError, otpError, success, showOtpInput } =
      this.state;

    return (
      <>
        <Modal show={true} onHide={this.onClick} centered>
          <Modal.Header closeButton>
            <Modal.Title>Forgot Password</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Container>
              <p>Please enter your email address to recieve an OTP.</p>
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
                          onKeyDown={this.onKeyDownHandler}
                          className={"inputBox"}
                        />
                        {emailError && (
                          <div className="errorLabel" style={{ color: "red" }}>
                            {emailError}
                          </div>
                        )}
                      </Form.Group>
                    </div>

                    <br />
                    <div className={"inputContainer"}>
                      <Button
                        onClick={this.onSendOtpClick}
                        className={"inputButton"}
                      >
                        Send OTP
                      </Button>
                    </div>

                    {showOtpInput && (
                      <>
                        <br />
                        <div className={"inputContainer"}>
                          <Form.Group controlId="otpId">
                            <Form.Label>OTP</Form.Label>
                            <Form.Control
                              type="password"
                              name="otp"
                              placeholder="Enter OTP"
                              value={this.state.otp}
                              onChange={this.onChange}
                              onKeyDown={this.onKeyDownHandler}
                              className={"inputBox"}
                            />
                            {otpError && (
                              <div
                                className="errorLabel"
                                style={{ color: "red" }}
                              >
                                {otpError}
                              </div>
                            )}
                          </Form.Group>
                        </div>

                        <br />
                        <div className={"inputContainer"}>
                          <Button
                            onClick={this.onSubmitClick}
                            className={"inputButton"}
                          >
                            Reset Password
                          </Button>
                        </div>
                      </>
                    )}
                  </Form>

                  {success && <p className="success">{success}</p>}
                </Col>
              </Row>
            </Container>
          </Modal.Body>
        </Modal>
      </>
    );
  }
}

export default withNavigate(ForgotPassword);
