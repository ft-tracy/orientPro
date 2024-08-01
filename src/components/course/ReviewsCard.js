import { faStar } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { Card } from "react-bootstrap";

const ReviewsCard = ({ userInitial, name, date, text, stars }) => {
  const starRating = (rating) => {
    const totalStars = 5;
    return [...Array(totalStars)].map((_, index) => (
      <FontAwesomeIcon
        key={index}
        icon={faStar}
        style={{ color: index < rating ? "gold" : "gray" }}
      />
    ));
  };

  return (
    <Card className="mb-3">
      <Card.Body>
        <div className="user-icon-container">
          <div className="user-icon">
            <span className="user-letter">{userInitial}</span>
          </div>
          <div className="review-content">
            <Card.Title>{name}</Card.Title>
            <Card.Text>{date}</Card.Text>
          </div>
          <div className="star-rating">{starRating(stars)}</div>
        </div>
        <Card.Text className="review-text">{text}</Card.Text>
      </Card.Body>
    </Card>
  );
};

export default ReviewsCard;
