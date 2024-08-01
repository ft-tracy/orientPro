import React from "react";
import { Button, Card } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const HomeCard = ({ image, title, courseid }) => {
  const navigate = useNavigate();

  const onButtonClick = () => {
    navigate(`/course/${courseid}`);
  };

  return (
    <Card className="home-card">
      <Card.Img
        variant="top"
        src={image}
        alt="Course image"
        className="home-card-image"
      />
      <Card.Body>
        <Card.Title>{title}</Card.Title>
        <Button className="custom-button" onClick={onButtonClick}>
          View Course
        </Button>
      </Card.Body>
    </Card>
  );
};

export default HomeCard;
