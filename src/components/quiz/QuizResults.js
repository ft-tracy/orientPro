import React, { useContext, useState, useEffect } from "react";
import { Button, Container, Spinner } from "react-bootstrap";
import Footer from "../Footer";
import { AuthContext } from "../../contexts/AuthContext";
import Header from "../Header";
import HomeHeader from "../HomeHeader";
import ErrorPage from "../ErrorPage";
import trophy from "../../assets/course-imges/trophy.png";
import { Link, useLocation, useParams, useNavigate } from "react-router-dom";
import QuizScoreCard from "./QuizScoreCard";
import UserServices from "../../services/UserServices";
import { format } from "date-fns";

const QuizResults = () => {
  const { isAuthenticated, user } = useContext(AuthContext);
  const location = useLocation();
  const navigate = useNavigate();
  const { correctCount, scorePercentage, totalQuestions } = location.state;
  const [isLoading, setIsLoading] = useState(true);
  const { courseid, moduleid, quizid, nextContentId } = useParams();
  const [isLastModule, setIsLastModule] = useState(false);
  const [courseName, setCourseName] = useState("");
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCourseAndModuleDetails = async () => {
      try {
        const courseDetails = await UserServices.getCourseDetails(courseid);
        setCourseName(courseDetails ? courseDetails.courseTitle : "");

        const modules = await UserServices.getModules(courseid);
        const currentModuleIndex = modules.findIndex(
          (module) => module.moduleId === moduleid
        );
        console.log("modules:", modules);
        console.log("currentModuleIndex:", currentModuleIndex);
        console.log("modules.length:", modules.length);
        setIsLastModule(currentModuleIndex === modules.length - 1);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching course and module details:", error);
        setError(error);
        setIsLoading(false);
      }
    };

    fetchCourseAndModuleDetails();
  }, [courseid, moduleid]);

  const onNextClick = () => {
    navigate(
      `/course/${courseid}/module/${
        parseInt(moduleid) + 1
      }/content/${nextContentId}`
    );
  };

  const displayText = () => {
    if (isLastModule) {
      return (
        <div className="center" style={{ fontSize: "20px" }}>
          <p>
            Want to leave a review?{" "}
            <Link to={`/course/${courseid}/review`}>Click here</Link>
          </p>
          <p>
            Don't want to leave a review?{" "}
            <Link to={`/course/${courseid}/certificate`}>Click here</Link>
          </p>
        </div>
      );
    } else {
      return (
        <Button
          className="custom-button"
          style={{ float: "right" }}
          onClick={onNextClick}
        >
          Next
        </Button>
      );
    }
  };

  const userName = user.firstName + user.lastName;
  const formattedDate = format(new Date(), "dd/MM/yyyy");

  if (isLoading) {
    return (
      <>
        {isAuthenticated ? <Header /> : <HomeHeader />}
        <Container className="text-center">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
        </Container>
      </>
    );
  }

  if (error) {
    return <ErrorPage error={error} />;
  }

  return (
    <>
      {isAuthenticated ? <Header /> : <HomeHeader />}
      <Container>
        <div className="split-container">
          <div className="quiz-details">
            <div className="home-image">
              <img
                src={trophy}
                alt="Trophy"
                width="200px"
                height="200px"
                style={{ margin: "20px" }}
              />
            </div>

            <div className="quiz-user">
              <p>{userName || "Unknown User"}</p>
              <p>{courseName}</p>
              <p>{formattedDate}</p>
            </div>

            <div className="center" style={{ backgroundColor: "white" }}>
              <Link
                to={`/course/${courseid}/module/${moduleid}/quiz/${quizid}`}
              >
                Re-attempt quiz
              </Link>
            </div>
          </div>
          <div className="proceed-details">
            <div>
              <QuizScoreCard
                correctCount={correctCount}
                scorePercentage={scorePercentage}
                totalQuestions={totalQuestions}
              />
            </div>
            <br /> <br />
            {/* Text appears if it is the last module of the course and it is the last content of the module = courseProgress === 100 */}
            {displayText()}
          </div>
        </div>
      </Container>
      <Footer />
    </>
  );
};

export default QuizResults;
