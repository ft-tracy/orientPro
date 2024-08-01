import React, { useContext, useEffect, useState } from "react";
import { Container, Spinner } from "react-bootstrap";
import { AuthContext } from "../../contexts/AuthContext";
import Header from "../Header";
import HomeHeader from "../HomeHeader";
import Footer from "../Footer";
import ErrorPage from "../ErrorPage";
import CourseCard from "../dashboard/CourseCard";
import UserServices from "../../services/UserServices";

const CompletedCoursesPage = () => {
  const { isAuthenticated, user } = useContext(AuthContext);
  const [completedCourses, setCompletedCourses] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCompletedCourses = async () => {
      try {
        const fetchedCourses = await UserServices.getCompletedCourses();
        console.log("Completed courses:", fetchedCourses);

        if (!Array.isArray(fetchedCourses)) {
          throw new TypeError("Fetched courses is not an array");
        }

        // Update completed courses to include course progress data
        const updatedCourses = fetchedCourses.map((course) => {
          const courseProgress = user?.courseProgress?.[course.id] || {};
          console.log("courseProgress:", courseProgress);
          return {
            ...course,
            isEnrolled: true,
            hasStarted: courseProgress.hasStarted || false,
            progress: courseProgress.progress || 0,
          };
        });
        setCompletedCourses(updatedCourses);
      } catch (error) {
        console.error("Error fetching completed courses:", error);
        setError(error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchCompletedCourses();
  }, [user?.courseProgress]);

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
        <div className="titleContainer">Completed Courses</div>
        <div className="other-course-card-container">
          {completedCourses.map((data, index) => (
            <CourseCard
              key={index}
              image={data.courseImageUrl}
              title={data.courseTitle}
              description={data.courseDescription}
              isEnrolled={data.isEnrolled}
              hasStarted={data.hasStarted}
              progress={data.progress}
              courseid={data.id}
              userid={user?.documentId}
            />
          ))}
        </div>
      </Container>
      <Footer />
    </>
  );
};

export default CompletedCoursesPage;
