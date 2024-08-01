import React from "react";
import { Container } from "react-bootstrap";

const ErrorPage = ({ error }) => {
  return (
    <Container>
      <div
        style={{
          textAlign: "center",
          padding: "20px",
        }}
      >
        <div style={{ paddingTop: "100.000%", position: "relative" }}>
          <iframe
            title="Error GIF"
            src="https://gifer.com/embed/uGP"
            width="100%"
            height="100%"
            style={{ position: "absolute", top: 0, left: 0 }}
            frameBorder="0"
            allowFullScreen
          ></iframe>
        </div>
        <p>
          <a href="https://gifer.com"></a>
        </p>
        <div style={{ backgroundColor: "white" }}>
          <h1>Oh No!</h1>
          <h4>{error.message}</h4>
          <p>Kindly check your internet connection then refresh the page</p>
        </div>
      </div>
    </Container>
  );
};

export default ErrorPage;
