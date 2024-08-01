import React, { useContext, useEffect } from "react";
import Header from "../Header";
import Footer from "../Footer";
import { Button, Col, Container, Row, Spinner } from "react-bootstrap";
import { AuthContext } from "../../contexts/AuthContext";

import VideoContentPage from "./VideoContentPage";
import HomeHeader from "../HomeHeader";
import ReadingContentPage from "./ReadingContentPage";
import ErrorPage from "../ErrorPage";
import { useNavigate, useParams } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTh } from "@fortawesome/free-solid-svg-icons";
import Sidebar from "../Sidebar";
import "../../styles/Course.css";
import UserServices from "../../services/UserServices";
import useContentData from "../../hooks/useContentData";

const ContentPage = () => {
  const navigate = useNavigate();
  const { isAuthenticated, user } = useContext(AuthContext);
  const { courseid, contentid, moduleid } = useParams();
  const [state, setState] = useContentData(courseid, contentid, moduleid, user);

  const {
    showSidebar,
    contentType,
    isFirstContentOfCourse,
    isLastPage, // eslint-disable-line no-unused-vars
    courseDetails,
    modules,
    currentContent,
    allContent,
    isLoading,
    currentProgress,
    error,
  } = state;

  useEffect(() => {
    if (contentType === "quiz") {
      console.log("Current content:", currentContent);
      const quizContent = currentContent;
      const quizState = {
        ...currentContent,
      };
      localStorage.setItem("quizState", JSON.stringify(quizState));
      navigate(`/course/${courseid}/module/${moduleid}/quiz/${contentid}`, {
        state: { quiz: quizContent },
      });
    }
  }, [contentType, navigate, courseid, moduleid, contentid, currentContent]);

  //Will navigate to previous page.
  const onBackClick = () => {
    let previousContentId = null;
    let previousModuleId = moduleid;

    const currentModule = modules.find(
      (module) => module.moduleId === moduleid
    );
    console.log("Current module:", currentModule);
    console.log("Current moduleid:", moduleid);

    if (currentModule && currentModule.contents) {
      console.log("Current module contents:", currentModule.contents);
      console.log("Current contentid:", contentid);

      const currentContentIndex = currentModule.contents.findIndex(
        (content) =>
          content.videoId === contentid ||
          content.readingId === contentid ||
          content.quizId === contentid
      );
      console.log("Current content index:", currentContentIndex);

      if (currentContentIndex > 0) {
        previousContentId = currentModule.contents[currentContentIndex - 1].id;
        console.log("Previous content id:", previousContentId);
      } else {
        const currentModuleIndex = modules.indexOf(currentModule);
        console.log("Current module index:", currentModuleIndex);
        if (currentModuleIndex > 0) {
          const previousModule = modules[currentModuleIndex - 1];
          console.log("Previous module:", previousModule);
          previousModuleId = previousModule.moduleId;
          console.log("Previous module id:", previousModuleId);
          previousContentId =
            previousModule.contents[previousModule.contents.length - 1].id;
          console.log("Previous content id:", previousContentId);
        }
      }
    }

    if (previousContentId) {
      navigate(
        `/course/${courseid}/module/${previousModuleId}/content/${previousContentId}`
      );
    }
  };

  //Will navigate to next page.
  const onNextClick = async () => {
    if (contentType === "reading") {
      try {
        await UserServices.updateProgress({
          UserId: user?.documentId || "U",
          CourseId: courseid,
          ModuleId: moduleid,
          ContentId: contentid,
          contentType: "reading",
          Progress: 100,
          LastQuestionIndex: null,
          QuizAnswers: {},
          QuizScore: null,
        });
        console.log("Reading progress updated");
      } catch (error) {
        console.error("Error updating reading progress", error);
      }
    }

    let nextContentId = null;
    let nextModuleId = moduleid;

    //Get current module
    const currentModule = modules.find(
      (module) => module.moduleId === moduleid
    );
    console.log("Current moduleid:", moduleid);
    console.log("Current module:", currentModule);

    if (currentModule && currentModule.contents) {
      console.log("Current module contents:", currentModule.contents);
      console.log("Current contentid:", contentid);

      const currentContentIndex = currentModule.contents.findIndex(
        (content) =>
          content.videoId === contentid ||
          content.readingId === contentid ||
          content.quizId === contentid
      );
      console.log("Current content index:", currentContentIndex);

      //Check if there is next content in the current module
      if (currentContentIndex < currentModule.contents.length - 1) {
        const nextContent = currentModule.contents[currentContentIndex + 1];
        nextContentId =
          nextContent.videoId || nextContent.readingId || nextContent.quizId;
        console.log("Next contentid:", nextContentId);
      } else {
        //Find next module
        const currentModuleIndex = modules.indexOf(currentModule);
        console.log("Current module index:", currentModuleIndex);
        if (currentModuleIndex < modules.length - 1) {
          const nextModule = modules[currentModuleIndex + 1];
          console.log("Next module:", nextModule);
          nextModuleId = nextModule.moduleId;
          console.log("Next moduleid:", nextModuleId);
          if (nextModule.contents && nextModule.contents.length > 0) {
            const nextContent = nextModule.contents[0];
            if (nextContent) {
              nextContentId =
                nextContent.videoId ||
                nextContent.readingId ||
                nextContent.quizId;
              console.log("Next contentid:", nextContentId);
            }
          }
        }
      }
    }

    //If nextContentId is still null, navigate to quiz
    if (nextContentId) {
      const nextContent = currentModule.contents.findIndex(
        (content) =>
          content.videoId === nextContentId ||
          content.readingId === nextContentId ||
          content.quizId === nextContentId
      );
      console.log("Next content:", nextContent);

      if (nextContent.type === "quiz") {
        localStorage.setItem(
          "lastContent",
          `/course/${courseid}/module/${moduleid}/content/${contentid}`
        );
        console.log("nextContent:", nextContent);
        navigate(
          `/course/${courseid}/module/${moduleid}/quiz/${nextContentId}`,
          { state: { quiz: nextContent } }
        );
        console.log("Navigating to Quiz with state:", { quiz: nextContent });
      } else {
        navigate(
          `/course/${courseid}/module/${nextModuleId}/content/${nextContentId}`
        );
      }
    }
  };

  const toggleSidebar = () => {
    setState((prevState) => ({
      ...prevState,
      showSidebar: !prevState.showSidebar,
    }));
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

      <Container fluid>
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
            {contentType === "video" && (
              <>
                <VideoContentPage
                  content={currentContent}
                  courseid={courseid}
                  onProgressUpdate={(progress) =>
                    setState((prevState) => ({
                      ...prevState,
                      currentProgress: progress,
                    }))
                  }
                />
              </>
            )}
            {contentType === "reading" && (
              <>
                <ReadingContentPage
                  content={currentContent}
                  courseid={courseid}
                />
              </>
            )}

            <div className="button-container">
              {!isFirstContentOfCourse && (
                <Button
                  className="custom-button"
                  style={{ marginLeft: "40px" }}
                  onClick={onBackClick}
                >
                  Back
                </Button>
              )}

              <Button
                className="custom-button"
                style={{ float: "right", marginRight: "40px" }}
                onClick={onNextClick}
                disabled={contentType === "video" && currentProgress < 100}
              >
                Next
              </Button>
            </div>
          </Col>
        </Row>
      </Container>
      <Footer />
    </>
  );
};

export default ContentPage;
