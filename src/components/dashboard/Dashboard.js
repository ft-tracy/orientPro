// Dashboard.js
import React, { useEffect, useState, useContext } from "react";
import { Container, Button, Spinner } from "react-bootstrap";
import CourseCard from "./CourseCard";
import Header from "../Header";
import Footer from "../Footer";
import HomeHeader from "../HomeHeader";
import GuestDashboard from "./GuestDashboard";
import TipsCard from "./TipsCard";
import ErrorPage from "../ErrorPage";
import { withAuth } from "../../hoc/withAuth";
import { withNavigate } from "../../hoc/withNavigate";
import { compose } from "redux";
import UserServices from "../../services/UserServices";
import { AuthContext } from "../../contexts/AuthContext";
import moment from "moment"; // Ensure moment is installed

const Dashboard = (props) => {
  const { user, isAuthenticated } = useContext(AuthContext);
  const [enrolledCourses, setEnrolledCourses] = useState([]);
  const [availableCourses, setAvailableCourses] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [userRole, setUserRole] = useState(null);
  const [error, setError] = useState(null);
  const [accessData, setAccessData] = useState([]); // Add state for access data

  useEffect(() => {
    const fetchUserDataAndCourse = async () => {
      console.log("Component did mount");
      console.log("AuthContext user:", user);

      if (isAuthenticated && user) {
        console.log("User authenticated, fetching user data");

        console.log("Fetched user data:", user);
        setUserRole(user.role);
        await fetchCourses(user.role, user.courseProgress);
        await fetchUserAccessData(user.documentId); // Fetch user access data
      } else {
        console.log("User not found in AuthContext");
        setError(error);
        setIsLoading(false);
      }
    };

    fetchUserDataAndCourse();
  }, [user, isAuthenticated]);

  const fetchCourses = async (role, courseProgress) => {
    console.log("Fetching courses for role:", role);

    let fetchedEnrolledCourses = [];
    let fetchedAvailableCourses = [];

    if (role === "Trainee" || role === "Guest") {
      fetchedEnrolledCourses = await UserServices.getEnrolledCourses();
      fetchedAvailableCourses = await UserServices.getAvailableCourses();
      fetchedAvailableCourses = filterAvailableCoursesByTags(
        fetchedEnrolledCourses,
        fetchedAvailableCourses
      );
    }

    // Update enrolled courses to include course progress data
    fetchedEnrolledCourses = fetchedEnrolledCourses.map((course) => {
      const progressData = courseProgress[course.id] || {};
      return {
        ...course,
        isEnrolled: true,
        hasStarted: progressData.hasStarted || false,
        progress: progressData.progress || 0,
      };
    });

    setEnrolledCourses(fetchedEnrolledCourses);
    setAvailableCourses(fetchedAvailableCourses);
    setIsLoading(false);
  };

  const fetchUserAccessData = async (userId) => {
    try {
      const data = await UserServices.getUserAccessData(userId);
      setAccessData(data);
    } catch (error) {
      console.error("Error fetching user access data:", error);
    }
  };

  const filterAvailableCoursesByTags = (enrolled, available) => {
    const enrolledTags = enrolled.reduce((acc, course) => {
      return acc.concat(course.courseTags);
    }, []);

    return available.filter((course) =>
      course.courseTags.some((tag) => enrolledTags.includes(tag))
    );
  };

  const handleSeeMoreCourses = () => {
    props.navigate("/availablecourses");
  };

  const isGuest = userRole === "Guest";
  const isGuestEnrolled = enrolledCourses.length > 0;

  const userInitials =
    user?.firstName.charAt(0).toUpperCase() +
    user?.lastName.charAt(0).toUpperCase();
  const userName = user?.firstName + " " + user?.lastName;

  const getLastSevenDays = () => {
    return [...Array(7).keys()]
      .map((i) => moment().subtract(i, "days").format("YYYY-MM-DD"))
      .reverse();
  };

  const checkAccessedDay = (date) => {
    const dayData = accessData.find((item) => item.date === date);
    return dayData && dayData.accessCount > 0;
  };

  const daysOfWeek = getLastSevenDays();

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
        <div className="welcome-text">
          Welcome {user ? user.firstName : "User"}!
        </div>
        {isGuest && !isGuestEnrolled ? (
          <GuestDashboard />
        ) : (
          <div className="split-container">
            <div className="enrolled-courses">
              <div className="content-title">Enrolled Courses</div>
              <div className="card-container">
                {enrolledCourses.map((data, index) => (
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
            </div>
            <div className="available-courses">
              <div className="content-title">Available Courses</div>
              <div className="card-container">
                {availableCourses.slice(0, 2).map((data, index) => (
                  <CourseCard
                    key={index}
                    image={data.courseImageUrl}
                    title={data.courseTitle}
                    description={data.courseDescription}
                    isEnrolled={false}
                    hasStarted={false}
                    progress={0}
                    courseid={data.id}
                    userid={user?.documentId}
                  />
                ))}
              </div>
              {(availableCourses.length > 2 ||
                availableCourses.length === 0) && (
                <Button
                  className="custom-button"
                  onClick={handleSeeMoreCourses}
                >
                  See More Courses
                </Button>
              )}

              <br />
              {userRole === "Trainee" && (
                <>
                  <div className="content-title">Helpful Tips</div>
                  <div className="reviews-card">
                    <TipsCard userInitials={userInitials} userName={userName} />
                  </div>
                </>
              )}
            </div>
          </div>
        )}
        {/* <div className="weekly-access">
          <div className="content-title">Weekly Access</div>
          <div className="days-container">
            {daysOfWeek.map((day, index) => (
              <div
                key={index}
                className="day"
                style={{
                  backgroundColor: checkAccessedDay(day) ? "blue" : "grey",
                  color: "white",
                  padding: "10px",
                  margin: "5px",
                  borderRadius: "5px",
                }}
              >
                {moment(day).format("dddd")}
              </div>
            ))}
          </div>
        </div> */}
      </Container>
      <Footer />
    </>
  );
};

export default compose(withAuth, withNavigate)(Dashboard);
