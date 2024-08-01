import React, { useState, useEffect, useContext } from "react";
import Header from "../Header";
import Footer from "../Footer";
import { Container, Row, Col, Spinner } from "react-bootstrap";
import CoursesCard from "./CoursesCard";
import ReviewsCard from "./ReviewsCard";
import RatingsCard from "./RatingsCard";
import ErrorPage from "../ErrorPage";
import UserServices from "../../services/UserServices";
import { AuthContext } from "../../contexts/AuthContext";
import HomeHeader from "../HomeHeader";
import "../../styles/Course.css";
import { useParams } from "react-router-dom";
import { format } from "date-fns";

const Course = () => {
  const { isAuthenticated, user } = useContext(AuthContext);
  const [courseDetails, setCourseDetails] = useState({});
  const [modules, setModules] = useState([]);
  const [reviews, setReviews] = useState([]);
  const { courseid } = useParams();
  const [allContent, setAllContent] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  //Fetch reviews from backend
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [details, modulesResponse, reviewsResponse] = await Promise.all([
          UserServices.getCourseDetails(courseid),
          UserServices.getModules(courseid),
          UserServices.getReviews(courseid),
        ]);

        setCourseDetails(details || {});
        console.log("Course details:", details);
        setModules(modulesResponse || []);
        console.log("Modules:", modulesResponse);
        setReviews(reviewsResponse || []);
        console.log("Reviews:", reviewsResponse);

        //Fetch all content for modules
        const allContentPromises = modulesResponse.map(async (module) => {
          const content = await UserServices.getContentDetails(module.moduleId);
          console.log("Module id:", module.moduleId);
          console.log("Content:", content);

          return {
            moduleId: module.moduleId,
            content: [
              ...content.videos.map((video) => ({
                type: "video",
                timestamp: new Date(video.uploadedDate),
                ...video,
              })),
              ...content.readingMaterials.map((reading) => ({
                type: "reading",
                timestamp: new Date(reading.uploadedDate),
                ...reading,
              })),
              ...content.quizzes.map((quiz) => ({
                type: "quiz",
                timestamp: new Date(quiz.createdDate),
                ...quiz,
              })),
            ],
          };
        });
        const contentResults = await Promise.all(allContentPromises);

        //Sort contents by timestamp
        contentResults.forEach((moduleContent) => {
          moduleContent.content.sort((a, b) => a.timestamp - b.timestamp);
        });

        //Combine all contents into a single array
        const allContent = contentResults.reduce((acc, moduleContent) => {
          return acc.concat(moduleContent.content);
        }, []);

        setAllContent(allContent);
        console.log("All Content:", allContent);

        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching data:", error);
        setError(error);
        setIsLoading(false);
      }
    };

    fetchData();
  }, [courseid]);

  //Format createdOn date
  const formattedDate = courseDetails.createdOn
    ? format(new Date(courseDetails.createdOn), "dd/MM/yy")
    : "";

  const userId = user?.documentId || "";
  console.log("User id:", userId);

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
      <Container fluid>
        <Row>
          <Col>
            <div className="split-container">
              <div className="course-container">
                <div>
                  <CoursesCard
                    image={courseDetails.courseImageUrl}
                    title={courseDetails.courseTitle}
                    date={formattedDate}
                    description={courseDetails.courseDescription}
                    modules={modules}
                    courseid={courseid}
                    userid={userId}
                    allContent={allContent}
                    tags={courseDetails.courseTags}
                  />
                </div>
              </div>

              <div className="reviews-container">
                <div className="content-title">Reviews</div>
                <div className="reviews-card">
                  <RatingsCard reviews={reviews} />
                  {reviews.length > 0 ? (
                    reviews.map((review, index) => {
                      const userInitials =
                        review.firstName.charAt(0).toUpperCase() +
                        review.lastName.charAt(0).toUpperCase();
                      const reviewFormattedDate = review.reviewDate
                        ? format(new Date(review.reviewDate), "dd/MM/yy")
                        : "";
                      return (
                        <ReviewsCard
                          key={index}
                          userInitial={userInitials}
                          name={`${review.firstName} ${review.lastName}`}
                          date={reviewFormattedDate}
                          stars={review.rating}
                          text={review.reviewContent}
                        />
                      );
                    })
                  ) : (
                    <p>No reviews available</p>
                  )}
                </div>
              </div>
            </div>
          </Col>
        </Row>
      </Container>

      <Footer />
    </>
  );
};

export default Course;
