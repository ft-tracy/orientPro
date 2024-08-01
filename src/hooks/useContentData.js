import { useEffect, useState } from "react";
import UserServices from "../services/UserServices";

const useContentData = (courseid, contentid, moduleid, user) => {
  const [state, setState] = useState({
    showSidebar: false,
    contentType: null,
    setIsFirstContentOfCourse: false,
    isLastPage: false,
    courseDetails: {},
    modules: [],
    currentContent: null,
    allContent: [],
    isLoading: true,
    currentProgress: 0,
    error: null,
  });

  useEffect(() => {
    const fetchAllModulesDetails = async () => {
      try {
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

        const allContents = detailedModules.flatMap(
          (module) => module.contents
        );

        //Get specific content and its type
        const currentContent = allContents.find(
          (content) =>
            content.videoId === contentid ||
            content.readingId === contentid ||
            content.quizId === contentid
        );

        const courseDetails = await UserServices.getCourseDetails(courseid);

        if (currentContent) {
          // Fetch progress for reading material
          const progress =
            currentContent.type === "reading"
              ? await UserServices.getProgress(
                  user?.documentId,
                  currentContent.id
                )
              : 0;

          setState((prevState) => ({
            ...prevState,
            modules: detailedModules,
            allContent: allContents,
            currentContent: currentContent,
            contentType: currentContent.type,
            courseDetails: courseDetails,
            currentProgress: progress?.Progress || 0,
            isFirstContentOfCourse:
              detailedModules[0]?.contents[0]?.id === contentid,
            isLastPage:
              detailedModules
                .find((module) => module.moduleId === moduleid)
                ?.contents.slice(-1)[0]?.id === contentid,
            isLoading: false,
          }));
        }
      } catch (error) {
        console.error("Error fetching content details:", error);
        setState((prevState) => ({
          ...prevState,
          error: error,
          isLoading: false,
        }));
      }
    };

    fetchAllModulesDetails();
  }, [contentid, moduleid, courseid, user]);

  return [state, setState];
};

export default useContentData;
