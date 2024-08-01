import React, { useContext, useEffect, useState } from "react";
import { Button, Modal, Container, Spinner } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import UserServices from "../../services/UserServices";
import { AuthContext } from "../../contexts/AuthContext";

const DownloadCertificate = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  const { courseid, moduleid, quizid } = useParams();
  const [isLoading, setIsLoading] = useState(false);
  const [courseProgress, setCourseProgress] = useState(0);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCourseProgress = async () => {
      try {
        const courses = await UserServices.getEnrolledCourses();
        const course = courses.find((course) => course.id === courseid);
        if (course) {
          const progressData = user?.courseProgress[course.id] || {};
          setCourseProgress(progressData.progress || 0);
        }
      } catch (error) {
        console.error("Error fetching course progress:", error);
        setError(error);
      }
    };
    fetchCourseProgress();
  }, [user?.courseProgress, courseid]);

  const handleClose = () => {
    navigate(
      `/course/${courseid}/module/${moduleid}/quiz/${quizid}/quizresults`
    );
  };

  const onDownloadClick = async () => {
    if (courseProgress < 100) {
      alert(
        `Not all content completed, kindly go back and complete all content. Course progress: ${courseProgress}%`
      );
      return;
    }

    setIsLoading(true);
    try {
      console.log("Starting certificate download");
      const userid = user?.documentId;
      await UserServices.downloadCertificate(userid, courseid);
    } catch (error) {
      console.error("Error downloading the certificate:", error);
    } finally {
      setIsLoading(false);
      navigate("/certificates");
    }
  };

  return (
    <Modal show={true} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Download</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="titleContainer">Congratulations!!</div>
        <br />
        <div className="center" style={{ fontSize: "18px" }}>
          <p>You have completed the course.</p>
          <p>
            Click the button below to download your certificate and share it
            with your friends and family.
          </p>
        </div>

        <br />
        <Button
          className="custom-button"
          style={{ float: "right" }}
          onClick={onDownloadClick}
        >
          {isLoading ? (
            <Spinner animation="border" size="sm" />
          ) : (
            "Download Certificate"
          )}
        </Button>
      </Modal.Body>
    </Modal>
  );
};

export default DownloadCertificate;
