import { lazy, Suspense } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./components/Home";
import ErrorBoundary from "./components/ErrorBoundary";

//Import the styles needed in the project
import "./styles/App.css";
import "./styles/index.css";
import "./styles/Dashboard.css";
import "./styles/Header.css";
import "./styles/Course.css";
import "./styles/Content.css";
import "./styles/Module.css";
import "./styles/GuestDashboard.css";
import "./styles/Quiz.css";

//Lazily load pages
const Login = lazy(() => import("./components/login/Login"));
const Signup = lazy(() => import("./components/signup/Signup"));
const ResetPassword = lazy(() =>
  import("./components/changepassword/ResetPassword")
);
const ForgotPassword = lazy(() =>
  import("./components/changepassword/ForgotPassword")
);

//Dashboards
const Dashboard = lazy(() => import("./components/dashboard/Dashboard"));

//Course
const Course = lazy(() => import("./components/course/Course"));
const ContentPage = lazy(() => import("./components/content/ContentPage"));
const Quiz = lazy(() => import("./components/quiz/Quiz"));
const QuizQuestion = lazy(() => import("./components/quiz/QuizQuestion"));
const QuizResults = lazy(() => import("./components/quiz/QuizResults"));

//After course
const LeaveReview = lazy(() => import("./components/aftercourse/LeaveReview"));
const DownloadCertificate = lazy(() =>
  import("./components/aftercourse/DownloadCertificate")
);
const ViewQuizzes = lazy(() => import("./components/aftercourse/ViewQuizzes"));
const ViewCertificates = lazy(() =>
  import("./components/aftercourse/ViewCertificates")
);

//Extra course pages
const AvailableCoursesPage = lazy(() =>
  import("./components/extracourse/AvailableCoursesPage")
);
const EnrolledCoursesPage = lazy(() =>
  import("./components/extracourse/EnrolledCoursesPage")
);
const CompletedCoursesPage = lazy(() =>
  import("./components/extracourse/CompletedCoursesPage")
);

//Header pages
const Settings = lazy(() => import("./components/settings/Settings"));
const SearchResults = lazy(() => import("./components/SearchResults"));

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <ErrorBoundary>
          <Suspense fallback={<h1>Loading...</h1>}>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              <Route path="/resetpassword" element={<ResetPassword />} />
              <Route path="/forgotpassword" element={<ForgotPassword />} />

              <Route path="/dashboard" element={<Dashboard />} />

              <Route path="/course/:courseid" element={<Course />} />
              <Route
                path="/course/:courseid/module/:moduleid/content/:contentid"
                element={<ContentPage />}
              />
              <Route
                path="/course/:courseid/module/:moduleid/quiz/:quizid"
                element={<Quiz />}
              />
              <Route
                path="/course/:courseid/module/:moduleid/quiz/:quizid/quizquestion"
                element={<QuizQuestion />}
              />
              <Route
                path="/course/:courseid/module/:moduleid/quiz/:quizid/quizresults"
                element={<QuizResults />}
              />

              <Route
                path="/course/:courseid/review"
                element={<LeaveReview />}
              />
              <Route
                path="/course/:courseid/certificate"
                element={<DownloadCertificate />}
              />

              <Route path="/settings" element={<Settings />} />
              <Route path="/quizzes" element={<ViewQuizzes />} />
              <Route path="/certificates" element={<ViewCertificates />} />
              <Route
                path="/availablecourses"
                element={<AvailableCoursesPage />}
              />
              <Route
                path="/enrolledcourses"
                element={<EnrolledCoursesPage />}
              />
              <Route
                path="/completedcourses"
                element={<CompletedCoursesPage />}
              />
              <Route path="/searchresults" element={<SearchResults />} />
            </Routes>
          </Suspense>
        </ErrorBoundary>
      </BrowserRouter>
    </div>
  );
}

export default App;
