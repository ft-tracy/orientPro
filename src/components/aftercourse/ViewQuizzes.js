import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import Header from "../Header";
import HomeHeader from "../HomeHeader";
import Footer from "../Footer";
import ErrorPage from "../ErrorPage";
import { Container, ListGroup, Spinner } from "react-bootstrap";
import UserServices from "../../services/UserServices";
import "../../styles/Quiz.css";

const ViewQuizzes = () => {
  const { isAuthenticated, user } = useContext(AuthContext);
  const [quizProgress, setQuizProgress] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAllQuizzes = async () => {
      try {
        const courses = await UserServices.getEnrolledCourses();
        const allQuizzes = [];
        const progressPromises = [];

        for (const course of courses) {
          const modules = await UserServices.getModules(course.id);

          for (const module of modules) {
            const contentDetails = await UserServices.getContentDetails(
              module.moduleId
            );
            console.log("Content Details:", contentDetails);

            const moduleQuizzes = contentDetails.quizzes;
            console.log("moduleQuizzes:", moduleQuizzes);

            for (const quiz of moduleQuizzes) {
              const quizData = await UserServices.getQuizzes(
                module.moduleId,
                quiz.quizId
              );
              allQuizzes.push({
                ...quizData,
                courseId: course.courseid,
                moduleName: module.title,
              });

              progressPromises.push(
                UserServices.getProgress(user?.documentId, quiz.quizId)
              );
            }
          }
        }

        const progressResults = await Promise.all(progressPromises);
        console.log("Progress Results:", progressResults);

        const progressData = allQuizzes.map((quiz, index) => {
          const progress = progressResults[index];
          console.log("progress:", progress);
          let status = "Not Started";
          let score = null;

          if (progress) {
            if (progress.progress === 100) {
              status = "Completed";
              score = progress.quizScore;
            } else if (progress.progress > 0) {
              status = "In Progress";
            }
          }

          return { ...quiz, status, score };
        });

        setQuizProgress(progressData);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching quizzes:", error);
        setError(error);
        setIsLoading(false);
      }
    };

    fetchAllQuizzes();
  }, [user]);

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
        <div className="titleContainer" style={{ width: "100%" }}>
          Quizzes
        </div>

        <ListGroup className="all-quizzes-container">
          {quizProgress.map((quiz, index) => (
            <ListGroup.Item key={index}>
              <div className="quizzes-details">
                <div className="quizzes-title">{quiz.moduleName} Quiz</div>
                <div className="quiz-status">
                  Status: {quiz.status}
                  {quiz.status === "Completed" && (
                    <span style={{ marginLeft: "100px" }}>
                      Score: {quiz.score}%
                    </span>
                  )}
                </div>
              </div>
            </ListGroup.Item>
          ))}
        </ListGroup>
      </Container>
      <Footer />
    </>
  );
};

export default ViewQuizzes;
