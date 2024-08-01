import React, { useContext, useState, useEffect } from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { AuthContext } from "../../contexts/AuthContext";
import "../../styles/GuestDashboard.css";
import HomeCard from "../HomeCard";
import ErrorPage from "../ErrorPage";
import UserServices from "../../services/UserServices";

const GuestDashboard = () => {
  const { isAuthentiated, user } = useContext(AuthContext); // eslint-disable-line no-unused-vars
  const [groups, setGroups] = useState([]);
  const [currentIndexes, setCurrentIndexes] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const getCourses = async () => {
      try {
        const courses = await UserServices.getCourses();
        const availableCourses = courses.filter(
          (course) => !course.isEnrolled && !course.exclusiveToCompanyEmployees
        );

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

  const handleNextArrowClick = (groupIndex) => {
    setCurrentIndexes((prevIndexes) => {
      const newIndexes = [...prevIndexes];
      const groupKeys = Object.keys(groups);
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
          : groups[groupIndex].courses.length - 1;
      return newIndexes;
    });
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <ErrorPage error={error} />;
  }

  const groupKeys = Object.keys(groups);

  return (
    <>
      <Container>
        <div className="guest-layout">
          <div className="titleContainer">Available Courses</div>
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
    </>
  );
};

export default GuestDashboard;
