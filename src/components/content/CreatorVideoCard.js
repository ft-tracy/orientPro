import React from "react";
import { Card } from "react-bootstrap";
import ReactTimeAgo from "react-time-ago";

const CreatorVideoCard = ({ content }) => {
  const { videoTitle, videoDescription, firstName, lastName, uploadedDate } =
    content;
  const uploadedDateObj = new Date(uploadedDate);
  const uploadedBy = firstName + lastName;
  const userInitials =
    firstName.charAt(0).toUpperCase() + lastName.charAt(0).toUpperCase();

  return (
    <Card className="creator-card">
      <Card.Body>
        <Card.Title>{videoTitle}</Card.Title>
        <div className="user-icon-container">
          <div className="user-icon">
            <span className="user-letter">{userInitials || "U"}</span>
          </div>
          <div className="review-content">
            <Card.Subtitle className="mb-2 text-muted">
              {uploadedBy || "Unknown User"}
            </Card.Subtitle>
          </div>
        </div>

        <Card.Text>
          <span>{uploadedDateObj.toLocaleDateString()}</span>
          <span className="time-passed">
            <ReactTimeAgo date={uploadedDateObj} />
          </span>
          <br />
          <br />
          <div style={{ fontWeight: "bold" }}>Description</div>
          {videoDescription}
        </Card.Text>
      </Card.Body>
    </Card>
  );
};

export default CreatorVideoCard;
