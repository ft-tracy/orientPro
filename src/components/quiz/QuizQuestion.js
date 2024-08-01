import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import Header from "../Header";
import HomeHeader from "../HomeHeader";
import Footer from "../Footer";
import ErrorPage from "../ErrorPage";
import { Container, Row, Col, Button, Form, Spinner } from "react-bootstrap";
import "../../styles/Quiz.css";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import Sidebar from "../Sidebar";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTh } from "@fortawesome/free-solid-svg-icons";
import UserServices from "../../services/UserServices";
import useCourseDetails from "../../hooks/useCourseDetails";

const QuizQuestion = () => {
  const { isAuthenticated, user } = useContext(AuthContext);
  const [selectedAnswer, setSelectedAnswer] = useState(null);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState([]);
  const navigate = useNavigate();
  const location = useLocation();
  const [quiz, setQuiz] = useState(location.state?.quiz || null);
  console.log("Quiz state:", quiz);
  const { courseid, moduleid, quizid } = useParams();
  const [showSidebar, setShowSidebar] = useState(false);
  const { courseDetails, modules, allContent, error } =
    useCourseDetails(courseid); // eslint-disable-line no-unused-vars

  useEffect(() => {
    if (location.state?.quiz) {
      setQuiz(location.state.quiz);
    } else {
      const savedQuizState = localStorage.getItem("quizState");
      if (savedQuizState) {
        const parsedQuiz = JSON.parse(savedQuizState);
        console.log("parsedQuiz:", parsedQuiz);

        setQuiz(parsedQuiz);
      }
    }
  }, [location.state?.quiz]);

  useEffect(() => {
    try {
      const userid = user?.documentId || "";
      const contentid = quizid;
      if (quiz) {
        UserServices.getProgress(userid, contentid)
          .then((response) => {
            console.log("Quiz progress:", response);
            const progress = response.progress;
            setCurrentQuestionIndex(progress.LastQuestionIndex || 0);
            setAnswers(progress.QuizAnswers || []);
          })
          .catch((error) =>
            console.error("Error fetching quiz progress", error)
          );
      }
    } catch (error) {
      console.error("Error getting progress:", error);
      /* setError(error); */
    }
  }, [user, quizid, quiz]);

  //Set the current question
  useEffect(() => {
    try {
      if (quiz) {
        setSelectedAnswer(answers[currentQuestionIndex] || null);
      }
    } catch (error) {
      console.error("Error setting current question:", error);
      /* setError(error); */
    }
  }, [currentQuestionIndex, quiz, answers]);

  const handleAnswerChange = (event) => {
    const answer = event.target.value;
    setSelectedAnswer(answer);

    //Store the answer
    const updatedAnswers = [...answers];
    updatedAnswers[currentQuestionIndex] = answer;
    setAnswers(updatedAnswers);

    const answeredQuestions = updatedAnswers.filter(
      (ans) => ans !== null
    ).length;
    const progressPercentage =
      (answeredQuestions / quiz.questions.length) * 100;

    //Update progress
    UserServices.updateProgress({
      UserId: user?.documentId || "",
      CourseId: courseid,
      ModuleId: moduleid,
      ContentId: quizid,
      ContentType: "quiz",
      Progress: progressPercentage,
      LastQuestionIndex: currentQuestionIndex,
      QuizAnswers: { ...answers, [currentQuestionIndex]: answer },
      QuizScore: null,
    })
      .then(() => console.log("Quiz progress updated"))
      .catch((error) => console.error("Error updating quiz progress", error));
  };

  const handleQuestionClick = (index) => {
    setCurrentQuestionIndex(index);
  };

  const handleNext = async () => {
    if (currentQuestionIndex < quiz.questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    } else {
      //Calculate score
      let correctCount = 0;
      quiz.questions.forEach((question, index) => {
        if (question.correctAnswers.includes(answers[index])) {
          correctCount++;
        }
      });

      const scorePercentage = (correctCount / quiz.questions.length) * 100;

      //Submit score to the backend
      const userid = user.documentId;
      UserServices.submitQuizScore(userid, quizid, scorePercentage)
        .then(() => {
          navigate(
            `/course/${courseid}/module/${moduleid}/quiz/${quizid}/quizresults`,
            {
              state: {
                correctCount,
                scorePercentage,
                totalQuestions: quiz.questions.length,
              },
            }
          );
        })
        .catch((error) => {
          console.error("Error submitting quiz score:", error);
        });
    }
  };

  const handleBack = () => {
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex(currentQuestionIndex - 1);
    }
  };

  const toggleSidebar = () => {
    setShowSidebar(!showSidebar);
  };

  if (!quiz) {
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

  const currentQuestion = quiz.questions[currentQuestionIndex];

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
            md={showSidebar ? 9 : 13}
            className={`main-content ${showSidebar ? "content-shift" : ""}`}
          >
            <div className="button-container">
              <div className="back-button-placeholder">
                {currentQuestionIndex > 0 && (
                  <Button className="custom-button" onClick={handleBack}>
                    Back
                  </Button>
                )}
              </div>

              <h1 className="quiz-title">
                Question {currentQuestionIndex + 1}
              </h1>

              {/* Will use this to track number of attempts */}
              <Button
                className="custom-button"
                onClick={handleNext}
                style={{ float: "right" }}
                type="submit"
              >
                {currentQuestionIndex === quiz.questions.length - 1
                  ? "Submit"
                  : "Next"}
              </Button>
            </div>

            {/* Map question and corresponding answers*/}
            <div>
              <Col className="text-center">
                <p className="quiz-question">{currentQuestion.questionText}</p>
                <Form className="d-flex flex-column align-items-center">
                  {currentQuestion.choices.map((choice, index) => (
                    <Form.Check
                      type="radio"
                      id={`answer-${index}`}
                      name="quiz-answer"
                      label={choice}
                      value={choice}
                      checked={selectedAnswer === choice}
                      onChange={handleAnswerChange}
                      className="quiz-answer"
                      key={index}
                    />
                  ))}
                </Form>
              </Col>
            </div>

            {/* Navigation buttons below */}
            <div>
              <Col
                xs="auto"
                className="question-navigation d-flex justify-content-center"
              >
                {quiz.questions.map((_, idx) => (
                  <Button
                    className={`question-toggle ${
                      idx === currentQuestion ? "active" : "inactive"
                    }`}
                    key={idx}
                    onClick={() => handleQuestionClick(idx)}
                  >
                    &nbsp;
                  </Button>
                ))}
              </Col>
            </div>
          </Col>
        </Row>
      </Container>

      <Footer />
    </>
  );
};

export default QuizQuestion;
