import React, { useEffect, useRef, useContext, useState } from "react";
import { Container } from "react-bootstrap";
import CreatorVideoCard from "./CreatorVideoCard";
import ConversationsCard from "./ConversationsCard";
import UserServices from "../../services/UserServices";
import { AuthContext } from "../../contexts/AuthContext";
import { useParams } from "react-router-dom";

const VideoContentPage = ({ content, courseid, onProgressUpdate }) => {
  const { user } = useContext(AuthContext);
  const videoURL = content.videoUrl;
  const videoRef = useRef(null);
  const { contentid } = useParams();
  const [progress, setProgress] = useState(0); // eslint-disable-line no-unused-vars

  const updateProgress = async () => {
    if (videoRef.current) {
      const currentTime = videoRef.current.currentTime;
      console.log("currentTime:", currentTime);
      const totalTime = videoRef.current.duration;
      console.log("totalTime:", totalTime);
      const newProgress = totalTime > 0 ? (currentTime / totalTime) * 100 : 0;
      console.log("progress:", newProgress);

      //Update progress state in parent component
      setProgress(newProgress);

      UserServices.updateProgress({
        UserId: user?.documentId || "",
        CourseId: courseid,
        ModuleId: content.moduleId,
        ContentId: content.id,
        ContentType: "video",
        Progress: newProgress,
        LastQuestionIndex: null,
        QuizAnswers: {},
        QuizScore: null,
      })
        .then(() => console.log("Video progress updated"))
        .catch((error) =>
          console.error("Error updating video progress", error)
        );

      if (onProgressUpdate) {
        onProgressUpdate(newProgress);
      }
    }
  };

  useEffect(() => {
    const userid = user?.documentId || "";
    console.log("Course id:", courseid);
    const contentid = content.id;

    UserServices.getProgress(userid, contentid)
      .then((response) => {
        console.log("Response:", response);
        const progress = response.progress;
        console.log("progress:", progress);
        if (progress !== undefined && videoRef.current) {
          const totalTime = videoRef.current.duration;
          console.log("totalTime:", totalTime);
          const currentTime = (progress / 100) * totalTime;
          console.log("Setting currentTime to:", currentTime);
          videoRef.current.currentTime = currentTime;
          setProgress(progress);
        } else {
          console.log("No progress data available");
        }
      })
      .catch((error) => console.error("Error fetching video progress", error));

    // Set up an interval to update progress every 10 seconds
    const interval = setInterval(() => {
      updateProgress();
    }, 60000);

    return () => {
      clearInterval(interval); // Clear interval on component unmount
      updateProgress(); // Save progress one last time
    };
  }, [contentid, courseid, content.moduleId, content.id, user?.documentId]);

  return (
    <Container fluid>
      <div className="split-container">
        <div className="video-comments-container">
          <div className="video-frame">
            <video ref={videoRef} width="100%" controls>
              <source src={videoURL} type="video/mp4" />
              <source src={`${videoURL}.webm`} type="video/webm" />
              <source src={`${videoURL}.ogv`} type="video/ogg" />
              Your browser does not support the video tag
            </video>
          </div>
          <CreatorVideoCard content={content} />
        </div>
        <div className="conversation-container">
          <ConversationsCard videoid={contentid} userid={user?.documentId} />
        </div>
      </div>
    </Container>
  );
};

export default VideoContentPage;
