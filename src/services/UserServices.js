import axios from "axios";

const API_URL = "https://orientproservice-1.onrender.com";

class UserServices {
  getUserData() {
    const token = localStorage.getItem("token");
    return axios.get(`${API_URL}/api/user/getuser`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "ngrok-skip-browser-warning": "69420",
      },
    });
  }

  //Dashboard
  //Get all courses
  async getCourses() {
    try {
      console.log(`Requesting courses from:`, `${API_URL}/courses/getCourses`);
      const response = await axios.get(`${API_URL}/courses/getCourses`, {
        headers: {
          "ngrok-skip-browser-warning": "69420",
        },
      });
      console.log("Courses:", response.data);
      return response.data;
    } catch (error) {
      console.error("Error fetching courses:", error);
      throw error;
    }
  }

  //Get enrolled courses
  async getEnrolledCourses() {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `${API_URL}/courses/getEnrolledCourses`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      console.log("Enrolled Courses:", response.data);
      return response.data;
    } catch (error) {
      console.error("Error fecthing enrolled courses:", error);
      return [];
    }
  }

  //Get available courses
  async getAvailableCourses() {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `${API_URL}/courses/getAvailableCourses`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      console.log("Available Courses:", response.data);
      return response.data;
    } catch (error) {
      console.error("Error fecthing available courses:", error);
      return [];
    }
  }

  //Get completed courses
  async getCompletedCourses() {
    try {
      console.log(
        `Fetching completed courses from:`,
        `${API_URL}/courses/getCompletedCourses`
      );
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `${API_URL}/courses/getCompletedCourses`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error fetching completed courses:", error);
      return [];
    }
  }

  //Get tips
  async getTips() {
    try {
      const response = await axios.get(`${API_URL}/api/account/gettips`, {
        headers: {
          "ngrok-skip-browser-warning": "69420",
        },
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching tips:", error);
      throw error;
    }
  }

  //Add tips
  async addTips(userid, tipContent) {
    try {
      console.log(
        `Adding a tip for userId: ${userid} with content: ${tipContent}`
      );
      console.log(`Adding a tip:`, `${API_URL}/api/account/addtip/${userid}`);
      const response = await axios.post(
        `${API_URL}/api/account/addtip/${userid}`,
        { tipContent },
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error adding tip:", error);
      throw error;
    }
  }

  //CoursePage
  //Fetch reviews to display
  async getReviews(courseid) {
    try {
      const response = await axios.get(
        `${API_URL}/api/reviews/course/${courseid}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error fecthing reviews:", error);
      return [];
    }
  }

  //Add a review
  async addReview(userid, courseid, review) {
    try {
      const response = await axios.post(
        `${API_URL}/api/reviews/${userid}/review/${courseid}`,
        review,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error adding review:", error);
    }
  }

  //Get course details
  async getCourseDetails(courseid) {
    try {
      const response = await axios.get(`${API_URL}/courses/${courseid}`, {
        headers: {
          "ngrok-skip-browser-warning": "69420",
        },
      });
      console.log("getCourseDetails response:", response.data);
      return response.data;
    } catch (error) {
      console.error("Error fetching course details", error);
      throw error;
    }
  }

  //Get modules of a specific course
  async getModules(courseid) {
    try {
      const response = await axios.get(
        `${API_URL}/api/modules/${courseid}/modules`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      console.log("getModules response:", response.data);
      return response.data;
    } catch (error) {
      console.error("Error fetching modules:", error);
      throw error;
    }
  }

  //Get contents for a specific module
  async getContentDetails(moduleid) {
    try {
      console.log(
        `Requesting content from:`,
        `${API_URL}/content/getModuleContents/${moduleid}`
      );
      const response = await axios.get(
        `${API_URL}/content/getModuleContents/${moduleid}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error fetching content details:", error);
      throw error;
    }
  }

  //Enroll user in course
  async enrollCourse(courseid, userid) {
    try {
      console.log(
        `Enrolling user in course:`,
        `${API_URL}/api/user/${userid}/enroll/${courseid}`
      );
      const response = await axios.post(
        `${API_URL}/api/user/${userid}/enroll/${courseid}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error enrolling in course:", error);
      throw error;
    }
  }

  //Change hasStarted flag to true
  async startCourse(courseid, userid) {
    try {
      console.log(
        `Starting course:`,
        `${API_URL}/api/user/${userid}/start/${courseid}`
      );
      const response = await axios.post(
        `${API_URL}/api/user/${userid}/start/${courseid}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error starting course:", error);
      throw error;
    }
  }

  //Get details of a specific module
  async getModuleDetails(moduleid) {
    try {
      console.log(
        `Requesting module details from:`,
        `${API_URL}/api/modules/${moduleid}`
      );
      const response = await axios.get(`${API_URL}/api/modules/${moduleid}`, {
        headers: {
          "ngrok-skip-browser-warning": "69420",
        },
      });
      console.log("getModuleDetails response:", response.data);
      return response.data;
    } catch (error) {
      console.error("Error fetching module details:", error);
      throw error;
    }
  }

  //Content pages
  //Send progress updates
  async updateProgress(data) {
    try {
      console.log(
        `Updating progress via:`,
        `${API_URL}/api/progress/updateprogress`
      );
      const response = await axios.post(
        `${API_URL}/api/progress/updateprogress`,
        data,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error updating progress:", error);
      throw error;
    }
  }

  //Get progress
  async getProgress(userid, contentid) {
    try {
      console.log(
        `Getting progress from:`,
        `${API_URL}/api/progress/getprogress/${userid}/${contentid}`
      );
      const response = await axios.get(
        `${API_URL}/api/progress/getprogress/${userid}/${contentid}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error getting progress:", error);
      throw error;
    }
  }

  //Get last accessed content
  async getLastAccessedContent(userid) {
    try {
      const response = await axios.get(
        `${API_URL}/api/Progress/lastAccessedContent/${userid}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error getting last accessed content:", error);
      throw error;
    }
  }

  //VideoContentPage
  async getComments(videoid) {
    try {
      const response = await axios.get(
        `${API_URL}/Content/GetCommentsAndRepliesForVideo/${videoid}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error fetching comments and replies:", error);
      throw error;
    }
  }

  async addComment(userid, videoid, commentText) {
    try {
      console.log(
        `Adding comment by ${userid} to ${videoid} with text: ${commentText}`
      );
      const response = await axios.post(
        `${API_URL}/Content/AddCommentToVideo/${videoid}/${userid}`,
        { commentText },
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error adding comment:", error);
      throw error;
    }
  }

  async addReply(userid, videoid, commentid, replyText) {
    try {
      console.log(
        `Adding reply by ${userid} to ${videoid} with text: ${replyText}`
      );
      const response = await axios.post(
        `${API_URL}/Content/AddReplyToComment/${videoid}/${commentid}/${userid}`,
        { replyText },
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error adding reply:", error);
      throw error;
    }
  }

  async updateCommentLikes(videoid, commentId, likes) {
    try {
      const response = await axios.put(
        `${API_URL}/Content/likeComment/${videoid}/${commentId}`,
        { likes },
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error updating comment likes:", error);
      throw error;
    }
  }

  async updateReplyLikes(videoid, commentId, replyId, likes) {
    try {
      const response = await axios.put(
        `${API_URL}/Content/likeReply/${videoid}/${commentId}/${replyId}`,
        { likes },
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error updating reply likes:", error);
      throw error;
    }
  }

  //Quiz pages
  //Submit quiz score
  async submitQuizScore(userid, quizid, score) {
    try {
      const response = await axios.put(
        `${API_URL}/api/Progress/submitQuizScore/${userid}/${quizid}/${score}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error submitting quiz score");
      throw error;
    }
  }

  //Get all quizzes
  async getQuizzes(moduleid, quizid) {
    try {
      const response = await axios.get(
        `${API_URL}/content/getquiz/${moduleid}/${quizid}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error fetching quizzes");
    }
  }

  //Download certificate
  async downloadCertificate(userid, courseid) {
    try {
      console.log(
        `Downloading certificate for user ${userid}, course ${courseid} from:`,
        `${API_URL}/api/certificate/download/${userid}/${courseid}`
      );
      const response = await axios.post(
        `${API_URL}/api/certificate/download/${userid}/${courseid}`,
        {
          headers: {
            "ngrok-skip-browser-warning": "69420",
          },
        },
        {
          responseType: "blob",
        }
      );

      //A PDF blob is received and download is triggered
      const url = window.URL.createObjectURL(new Blob([response.data]));
      console.log("url:", url);
      const link = document.createElement("a");
      console.log("link:", link);
      link.href = url;
      link.setAttribute("download", "certificate.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      throw error;
    }
  }

  //Weekly progress
  async getUserAccessData(userId) {
    try {
      console.log(`Getting weekly progress for:`, `${userId}`);
      const response = await axios.get(
        `${API_URL}/api/Progress/userAccessData/${userId}`
      );
      console.log("Weekly progress:", response.data);
      return response.data;
    } catch (error) {
      console.error("Error fetching user access data:", error);
      throw error;
    }
  }
}

/* eslint-disable-next-line import/no-anonymous-default-export */
export default new UserServices();
