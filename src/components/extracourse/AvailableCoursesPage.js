import React, { useContext, useEffect, useState } from "react";
import { Container, Row, Col, Button, Spinner } from "react-bootstrap";
import { AuthContext } from "../../contexts/AuthContext";
import Header from "../Header";
import HomeHeader from "../HomeHeader";
import Footer from "../Footer";
import "../../styles/GuestDashboard.css";
import ErrorPage from "../ErrorPage";
import HomeCard from "../HomeCard";
import UserServices from "../../services/UserServices";

const AvailableCoursesPage = () => {
  const { isAuthenticated } = useContext(AuthContext);
  const [groups, setGroups] = useState({});
  const [currentIndexes, setCurrentIndexes] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const getCourses = async () => {
      try {
        const courses = await UserServices.getCourses();
        if (!courses) {
          throw new Error("No courses returned from the API");
        }
        const availableCourses = courses.filter(
          (course) => !course.isEnrolled && !course.exclusiveToCompanyEmployees
        );

        // Group courses based on their tags
        const groupedCourses = availableCourses.reduce((acc, course) => {
          if (Array.isArray(course.courseTags)) {
            course.courseTags.forEach((tag) => {
              if (!acc[tag]) {
                acc[tag] = [];
              }
              acc[tag].push(course);
            });
          }
          return acc;
        }, {});

        const initialIndexes = Object.keys(groupedCourses).map(() => 0);

        setGroups(groupedCourses);
        setCurrentIndexes(initialIndexes);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching courses:", error);
        setError(error);
        setIsLoading(false);
      }
    };

    getCourses();
  }, []);

  const groupKeys = Object.keys(groups);

  const handleNextArrowClick = (groupIndex) => {
    setCurrentIndexes((prevIndexes) => {
      const newIndexes = [...prevIndexes];
      const groupCourses = groups[groupKeys[groupIndex]];
      newIndexes[groupIndex] =
        newIndexes[groupIndex] + 1 < groupCourses.length
          ? newIndexes[groupIndex] + 1
          : newIndexes[groupIndex];
      return newIndexes;
    });
  };

  const handlePreviousArrowClick = (groupIndex) => {
    setCurrentIndexes((prevIndexes) => {
      const newIndexes = [...prevIndexes];
      newIndexes[groupIndex] =
        newIndexes[groupIndex] > 0
          ? newIndexes[groupIndex] - 1
          : prevIndexes[groupIndex];
      return newIndexes;
    });
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

      <Container>
        <div className="guest-layout">
          <div className="titleContainer">Offered courses</div>
          {groupKeys.map((group, groupIndex) => (
            <div key={groupIndex}>
              <div className="content-title">{group}</div>
              <Row>
                <Col
                  xs={1}
                  className="d-flex align-items-center justify-content-center"
                >
                  {currentIndexes[groupIndex] > 0 && (
                    <Button
                      onClick={() => handlePreviousArrowClick(groupIndex)}
                      className="navigation-button"
                    >
                      {"<"}
                    </Button>
                  )}
                </Col>
                <Col xs={10}>
                  <Row className="course-row">
                    {groups[group]
                      .slice(
                        currentIndexes[groupIndex],
                        currentIndexes[groupIndex] + 2
                      )
                      .map((course, index) => (
                        <Col key={index} md={6}>
                          <HomeCard
                            image={course.courseImageUrl}
                            title={course.courseTitle}
                            courseid={course.id}
                          />
                        </Col>
                      ))}
                  </Row>
                </Col>
                <Col
                  xs={1}
                  className="d-flex align-items-center justify-content-center"
                >
                  {currentIndexes[groupIndex] + 2 < groups[group].length && (
                    <Button
                      onClick={() => handleNextArrowClick(groupIndex)}
                      className="navigation-button"
                    >
                      {">"}
                    </Button>
                  )}
                </Col>
              </Row>
            </div>
          ))}
        </div>
      </Container>
      <Footer />
    </>
  );
};

export default AvailableCoursesPage;
