import { faStar } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { Card, ProgressBar } from "react-bootstrap";

const RatingsCard = ({ reviews = [] }) => {
  const calculateAverageRating = (reviews) => {
    if (!Array.isArray(reviews)) return 0;
    const totalReviews = reviews.length;
    const totalStars = reviews.reduce((acc, review) => acc + review.rating, 0);
    return totalReviews > 0 ? (totalStars / totalReviews).toFixed(1) : 0;
  };

  const calculateStarDistribution = (reviews) => {
    if (!Array.isArray(reviews)) return [0, 0, 0, 0, 0];
    const distribution = [0, 0, 0, 0, 0];
    reviews.forEach((review) => {
      if (review.rating > 0 && review.rating <= 5)
        distribution[review.rating - 1]++; //Sets the numbers to be 1-5
    });
    return distribution;
  };

  const averageRating = calculateAverageRating(reviews);
  const starDistribution = calculateStarDistribution(reviews);
  const totalReviews = reviews.length;

  return (
    <Card className="mb-3">
      <Card.Body>
        <h5>Average Rating</h5>
        <div className="average-ratings">
          <FontAwesomeIcon icon={faStar} className="average-star" />
          <span className="average-number">{averageRating}</span>
        </div>

        {starDistribution
          .slice()
          .reverse()
          .map((count, index) => (
            <div key={index} className="star-distribution">
              <span style={{ marginLeft: "5px" }}>{5 - index}</span>{" "}
              {/*Displays the numbers beside the progress bar */}
              <ProgressBar
                now={(count / totalReviews) * 100} //Determines how much of the progress bar the count takes
                className="ratings-bar"
                variant="warning"
              />
              <span style={{ marginLeft: "10px" }}>
                {((count / totalReviews) * 100).toFixed(1)}%
              </span>
            </div>
          ))}
      </Card.Body>
    </Card>
  );
};

export default RatingsCard;
