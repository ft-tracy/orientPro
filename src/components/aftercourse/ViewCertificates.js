import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import Header from "../Header";
import HomeHeader from "../HomeHeader";
import Footer from "../Footer";
import ErrorPage from "../ErrorPage";
import { Button, Container, ListGroup, Row, Spinner } from "react-bootstrap";
import UserServices from "../../services/UserServices";
import { useNavigate } from "react-router-dom";

const ViewCertificates = () => {
  const { isAuthenticated, user } = useContext(AuthContext);
  const navigate = useNavigate();
  const [earnedCertificates, setEarnedCertificates] = useState([]);
  const [yetToEarnCertificates, setYetToEarnCertificates] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCertificates = async () => {
      try {
        const courses = await UserServices.getEnrolledCourses();
        console.log("Enrolled courses:", courses);

        // Update enrolled courses to include course progress data
        const enrolledCourses = courses.map((course) => {
          const progressData = user?.courseProgress[course.id] || {};
          return {
            ...course,
            isEnrolled: true,
            hasStarted: progressData.hasStarted || false,
            progress: progressData.progress || 0,
          };
        });

        const earned = enrolledCourses.filter(
          (course) => course.progress === 100
        );
        const yetToEarn = enrolledCourses.filter(
          (course) => course.progress >= 0 && course.progress < 100
        );
        setEarnedCertificates(earned);
        setYetToEarnCertificates(yetToEarn);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching certificates:", error);
        setError(error);
        setIsLoading(false);
      }
    };
    fetchCertificates();
  }, [user?.courseProgress]);

  const onViewDownloadClick = async (courseId) => {
    setIsLoading(true);
    try {
      const userid = user?.documentId;
      await UserServices.downloadCertificate(userid, courseId);
    } catch (error) {
      console.error("Error downloading the certificate:", error);
    } finally {
      setIsLoading(false);
      navigate("/certificates");
    }
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
      {isAuthenticated ? <Header /> : <HomeHeader />}

      <Container style={{ marginLeft: "40px" }}>
        <Row>
          <h2 className="titleContainer">Your certificates</h2>
          {/*Welcome to the certificates page*/}
          <div className="earned-container">
            <h3 style={{ backgroundColor: "white" }}>Earned certificates</h3>
            <ListGroup>
              {earnedCertificates.length > 0 ? (
                earnedCertificates.map((course, index) => (
                  <ListGroup.Item
                    key={index}
                    className="d-flex justify-content-between align-items-center"
                  >
                    <span>{course.courseTitle}</span>
                    <Button
                      className="custom-button"
                      onClick={() => onViewDownloadClick(course.id)}
                    >
                      Download certificate
                    </Button>
                  </ListGroup.Item>
                ))
              ) : (
                <ListGroup.Item>
                  Once you complete a course all certificates earned will appear
                  here
                </ListGroup.Item>
              )}
            </ListGroup>
          </div>

          <div className="not-earned-container">
            <h3 style={{ backgroundColor: "white" }}>
              Yet to earn certificates
            </h3>
            <ListGroup>
              {yetToEarnCertificates.length > 0 ? (
                yetToEarnCertificates.map((course, index) => (
                  <ListGroup.Item
                    key={index}
                    className="d-flex justify-content-between align-items-center"
                  >
                    <span>{course.courseTitle}</span>
                  </ListGroup.Item>
                ))
              ) : (
                <ListGroup.Item>
                  Once you enroll in a course, the corresponding certificate
                  will appear here
                </ListGroup.Item>
              )}
            </ListGroup>
          </div>
        </Row>
      </Container>
      <Footer />
    </>
  );
};

export default ViewCertificates;
