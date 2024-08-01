import React, { useContext, useEffect } from "react";
import { Button, Card } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import { AuthContext } from "../../contexts/AuthContext";
import UserServices from "../../services/UserServices";

const CoursesCard = ({
  image,
  title,
  date,
  description,
  modules,
  courseid,
  userid,
  allContent,
  tags,
}) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { isAuthenticated } = useContext(AuthContext);

  useEffect(() => {
    console.log("allContent:", allContent);
    console.log("Modules:", modules);

    if (!Array.isArray(allContent) || !Array.isArray(modules)) {
      console.error("allContent or modules is not defined or not an array");
      return;
    }
  }, [modules, allContent]);

  const onButtonClick = async () => {
    if (isAuthenticated) {
      try {
        const updatedCourse = await UserServices.enrollCourse(courseid, userid);

        console.log("Enrolled in course:", updatedCourse);
      } catch (error) {
        console.error("Error enrolling in course:", error);
      }

      try {
        const startedCourse = await UserServices.startCourse(courseid, userid);
        console.log("Started course:", startedCourse);
      } catch (error) {
        console.error("Error starting course:", error);
      }

      const module = modules.length > 0 ? modules[0] : null;
      const content = allContent.length > 0 ? allContent[0] : null;

      console.log("Content:", content);

      const moduleid = module ? module.moduleId : null;
      const contentid = content
        ? content.videoId || content.readingId || content.quizId
        : null;

      console.log("Module id:", moduleid);
      console.log("Content id:", contentid);

      if (moduleid && contentid) {
        navigate(`/course/${courseid}/module/${moduleid}/content/${contentid}`);
      } else {
        console.error("Module ID or Content ID not found");
      }
    } else {
      localStorage.setItem("redirectAfterLogin", location.pathname);
      navigate("/login");
    }
  };

  return (
    <Card className="course-card">
      <Card.Img variant="top" src={image} className="course-image" />

      <Card.Body className="course-card-body">
        <Card.Title>{title}</Card.Title>
        <Card.Text>{date}</Card.Text>
        <Card.Text>{description}</Card.Text>
        <Card.Text>
          <b>Course Outline</b>
        </Card.Text>
        <Card.Text>
          {modules.length > 0 ? (
            modules.map((module, index) => (
              <div key={index}>
                <div>{module.title}</div>
              </div>
            ))
          ) : (
            <div>No modules available</div>
          )}
        </Card.Text>

        <Card.Text>
          <b>Course Tags</b>
        </Card.Text>
        <Card.Text>
          {tags.length > 0 ? (
            tags.map((tag, index) => (
              <div key={index}>
                <div>{tag}</div>
              </div>
            ))
          ) : (
            <div>No tags available</div>
          )}
        </Card.Text>

        <Button className="custom-button" onClick={onButtonClick}>
          Start
        </Button>
      </Card.Body>
    </Card>
  );
};

export default CoursesCard;
