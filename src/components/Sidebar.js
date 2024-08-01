import React from "react";
import { Nav, NavLink } from "react-bootstrap";
import "../styles/Sidebar.css";

const Sidebar = ({ showSidebar, course, modules, allContent, moduleid }) => {
  return (
    <Nav className={`sidebar ${showSidebar ? "show" : ""}`}>
      <div>
        <div className="sidebar-course">
          <NavLink key={course.id} href={`/course/${course.id}`}>
            {course.courseTitle || `Course ${course.id}`}
          </NavLink>
        </div>

        {modules.map((module) => (
          <div key={module.moduleId}>
            <div className="sidebar-module">
              <Nav.Item>{module.title}</Nav.Item>
            </div>

            <div className="indent">
              {allContent
                .filter((content) => content.moduleId === module.moduleId)
                .map((content) => {
                  switch (content.type) {
                    case "video":
                      return (
                        <NavLink
                          key={content.videoId}
                          href={`/course/${course.id}/module/${module.moduleId}/content/${content.videoId}`}
                        >
                          {content.videoTitle || `Video ${content.videoId}`}
                        </NavLink>
                      );
                    case "reading":
                      return (
                        <NavLink
                          key={content.readingId}
                          href={`/course/${course.id}/module/${module.moduleId}/content/${content.readingId}`}
                        >
                          {content.readingTitle ||
                            `Reading ${content.readingId}`}
                        </NavLink>
                      );
                    case "quiz":
                      return (
                        <Nav.Link
                          key={content.quizId}
                          href={`/course/${course.id}/module/${module.moduleId}/quiz/${content.quizId}`}
                        >
                          {module.title} Quiz
                        </Nav.Link>
                      );
                    default:
                      return null;
                  }
                })}
            </div>
          </div>
        ))}
      </div>
    </Nav>
  );
};

export default Sidebar;
