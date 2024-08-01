import { useState, useEffect } from "react";
import UserServices from "../services/UserServices";

const useCourseDetails = (courseid) => {
  const [courseDetails, setCourseDetails] = useState({});
  const [modules, setModules] = useState([]);
  const [allContent, setAllContent] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchContentDetails = async () => {
      try {
        const courseDetails = await UserServices.getCourseDetails(courseid);
        setCourseDetails(courseDetails || {});

        //For sidebar
        const modulesList = await UserServices.getModules(courseid);
        console.log("Module list:", modulesList);

        const detailedModules = await Promise.all(
          modulesList.map(async (module) => {
            const moduleDetails = await UserServices.getModuleDetails(
              module.moduleId
            );

            const contentDetails = await UserServices.getContentDetails(
              module.moduleId
            );
            console.log("Module details:", moduleDetails);
            console.log("Content details:", contentDetails);
            let allContents = [];

            if (contentDetails.videos) {
              allContents = allContents.concat(
                contentDetails.videos.map((video) => ({
                  type: "video",
                  timestamp: new Date(video.uploadedDate),
                  ...video,
                }))
              );
            }

            if (contentDetails.readingMaterials) {
              allContents = allContents.concat(
                contentDetails.readingMaterials.map((reading) => ({
                  type: "reading",
                  timestamp: new Date(reading.uploadedDate),
                  ...reading,
                }))
              );
            }

            if (contentDetails.quizzes) {
              allContents = allContents.concat(
                contentDetails.quizzes.map((quiz) => ({
                  type: "quiz",
                  timestamp: new Date(quiz.createdDate),
                  ...quiz,
                }))
              );
            }

            // Sort contents by timestamp
            allContents.sort((a, b) => a.timestamp - b.timestamp);
            console.log(
              `All contents sorted by timestamp for module ${module.moduleId}:`,
              allContents
            );

            return {
              ...moduleDetails,
              moduleId: module.moduleId,
              contents: allContents.map((content) => ({
                id: content.videoId || content.readingId || content.quizId,
                ...content,
              })),
            };
          })
        );

        console.log("Detailed modules:", detailedModules);
        setModules(detailedModules);

        const allContents = detailedModules.flatMap(
          (module) => module.contents
        );
        setAllContent(allContents);

        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching content details:", error);
        setError(error);
        setIsLoading(false);
      }
    };

    fetchContentDetails();
  }, [courseid]);

  return { courseDetails, modules, allContent, isLoading, error };
};

export default useCourseDetails;
