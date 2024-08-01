import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import Header from "../Header";
import HomeHeader from "../HomeHeader";
import Footer from "../Footer";
import Sidebar from "../Sidebar";
import ErrorPage from "../ErrorPage";
import { Button, Col, Container, Row, Spinner } from "react-bootstrap";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import "../../styles/Quiz.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTh } from "@fortawesome/free-solid-svg-icons";
import useCourseDetails from "../../hooks/useCourseDetails";
import UserServices from "../../services/UserServices";

const Quiz = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { isAuthenticated, user } = useContext(AuthContext);
  const { courseid, moduleid, quizid, contentid } = useParams();
  const [showSidebar, setShowSidebar] = useState(false);
  const { courseDetails, modules, allContent, isLoading, error } =
    useCourseDetails(courseid);
  const [quiz, setQuiz] = useState(location.state?.quiz || null);
  const [moduleName, setModuleName] = useState("");

  useEffect(() => {
    const savedQuizState = localStorage.getItem("quizState");
    if (savedQuizState) {
      const parsedQuiz = JSON.parse(savedQuizState);
      console.log("parsedQuiz:", parsedQuiz);

      if (parsedQuiz && parsedQuiz.quiz) {
        setQuiz(parsedQuiz);
      }
    } else if (location.state?.quiz) {
      setQuiz(location.state.quiz);
    }

    const currentModule = modules.find(
      (module) => module.moduleId === moduleid
    );
    if (currentModule) {
      setModuleName(currentModule.title);
    }
  }, [modules, moduleid, location.state?.quiz]);

  const onStartClick = () => {
    console.log("Navigating with quiz data:", quiz);
    navigate(
      `/course/${courseid}/module/${moduleid}/quiz/${quizid}/quizquestion`,
      { state: { quiz } }
    );
  };

  const onBackClick = async () => {
    try {
      const response = await UserServices.getLastAccessedContent(
        user?.documentId
      );
      console.log("Response from getLastAccessedContent:", response);

      const lastAccessedContent = response.lastAccessedContent;

      navigate(
        `/course/${lastAccessedContent.courseId}/module/${lastAccessedContent.moduleId}/content/${lastAccessedContent.contentId}`
      );
    } catch (error) {
      console.error("Error getting last accessed content:", error);
    }
  };

  const toggleSidebar = () => {
    setShowSidebar(!showSidebar);
  };

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
      <div>{isAuthenticated ? <Header /> : <HomeHeader />}</div>
      <Container>
        <Row>
          <Col md={1}>
            <FontAwesomeIcon
              icon={faTh}
              onClick={toggleSidebar}
              className="sidebar-toggle-icon"
            />
          </Col>
          <Sidebar
            showSidebar={showSidebar}
            course={courseDetails}
            modules={modules}
            allContent={allContent}
            moduleid={moduleid}
          />
          <Col
            md={showSidebar ? 9 : 11}
            className={`main-content ${showSidebar ? "content-shift" : ""}`}
          >
            <div className="titleContainer">{`${moduleName} Quiz`}</div>
            <br />
            <div>
              <div className="quiz-instructions">
                Read the following instructions carefully before attempting the
                quiz.
              </div>
              <br />
              <div className="quiz-list">
                <ol>
                  <li>
                    Make sure you have completed all the content before
                    attempting the quiz.
                  </li>
                  <li>
                    You will be able to navigate from one question to another
                    using the question bar at the bottom of the page.
                  </li>
                  <li>
                    Once the quiz is submitted, you will not be able to change
                    any answer given.
                  </li>
                  <li>
                    The quiz passmark is 80%. If you fail the quiz, you will
                    only have one more attempt.
                  </li>
                </ol>
              </div>
            </div>
            <div className="button-container">
              <Button
                className="custom-button"
                style={{ marginLeft: "40px" }}
                onClick={onBackClick}
              >
                Back
              </Button>

              <Button
                className="custom-button"
                onClick={onStartClick}
                style={{ float: "right" }}
              >
                Start Quiz
              </Button>
            </div>
          </Col>
        </Row>
      </Container>
      <Footer />
    </>
  );
};

export default Quiz;
