import React from "react";
import { Button, Card, ProgressBar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import "../../styles/Course.css";
import UserServices from "../../services/UserServices";

const CourseCard = ({
  image,
  title,
  description,
  isEnrolled,
  hasStarted,
  progress,
  courseid,
  userid,
}) => {
  const navigate = useNavigate();

  const onButtonClick = () => {
    navigate(`/course/${courseid}`);
  };

  const onResumeClick = async () => {
    try {
      const response = await UserServices.getLastAccessedContent(userid);
      console.log("Response from getLastAccessedContent:", response);

      const lastAccessedContent = response.lastAccessedContent;

      console.log("lastAccessedContent:", lastAccessedContent);
      console.log("courseId:", lastAccessedContent.courseId);
      console.log("moduleId:", lastAccessedContent.moduleId);
      console.log("contentId:", lastAccessedContent.contentId);

      navigate(
        `/course/${lastAccessedContent.courseId}/module/${lastAccessedContent.moduleId}/content/${lastAccessedContent.contentId}`
      );
    } catch (error) {
      console.error("Error getting last accessed content:", error);
    }
  };

  const renderButton = () => {
    if (isEnrolled) {
      if (hasStarted) {
        if (progress === 100) {
          return (
            <Button className="custom-button" onClick={onButtonClick}>
              Completed
            </Button>
          );
        }
        return (
          <Button className="custom-button" onClick={onResumeClick}>
            Resume
          </Button>
        );
      }
      return (
        <Button className="custom-button" onClick={onButtonClick}>
          Start
        </Button>
      );
    }
    return (
      <Button className="custom-button" onClick={onButtonClick}>
        Join
      </Button>
    );
  };

  return (
    <Card className="dashboard-card">
      <Card.Img variant="top" src={image} />
      <Card.Body className="course-card-body">
        <Card.Title>{title}</Card.Title>
        <Card.Text>{description}</Card.Text>
        {renderButton()}
        {isEnrolled && hasStarted && (
          <ProgressBar
            now={progress}
            label={`${progress}%`}
            className="mt-2"
            style={{ color: "#001628" }}
          />
        )}
      </Card.Body>
    </Card>
  );
};

export default CourseCard;
