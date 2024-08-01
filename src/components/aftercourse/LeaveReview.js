import React, { useContext, useEffect, useState } from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar } from "@fortawesome/free-solid-svg-icons";
import { useNavigate, useParams } from "react-router-dom";
import "../../styles/Review.css";
import { AuthContext } from "../../contexts/AuthContext";
import UserServices from "../../services/UserServices";

const LeaveReview = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  const { courseid, moduleid, quizid } = useParams();
  const [rating, setRating] = useState(0);
  const [hover, setHover] = useState(0);
  const [reviewContent, setReviewContent] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [courseTitle, setCourseTitle] = useState("");

  const userInitials =
    user.firstName.charAt(0).toUpperCase() +
    user.lastName.charAt(0).toUpperCase();

  useEffect(() => {
    const fetchCourseDetails = async () => {
      try {
        const courseDetails = await UserServices.getCourseDetails(courseid);
        setCourseTitle(courseDetails ? courseDetails.courseTitle : "");

        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching course details:", error);
        setIsLoading(false);
      }
    };

    fetchCourseDetails();
  }, [courseid]);

  const handleSubmit = async () => {
    const review = {
      rating: rating,
      reviewContent: reviewContent,
    };

    try {
      const userid = user?.documentId || "";
      await UserServices.addReview(userid, courseid, review);
      console.log("Review submitted successfully");

      setIsLoading(false);
    } catch (error) {
      console.error("Error submitting review:", error);
      setIsLoading(false);
    }

    navigate(`/course/${courseid}/certificate`);
  };

  const handleClose = () => {
    navigate(
      `/course/${courseid}/module/${moduleid}/quiz/${quizid}/quizresults`
    );
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <Modal show={true} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Review</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="titleContainer">Leave a Review</div>
        <div className="review-container">
          <div className="user-icon-container">
            <div className="user-icon">
              <div className="user-letter">{userInitials || "U"}</div>
            </div>
            <h3 className="review-content">{courseTitle}</h3>
          </div>
          <div className="review-content">
            <p style={{ fontSize: "18px" }}>Rate the overall course</p>

            {/* Star rating */}
            <div className="stars">
              {[...Array(5)].map((star, index) => {
                const ratingValue = index + 1;
                return (
                  <label key={index}>
                    <input
                      type="radio"
                      name="rating"
                      value={ratingValue}
                      onClick={() => setRating(ratingValue)}
                    />
                    <FontAwesomeIcon
                      icon={faStar}
                      className="star"
                      color={
                        ratingValue <= (hover || rating) ? "#ffc107" : "#e4e5e9"
                      }
                      size="2x"
                      onMouseEnter={() => setHover(ratingValue)}
                      onMouseLeave={() => setHover(rating)}
                    />
                  </label>
                );
              })}
            </div>

            {/* Text Area */}
            <Form.Group controlId="reviewContent">
              <Form.Control
                as="textarea"
                rows={3}
                placeholder="Write a review"
                value={reviewContent}
                onChange={(e) => setReviewContent(e.target.value)}
              />
            </Form.Group>
          </div>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button className="custom-button" onClick={handleClose}>
          Close
        </Button>
        <Button className="custom-button" onClick={handleSubmit}>
          Submit
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default LeaveReview;
